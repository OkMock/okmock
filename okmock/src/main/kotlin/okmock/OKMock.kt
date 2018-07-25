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
 * The entry point into the *OKMock*.
 *
 * @author Saeed Masoumi (7masoumi@gmail.com)
 */
object OKMock {

    private var agent: OKMockAgent? = null

    /**
     *
     * @param config
     * @throws IllegalStateException
     */
    @Synchronized
    fun setup(config: Config) {
        if (agent != null) {
            throw IllegalStateException("OKMock is already initialized")
        }
        agent = OKMockAgent(config)
        agent?.start()
    }

    /**
     *
     */
    fun addInterceptor(interceptor: OKMockInterceptor) {
        if (agent == null) {
            throw IllegalStateException(
                    "OKMock.setup() must be called before adding any interceptor")
        }
        agent?.addInterceptor(interceptor)
    }

    /**
     * The internal reference to the [agent] for [okmockServer][io.ktor.application.Application.okmockServer] usage.
     *
     * @return the [agent] instance.
     */
    internal fun agent() = agent

    /**
     * Kills and remove the [agent] instance.
     */
    fun shutdown() {
        agent?.kill()
        agent = null
    }
}