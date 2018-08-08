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

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import okmock.CallAction
import okmock.Mediator
import okmock.OKMockInterceptor

/**
 * Retrofit Interceptor for OKMock.
 * Can be added to your Retrofit.Builder with OkHttpClient.Builder().addOKMock()
 *
 *
 * @author Adib Faramarzi (adibfara@gmail.com)
 */

internal class RetrofitInterceptor : Interceptor, OKMockInterceptor {

    private lateinit var mediator: Mediator

    override fun onInitialize(mediator: Mediator) {
        this.mediator = mediator

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!::mediator.isInitialized) {
            throw Exception(
                    "OKMock is not configured. Please initialize OKMock before adding the interceptor.")
        }
        val request = chain.request()
        val requestSentAt = System.currentTimeMillis()

        val modifiers = mediator.getCallAction(
                request.getOKMockMethodType(),
                request.url().url(),
                request.headers().toMultimap(),
                request.body().asByteArray()
        )


        val updatedRequest = when (modifiers) {
            is CallAction.GeneratedResponse -> request
            is CallAction.Modify -> request.applyModifiers(modifiers)
        }


        val loggedRequest = updatedRequest.newBuilder().build().let {
            mediator.logRequestCall(
                    it.getOKMockMethodType(),
                    it.url().url(),
                    it.headers().toMultimap(),
                    it.body().asByteArray()
            )
        }
        val response = when (modifiers) {
            is CallAction.GeneratedResponse -> {
                request.createRetrofitResponse(chain, requestSentAt, modifiers)
            }
            is CallAction.Modify -> {
                chain.proceed(updatedRequest).applyModifiers(modifiers)
            }
        }
        response.apply {
            mediator.logResponse(
                    loggedRequest,
                    code(),
                    headers().toMultimap(),
                    receivedResponseAtMillis(),
                    body()?.bytes() ?: ByteArray(0)

            )
        }
        return response


    }

    private fun RequestBody?.asByteArray(): ByteArray {
        if (this == null)
            return ByteArray(0)
        return Buffer().apply { writeTo(this) }.readByteArray()
    }

}
