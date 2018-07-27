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

import java.net.URL

/**
 * @author Saeed Masoumi (7masoumi@gmail.com)
 */

interface Mediator {
    /**
     * @return RequestModifiers to be applied to the given request parameters by the Interceptors
     */
    fun getCallAction(methodType: MethodType, url: URL,
            headers: Map<String, MutableList<String>>,
            body: ByteArray): CallAction

    /**
     * Notifies OKMock to log this request.
     */
    fun logRequestCall(methodType: MethodType, url: URL,
            toMultimap: Map<String, MutableList<String>>, body: ByteArray): RequestLog

    /**
     * Notifies OKMock to log this response based on a previous request.
     */
    fun logResponse(requestLog: RequestLog, responseCode: Int,
            headers: Map<String, MutableList<String>>, receivedResponseAtMillis: Long,
            body: ByteArray)
}