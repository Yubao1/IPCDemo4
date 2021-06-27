package com.xe.ipcdemo4

import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

/**
 * Created by 86188 on 2021/6/23.
 */
class MainActivity: AppCompatActivity() {
    var mContentObserver: ContentObserver? = null
    companion object {
        var URI: String = "content://com.zyb.my_provider"//com.zyb.provider
        var TAG: String = "MainActivity"
    }
    var mTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun onClick(v: View) {
        var bookUri: Uri = Uri.parse(URI + "/book");
        var cv: ContentValues = ContentValues();
        cv.put("_id",7);
        cv.put("name","西游记");
        Thread() {
            kotlin.run {
                getContentResolver().insert(bookUri,cv);
            }
        }.start()

        Toast.makeText(this,"插入数据成功", Toast.LENGTH_SHORT).show();
    }

    fun init() {
        mTv = findViewById(R.id.tv);
        mContentObserver = MyContentObserver(Handler());
        var bookUri: Uri = Uri.parse(URI + "/book");
        var cv: ContentValues = ContentValues();
        cv.put("_id",6);
        cv.put("name","程序设计");
        getContentResolver().insert(bookUri,cv);
        var bookCursor: Cursor = getContentResolver().query(bookUri,arrayOf("_id","name"),null,null,null);
        while (bookCursor.moveToNext()) {
            var book: Book  = Book();
            book.mId = bookCursor.getInt(0)
            book.mName = bookCursor.getString(1)
            Log.d(TAG,"query book:" + book.toString());
        }
        bookCursor.close();
        getContentResolver().registerContentObserver(bookUri,true,mContentObserver);
    }


    inner class MyContentObserver(h: Handler): ContentObserver(h) {
        var TAG: String = "MyContentObserver"
        var sb: StringBuffer = StringBuffer()
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            var bookUri: Uri = Uri.parse(URI + "/book");
            var bookCursor: Cursor = getContentResolver().query(bookUri, arrayOf("_id","name"),null,null,null);
            while (bookCursor.moveToNext()) {
                var book: Book = Book();
                book.mId = bookCursor.getInt(0)
                book.mName = bookCursor.getString(1)
                sb.append(book.toString() + "\n")
                Log.d(TAG,"query book-----------onChange---" + book.toString());
            }
            mTv!!.setText(sb)
            bookCursor.close();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getContentResolver().unregisterContentObserver(mContentObserver)
    }
}