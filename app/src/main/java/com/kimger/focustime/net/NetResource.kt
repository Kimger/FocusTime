/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kimger.focustime.net

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.kimger.focustime.net.vo.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
class NetResource<ResultType> {

    companion object {
        fun <T> get(): NetResource<T> {
            return NetResource()
        }
    }

    private val result = MediatorLiveData<Resource<ResultType>>()

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetworkProcess(apiResponse: LiveData<ApiResponse<ResultType>>): LiveData<Resource<ResultType>> {
        setValue(Resource.loading(null))
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    setValue(Resource.success(processResponse(response)))
                    setValue(Resource.complete(processResponse(response)))
                }
                is ApiEmptyResponse -> {
                    setValue(Resource.success(null))
                    setValue(Resource.complete(null))
                }
                is ApiErrorResponse -> {
                    setValue(Resource.error(response.errorMessage, null))
                    setValue(Resource.complete(null))
                }
            }
        }
        return asLiveData()
    }

    private fun asLiveData() = result as LiveData<Resource<ResultType>>

//    private fun saveCallResult(item: ResultType) {
//        val clazz = item as Class<*>
//        Hawk.put(clazz.name, item)
//    }

    @WorkerThread
    private fun processResponse(response: ApiSuccessResponse<ResultType>) = response.body

    fun fetchFromNetwork(method: (LiveData<ApiResponse<ResultType>>)): LiveData<Resource<ResultType>> {
        return this.fetchFromNetworkProcess(method)
    }

}
