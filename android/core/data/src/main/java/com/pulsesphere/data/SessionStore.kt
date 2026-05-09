package com.pulsesphere.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

object SessionStoreKeys {
  val ACCESS_TOKEN: Preferences.Key<String> = stringPreferencesKey("access_token")
}
