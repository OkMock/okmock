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

import okmock.CallAction.GeneratedResponse
import okmock.MethodType.POST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * @author Amin Bahiraei (mr.bahiraee@gmail.com)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DataStoreRepositoryTest {


    private lateinit var repository: Repository<Rule>
    @BeforeAll
    fun fixture() {
        repository = RepositoryFactory.createRepository()
    }

    @Test
    fun get() {
        val filter = Filter("http", "example.com", methodType = POST)
        val callAction: CallAction = GeneratedResponse(RawResponse(200, "empty", null, 12, null))
        val rule = Rule(filter = filter, action = callAction)
        assert(repository.add(rule))
        val savedRule = repository.getAll()[0]
        val foundedRule = repository.get(savedRule)
        assert(foundedRule!!.id.equals(savedRule.id))

    }

    @Test
    fun add() {
        val filter = Filter("http", "example.com", methodType = POST)
        val callAction: CallAction = GeneratedResponse(RawResponse(200, "empty", null, 12, null))
        val rule = Rule(filter = filter, action = callAction)
        assert(repository.add(rule))
    }

    @Test
    fun update() {
        val filter = Filter("http", "example.com", methodType = POST)
        val callAction: CallAction = GeneratedResponse(RawResponse(200, "empty", null, 12, null))
        val rule = Rule(filter = filter, action = callAction)
        assert(repository.add(rule))
        val savedRule = repository.getAll()[0]
        savedRule.filter.path = "xxxxxx"
        repository.update(savedRule)
        assert(repository.get(savedRule)!!.id.equals(savedRule.id))
        assert(repository.get(savedRule)!!.filter.path.equals("xxxxxx"))
    }

    @Test
    fun remove() {
        val filter = Filter("http", "example.com", methodType = POST)
        val callAction: CallAction = GeneratedResponse(RawResponse(200, "empty", null, 12, null))
        val rule = Rule(filter = filter, action = callAction)
        repository.remove(rule)
        assertEquals(repository.getAll().size, 4)
    }

    @Test
    fun getAll() {
        val filter = Filter("http", "example.com", methodType = POST)
        val callAction: CallAction = GeneratedResponse(RawResponse(200, "empty", null, 12, null))
        val rule = Rule(filter = filter, action = callAction)
        assert(repository.add(rule))
        assert(repository.add(rule))
        assert(repository.add(rule))
        assert(repository.add(rule))

        assertEquals(repository.getAll().size, 4)

    }

    @Test
    fun deleteAll() {
        val filter = Filter("http", "example.com", methodType = POST)
        val callAction: CallAction = GeneratedResponse(RawResponse(200, "empty", null, 12, null))
        val rule = Rule(filter = filter, action = callAction)
        assert(repository.add(rule))
        assert(repository.add(rule))
        assert(repository.add(rule))
        assert(repository.add(rule))
        repository.deleteAll()
        assert(repository.getAll().isEmpty())
    }

    @Test
    fun search() {

    }
}