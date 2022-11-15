package tia.sarwoedhi.storyapp.core.data.local.data_store_local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import tia.sarwoedhi.storyapp.utils.Constant.TOKEN
import java.io.IOException
import javax.inject.Inject


class DataStoreImpl @Inject constructor(private val context: Context) : DataStoreInterface {

    override suspend fun saveTokenKey(token: String) {
       context.AppDataStore.edit { preferences ->
            preferences[stringPreferencesKey(TOKEN)] = token
        }
    }

    override suspend fun deleteTokenKey() {
        context.AppDataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(TOKEN))
        }
    }

    override suspend fun getTokenKey(): Flow<String> = flow {

        context.AppDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(exception.localizedMessage)
            } else {
                emit(exception.message.toString())
            }
        }.collect { value ->
            emit(value[stringPreferencesKey(TOKEN)] ?: "")
        }

    }
}