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
    data class Modify(val requestModifyActions: List<RequestModifyAction>, val responseModifyActions: List<ModifyAction.ResponseModifyAction> = listOf()) : CallAction()
}

data class RawResponse(val responseCode: Int, val contentType: String,
        val headers: Map<String, String>, val contentLength: Long, val responseBody: InputStream)


data class RequestLog(val id:String)