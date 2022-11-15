package tia.sarwoedhi.storyapp.core.data.local.data_store_local

import kotlinx.coroutines.flow.Flow

interface DataStoreInterface {
    suspend fun saveTokenKey(token: String)
    suspend fun deleteTokenKey()
    suspend fun getTokenKey(): Flow<String>
}