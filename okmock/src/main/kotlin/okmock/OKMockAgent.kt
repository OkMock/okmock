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
 * @author Saeed Masoumi (7masoumi@gmail.com)
 */

internal class OKMockAgent(config: Config) {

    private val server: Server = Server(config)
    private val interceptors = arrayListOf<OKMockInterceptor>()
    private val mediator: Mediator = object : Mediator {

    }

    fun start() {
        server.start()
    }

    fun kill() {
        server.stop()
    }

    fun addInterceptor(interceptor: OKMockInterceptor) {
        interceptor.onInitialize(mediator)
        interceptors.add(interceptor)
    }
}