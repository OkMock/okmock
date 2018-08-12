/*
 * Copyright (c) 2018, OKMock.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package okmock

import okmock.ModifyAction.RequestModifyAction
import java.io.InputStream
import java.net.URL
import java.util.function.BiConsumer

/**
 * @author Saeed Masoumi (7masoumi@gmail.com)
 * @author Adib Faramarzi (adibfara@gmail.com)
 */

data class Config(val port: Int)

enum class MethodType {
    GET, POST, DELETE, HEAD, PUT, PATCH
}

sealed class CallAction {
    /**
     * Indicates that the interceptor should return the raw response, instead of actually calling the real Webservice.
     */
    data class GeneratedResponse(val response: RawResponse) : CallAction()

    /**
     * Indicates that the interceptor should modify the request with the given list of RequestModifyActions and call the real webservice with the modified request.
     */
    data class ModifyRequest(val modifyActions: List<RequestModifyAction>) : CallAction()
}


data class RawResponse(val responseCode: Int, val contentType: String,
        val headers: Map<String, String>? = null, val contentLength: Long,
        val responseBody: InputStream? = null)


data class RequestLog(val id: String)


data class RequestDto(val methodType: MethodType, val url: URL,
        val headers: Map<String, MutableList<String>>, val body: ByteArray)


//TODO Add AND OR ... to Filter
data class Filter(var schema: String? = null, var baseUrl: String? = null,
        var queryParams: Map<String, String>? = null,
        var path: String? = null,
        var methodType: MethodType? = null, var headers: Map<String, String>? = null)


data class Rule(var id: String? = null, var filter: Filter, var action: CallAction) {

    fun match(requestDto: RequestDto): Rule? {
        filter.path?.let { filterPath ->
            requestDto.url.path.let {
                if (it == filterPath) return this
            }
        }
        filter.methodType?.let { filterMethodType ->
            requestDto.methodType.let {
                if (it == filterMethodType) return this
            }
        }
        filter.headers?.let { filterHeaders ->
            val mutableFilterHeaders = HashMap(filterHeaders)
            requestDto.headers.forEach { requestRaw ->
                if (filterHeaders.containsKey(requestRaw.key)){
                    if (requestRaw.value.contains(filterHeaders[requestRaw.key])){
                        mutableFilterHeaders.remove(requestRaw.key)
                        if (mutableFilterHeaders.size == 0) return this
                    }
                }
            }
        }
        filter.queryParams?.let { filterQueryParams ->
            val mutableFilterQueryParam = HashMap(filterQueryParams)
            requestDto.headers.forEach { raw ->
                if (filterQueryParams.containsKey(raw.key)) {
                    mutableFilterQueryParam.remove(raw.key)
                    if (mutableFilterQueryParam.size == 0) return this
                }
            }
        }
        filter.schema?.let { filterSchema ->
            requestDto.url.protocol.let {
                if (it == filterSchema) return this
            }
        }

        filter.baseUrl?.let { filterBaseUrl ->
            requestDto.url.host.let {
                if (it == filterBaseUrl) return this
            }
        }

        return null
    }
}

