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
 * @author Amin Bahiraei (mr.bahiraee@gmail.com)
 */
class DataStoreRepository : Repository<Rule> {


    private lateinit var strategy: (RequestDto, List<Rule>?) -> Rule?


    /**
     * This function set strategy for search in rules
     */
    override fun searchStrategy(f: (RequestDto, List<Rule>?) ->Rule?) {
        strategy = f
    }

    override fun search(request: RequestDto): Rule? {

       return strategy(request,dataStore)
    }

    companion object {
        val dataStore = mutableListOf<Rule>()
    }

    override fun get(item: Rule): Rule? {
        val foundedList = dataStore.filter { it.id == item.id }
        return if (foundedList.isNotEmpty()) foundedList[0]
        else null
    }

    override fun add(item: Rule): Boolean {
        if (item.id == null)
            item.id = java.util.UUID.randomUUID().toString()
        return dataStore.add(item)
    }

    override fun update(item: Rule) {
        val founded = get(item)
        dataStore.remove(founded)
        dataStore.add(founded!!)
    }

    override fun remove(item: Rule) {
        dataStore.remove(item)
    }

    override fun getAll(): List<Rule> {
        return dataStore
    }

    override fun deleteAll() {
        dataStore.clear()
    }
}