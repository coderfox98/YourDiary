package com.example.yourdiary

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.COLUMN_DATE
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.COLUMN_DIARY
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.COLUMN_TITLE
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.TABLE_NAME
import com.example.yourdiary.data.Diary
import com.example.yourdiary.data.DiaryDBHelper
import kotlinx.android.synthetic.main.activity_diary.*

class DiaryActivity : AppCompatActivity() {

    private lateinit var mDBHelper: DiaryDBHelper

    private var diaryList : ArrayList<Diary> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)


        mDBHelper = DiaryDBHelper(this)

//        displayDataInfo()





    }

    fun displayDataInfo() {
        val db = mDBHelper.readableDatabase

        val projection = arrayOf(_ID, COLUMN_DATE, COLUMN_TITLE, COLUMN_DIARY)

        val cursor : Cursor = db.query(TABLE_NAME,projection,null,null,null,null,null)

        val idColumnIndex = cursor.getColumnIndexOrThrow(_ID)
        val dateColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE)
        val titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
        val diaryColumIndex = cursor.getColumnIndexOrThrow(COLUMN_DIARY)

        while (cursor.moveToNext()) {

            val currentId = cursor.getInt(idColumnIndex)
            val currentDate = cursor.getString(dateColumnIndex)
            val currentTitle = cursor.getString(titleColumnIndex)
            val currentDiary = cursor.getString(diaryColumIndex)

            diaryList.add(Diary(currentId,currentDate,currentTitle,currentDiary))
        }

        cursor.close()

        linearLayoutManager = LinearLayoutManager(this)

        diary_recycler_view.layoutManager = linearLayoutManager
        adapter = DiaryAdapter(diaryList)
        diary_recycler_view.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        diaryList.clear()
        displayDataInfo()
    }

    fun createNewDiary(view : View){
        val intent = Intent(this, NewDiaryActivity::class.java)
        startActivity(intent)
    }
}
