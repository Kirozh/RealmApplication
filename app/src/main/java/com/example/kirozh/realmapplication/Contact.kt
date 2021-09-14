package com.example.kirozh.realmapplication

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

/**
 * @author Kirill Ozhigin on 03.09.2021
 */

open class Contact(
    @PrimaryKey
    var id:Int = 10,
    var name: String = "",
    var surname: String = "",
    var number: String = ""
) : RealmObject(){}

