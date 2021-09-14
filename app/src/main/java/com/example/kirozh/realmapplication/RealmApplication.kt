package com.example.kirozh.realmapplication

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Kirill Ozhigin on 05.09.2021
 */
class RealmApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val configuration = RealmConfiguration.Builder()
            .name("contacts.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()

        Realm.setDefaultConfiguration(configuration)
    }
}