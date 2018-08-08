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

import okhttp3.Interceptor.Chain
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Okio
import okmock.CallAction
import okmock.ModifyAction

/**
 * Creates a new retrofit response based off a CallAction.
 * Will call the real webservice when the CallAction is not a GeneratedResponse
 *
 * @author Adib Faramarzi (adibfara@gmail.com)
 */
fun Request.createRetrofitResponse(chain: Chain, requestSentAt: Long, callAction: CallAction): Response {
    return when (callAction) {
        is CallAction.GeneratedResponse -> Response.Builder().apply {
            callAction.response.headers.forEach { key, value ->
                addHeader(key, value)
            }

            code(callAction.response.responseCode)

            body(ResponseBody.create(MediaType.parse(callAction.response.contentType),
                    callAction.response.contentLength,
                    Okio.buffer(Okio.source(callAction.response.responseBody))))

            sentRequestAtMillis(requestSentAt)

            receivedResponseAtMillis(System.currentTimeMillis())

        }.build()
        is CallAction.Modify -> chain.proceed(this)
    }
}


/**
 * Applies the set of ResponseModifiers to the response and returns a new one.
 * @author Adib Faramarzi (adibfara@gmail.com)
 */
internal fun Response.applyModifiers(callModifyAction: CallAction.Modify): Response {
    return this.newBuilder()
            .apply {
                callModifyAction.responseModifyActions.forEach {
                    when (it) {
                        is ModifyAction.ResponseModifyAction.AddHeader -> {
                            addHeader(it.name, it.value)
                        }
                        is ModifyAction.ResponseModifyAction.RemoveHeader -> {
                            removeHeader(it.name)
                        }
                    }
                }
            }.build()
}