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

internal class OKMockAgent(config: Config) {

    private val server: Server = Server(config)
    private val interceptors = mutableListOf<OKMockInterceptor>()
    private val repository = RepositoryFactory.createRepository()
    private val ruleEngine = RuleEngine(repository)

    private val actionModifier = ActionModifierImp(ruleEngine)
    private val logger = LoggerImp()

    private val mediator: Mediator = object : Mediator {
        override fun getCallAction(methodType: MethodType, url: URL,
                headers: Map<String, MutableList<String>>, body: ByteArray): CallAction {
            return actionModifier.matcher(methodType, url, headers, body).action
        }

        override fun logRequestCall(methodType: MethodType, url: URL,
                headers: Map<String, MutableList<String>>, body: ByteArray): RequestLog {
            server.notify(methodType, url, headers, body)
            return logger.logRequest(methodType, url, headers, body)
        }

        override fun logResponse(requestLog: RequestLog, responseCode: Int,
                headers: Map<String, MutableList<String>>, receivedResponseAtMillis: Long,
                body: ByteArray) {
            logger.logResponse(requestLog, responseCode, headers, receivedResponseAtMillis, body)
        }

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