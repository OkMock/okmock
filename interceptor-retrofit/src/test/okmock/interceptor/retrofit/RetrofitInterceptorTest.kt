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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL


/**
 * @author Adib Faramarzi (adibfara@gmail.com)
 */
internal class RetrofitInterceptorTest {

    val retrofitInterceptor = spyk(RetrofitInterceptor())


    val sampleRequest = sampleRetrofitRequest(headers = mapOf("okmock" to "primary"))

    val sampleResponse = sampleRetrofitResponse(sampleRequest, 200, "sample message", headers = mapOf("response_ok" to "true"), body =)
    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `test that a header exists when added to request`() {
        retrofitInterceptor.onInitialize(object : SampleMediator() {
            override fun getCallAction(methodType: MethodType, url: URL, headers: Map<String, MutableList<String>>, body: ByteArray): CallAction {
                return CallAction.ModifyRequest(listOf(
                        ModifyAction.RequestModifyAction.AddHeader("test-added-header", "test-added-value")
                ))
            }

        })
        retrofitInterceptor.intercept(createChain(sampleRequest, sampleResponse)).
    }
}