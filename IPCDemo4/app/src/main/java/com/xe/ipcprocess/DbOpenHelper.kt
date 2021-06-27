package com.xe.ipcprocess

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by 86188 on 2021/6/24.
 */
class DbOpenHelper: SQLiteOpenHelper {
    companion object {
        var BOOK_TABLE_NAME: String = "book"
        var DB_NAME: String = "book_provider.db"
        var DB_VERSION: Int = 1
    }
    var CREATE_BOOK_TABLE: String = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT)"
    constructor(context: Context):super(context, DB_NAME, null, DB_VERSION) {
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_BOOK_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}