package com.example.calculapp.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.calculapp.data.preference.model.KeepLoginModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreKeepLogin: DataStore<Preferences> by preferencesDataStore(name = "keep-login")

class KeepLogin(val context: Context) {

    //Constants
    companion object {
        //KeepLogin keys
        const val KEY_USER_IDENTIFICATION_NUMBER = "keyUserIdentificationNumber"
        const val KEY_AUTO_LOGIN = "keyAutomaticLogin"
    }


    //DataStore keepLogin methods
    //Function to save USER_IDENTIFICATION_NUMBER for auto login
    suspend fun saveUserIdentificationNumber(userIdentificationNumber: Long) {
        context.dataStoreKeepLogin.edit { keepLogin ->
            keepLogin[longPreferencesKey(KEY_USER_IDENTIFICATION_NUMBER)] = userIdentificationNumber
        }
    }


    /*
    * Function to save value boolean for auto login
    * true = auto login (with user_identification_number)
    * false = no auto login (init normal application)
    */
    suspend fun saveAutoLogin(autoLogin: Boolean) {
        context.dataStoreKeepLogin.edit { keepLogin ->
            keepLogin[booleanPreferencesKey(KEY_AUTO_LOGIN)] = autoLogin
        }
    }


    //Function to return login model for auto login
    fun getAutoLogin(): Flow<KeepLoginModel> {
        return context.dataStoreKeepLogin.data.map { keepLogin ->
            KeepLoginModel(
                userIdentificationNumber = keepLogin[longPreferencesKey(KEY_USER_IDENTIFICATION_NUMBER)] ?: -1,
                logging = keepLogin[booleanPreferencesKey(KEY_AUTO_LOGIN)] ?: false
            )
        }
    }


}