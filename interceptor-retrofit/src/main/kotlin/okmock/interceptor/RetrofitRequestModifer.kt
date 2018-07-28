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

package okmock.interceptor

import okhttp3.Request
import okmock.CallAction
import okmock.ModifyAction

/**
 * Applies the set of RequestModifiers to the request and returns a new one.
 * @author Adib Faramarzi (adibfara@gmail.com)
 */
internal fun Request.applyModifiers(callModiyAction: CallAction.ModifyRequest): Request {
    return this.newBuilder()
            .apply {
                callModiyAction.modifyActions.forEach {
                    val action = when (it) {
                        is ModifyAction.RequestModifyAction.AddHeader -> {

                        }
                        is ModifyAction.RequestModifyAction.RemoveHeader -> {

                        }
                    }
                }
            }.build()
}