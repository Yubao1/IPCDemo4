package com.xe.ipcprocess

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

/**
 * Created by 86188 on 2021/6/23.
 */
class MyContentProvider: ContentProvider() {
    var TAG: String = "MyContentProvider"
    var mDb: SQLiteDatabase? = null
    var mContext: Context? = null
    companion object {
        var URI: String = "content://com.zyb.my_provider"
        var BOOK_URI_CODE: Int = 0
        var sUriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH);
        init {
            sUriMatcher.addURI(URI,"book",BOOK_URI_CODE)
        }
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        Log.d(TAG,"------insert----currentThread------" + Thread.currentThread().getName());
        var table = getTableName(uri!!);
        mDb!!.insert(table,null,values);
        mContext!!.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        Log.d(TAG,"------query----currentThread------" + Thread.currentThread().getName());
        var table = getTableName(uri!!);
        return mDb!!.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    fun initProviderData() {
        mDb = DbOpenHelper(mContext!!).getWritableDatabase();
        Thread() {
            kotlin.run {
                mDb!!.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
                mDb!!.execSQL("insert into book values(3,'Android');");
                mDb!!.execSQL("insert into book values(1,'Ios');");
                mDb!!.execSQL("insert into book values(2,'html');");
            }
        }.start()
    }

    override fun onCreate(): Boolean {
        Log.d(TAG,"------onCreate----currentThread------" + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return false;
    }

    fun getTableName(uri: Uri): String{
        var tableName: String = DbOpenHelper.BOOK_TABLE_NAME
        return tableName
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG,"------update----currentThread------" + Thread.currentThread().getName());
        var table = getTableName(uri!!);
        var row = mDb!!.update(table,values,selection,selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG,"------delete----currentThread------" + Thread.currentThread().getName());
        var table = getTableName(uri!!);
        var count = mDb!!.delete(table,selection,selectionArgs)
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    override fun getType(uri: Uri?): String {
        Log.d(TAG,"------getType----currentThread------" + Thread.currentThread().getName());
        return null!!
    }
}