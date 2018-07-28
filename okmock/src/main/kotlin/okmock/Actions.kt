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

/**
 * Actions used to modify the request and response.
 *
 * @author Adib Faramarzi (adibfara@gmail.com)
 */

sealed class ModifyAction {
    /**
     * Actions that affect the request.
     */
    sealed class RequestModifyAction {
        /**
         * Adds a header to the request. Should replace a header, If one already exists.
         */
        data class AddHeader(val name: String, val value: String) : RequestModifyAction()

        /**
         * Removes a header from the request.
         */
        data class RemoveHeader(val name: String) : RequestModifyAction()
    }
}