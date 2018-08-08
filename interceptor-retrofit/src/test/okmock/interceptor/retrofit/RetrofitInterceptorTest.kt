/*
 *
 *  * Copyright (c) 2018, OKMock.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package okmock.interceptor.retrofit

import io.mockk.spyk
import okmock.CallAction
import okmock.MethodType
import okmock.ModifyAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL


/**
 * @author Adib Faramarzi (adibfara@gmail.com)
 */
internal class RetrofitInterceptorTest {

    val retrofitInterceptor = spyk(RetrofitInterceptor())


    val sampleRequest = sampleRetrofitRequest(headers = mapOf("okmock" to "primary"))

    val sampleResponse = sampleRetrofitResponse(sampleRequest, 200, "sample message", headers = mapOf("response_ok" to "true"), body = createResponseBody())
    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `test that a header exists when added to request`() {
        val expectedHeaderValue = "test-added-value"
        val expectedHeaderKey = "test-added-key"
        retrofitInterceptor.onInitialize(object : SampleMediator() {
            override fun getCallAction(methodType: MethodType, url: URL, headers: Map<String, MutableList<String>>, body: ByteArray): CallAction {
                return CallAction.Modify(listOf(
                        ModifyAction.RequestModifyAction.AddHeader(expectedHeaderKey, expectedHeaderValue)
                ))
            }

        })
        retrofitInterceptor.intercept(TestChain(sampleRequest, sampleResponse) { request ->
            assertEquals(expectedHeaderValue, request!!.header(expectedHeaderKey))
        })
    }

    @Test
    fun `test that a header does not exist when removed from request`() {
        val expectedHeaderValue = "test-added-value"
        val expectedHeaderKey = "test-added-key"
        val sampleRequest = sampleRetrofitRequest(headers = mapOf(expectedHeaderKey to expectedHeaderValue))
        retrofitInterceptor.onInitialize(object : SampleMediator() {
            override fun getCallAction(methodType: MethodType, url: URL, headers: Map<String, MutableList<String>>, body: ByteArray): CallAction {
                return CallAction.Modify(listOf(
                        ModifyAction.RequestModifyAction.RemoveHeader(expectedHeaderKey)
                ))
            }

        })
        retrofitInterceptor.intercept(TestChain(sampleRequest, sampleResponse) { request ->
            assertNull(request!!.header(expectedHeaderKey))
        })
    }

    @Test
    fun `test that a header exists when added to a response`() {
        val expectedHeaderValue = "test-added-value"
        val expectedHeaderKey = "test-added-key"
        retrofitInterceptor.onInitialize(object : SampleMediator() {
            override fun getCallAction(methodType: MethodType, url: URL, headers: Map<String, MutableList<String>>, body: ByteArray): CallAction {
                return CallAction.Modify(listOf(), listOf(
                        ModifyAction.ResponseModifyAction.AddHeader(expectedHeaderKey, expectedHeaderValue)
                ))
            }

        })
        val response = retrofitInterceptor.intercept(TestChain(sampleRequest, sampleResponse))
        assertEquals(expectedHeaderValue, response!!.header(expectedHeaderKey))
    }


    @Test
    fun `test that a header is removed when removed from a response`() {
        val expectedHeaderKey = "response_ok"
        val sampleRequest = sampleRetrofitRequest()
        retrofitInterceptor.onInitialize(object : SampleMediator() {
            override fun getCallAction(methodType: MethodType, url: URL, headers: Map<String, MutableList<String>>, body: ByteArray): CallAction {
                return CallAction.Modify(listOf(), listOf(
                        ModifyAction.ResponseModifyAction.RemoveHeader(expectedHeaderKey)
                ))
            }

        })
        val response = retrofitInterceptor.intercept(TestChain(sampleRequest, sampleResponse))
        assertNull(response!!.header(expectedHeaderKey))
    }

}