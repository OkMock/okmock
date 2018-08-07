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

import okhttp3.*
import okmock.Mediator
import okmock.MethodType
import okmock.RequestLog
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * @author Adib Faramarzi (adibfara@gmail.com)
 */

/**
 * @param url Request URL
 * @param method HTTP Method for the request
 * @param headers Map of the request headers
 * @param body Request body, required for POST, DELETE, PUT and PATCH methods.
 * @return OKHttp Request based on the given criteria.
 */
fun sampleRetrofitRequest(url: String = "http://okmock.com/v1/getRequests/", method: MethodType = MethodType.GET, headers: Map<String, String> = mapOf(), body: RequestBody? = null): Request {
    return Request.Builder()
            .apply {
                headers.forEach { key, value ->
                    addHeader(key, value)
                }
                when (method) {
                    MethodType.GET -> get()
                    MethodType.POST -> post(body!!)
                    MethodType.DELETE -> delete(body)
                    MethodType.HEAD -> head()
                    MethodType.PUT -> put(body!!)
                    MethodType.PATCH -> patch(body!!)
                }
                url(url)
            }
            .build()
}

/**
 * @param request OKHttp request given to the response
 * @param code Response code
 * @param message Http message
 * @param receivedResponseMillis Epoch time of received response
 * @param headers Optional headers for response
 * @param body Response body
 * @return OKHttp Response based on the given criteria.
 */
fun sampleRetrofitResponse(request: Request, code: Int, message: String? = null, receivedResponseMillis: Long = System.currentTimeMillis(), headers: Map<String, String> = mapOf(), body: ResponseBody? = null): Response {
    return Response.Builder()
            .code(code)
            .body(body)
            .request(request)
            .receivedResponseAtMillis(receivedResponseMillis)
            .apply {
                headers.forEach { key, value ->
                    addHeader(key, value)
                }
                message?.let {
                    message(it)
                }

            }
            .build()
}

/**
 * Creates a ResponseBody from a given String and it's contentType
 * @param fromString String of the response body
 * @param contentType contentType (MediaType) of the response. defaults to `application/json`.
 */
fun createResponseBody(fromString: String = "{\"sample\": \"true\"}", contentType: MediaType = MediaType.parse("application/json")!!): ResponseBody {
    return ResponseBody.create(contentType, fromString)
}

/**
 * Creates a ResponseBody from a given ByteArray and it's contentType
 * @param fromByteArray ByteArray toe create a response body from
 * @param contentType contentType (MediaType) of the response. defaults to `application/json`.
 */
fun createResponseBody(fromByteArray: ByteArray, contentType: MediaType): ResponseBody {
    return ResponseBody.create(contentType, fromByteArray)
}

/**
 * Creates a RequestBody from a given String and it's contentType
 * @param fromString String of the request body
 * @param contentType contentType (MediaType) of the request. defaults to `application/json`.
 */
fun createRequestBody(fromString: String = "{\"sample\": \"true\"}", contentType: MediaType = MediaType.parse("application/json")!!): RequestBody {
    return RequestBody.create(contentType, fromString)
}

/**
 * Creates a RequestBody from a given ByteArray and it's contentType
 * @param fromByteArray ByteArray toe create a request body from
 * @param contentType contentType (MediaType) of the request. defaults to `application/json`.
 */
fun createRequestBody(fromByteArray: ByteArray, contentType: MediaType): RequestBody {
    return RequestBody.create(contentType, fromByteArray)
}

fun createChain(request: Request, response: Response): Interceptor.Chain {
    return object : Interceptor.Chain {
        override fun writeTimeoutMillis(): Int {
            return 1
        }

        override fun call(): Call {
            return object : Call {
                override fun enqueue(responseCallback: Callback?) {

                }

                override fun isExecuted(): Boolean {
                    return true
                }

                override fun clone(): Call {
                    return this
                }

                override fun isCanceled(): Boolean {
                    return false
                }

                override fun cancel() {

                }

                override fun request(): Request {
                    return request
                }

                override fun execute(): Response {
                    return response
                }

            }
        }

        override fun proceed(request: Request?): Response {
            return response
        }

        override fun withWriteTimeout(timeout: Int, unit: TimeUnit?): Interceptor.Chain {
            return this
        }

        override fun connectTimeoutMillis(): Int {
            return 1
        }

        override fun connection(): Connection? {
            return null
        }

        override fun withConnectTimeout(timeout: Int, unit: TimeUnit?): Interceptor.Chain {
            return this
        }

        override fun withReadTimeout(timeout: Int, unit: TimeUnit?): Interceptor.Chain {
            return this
        }

        override fun request(): Request {
            return request
        }

        override fun readTimeoutMillis(): Int {
            return 1
        }

    }
}

abstract class SampleMediator : Mediator {

    override fun logRequestCall(methodType: MethodType, url: URL, toMultimap: Map<String, MutableList<String>>, body: ByteArray): RequestLog {
        return RequestLog("1")
    }

    override fun logResponse(requestLog: RequestLog, responseCode: Int, headers: Map<String, MutableList<String>>, receivedResponseAtMillis: Long, body: ByteArray) {
        // nothing to do in testing!
    }

}