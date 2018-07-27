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
import okmock.MethodType

fun Request.getOKMockMethodType(): MethodType{
    return when(this.method().toUpperCase()){
        "POST" -> MethodType.POST
        "GET" -> MethodType.GET
        "DELETE" -> MethodType.DELETE
        "PUT" -> MethodType.PUT
        "HEAD" -> MethodType.HEAD
        "PATCH" -> MethodType.PATCH
        else -> throw Exception("Method type not allowed. It must be one of the following: POST, GET, DELETE, PUT, HEAD, PATCH")
    }
}