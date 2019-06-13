package com.example.yourdiary

import android.content.ContentValues
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.COLUMN_DATE
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.COLUMN_DIARY
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.COLUMN_TITLE
import com.example.yourdiary.data.DatabaseManager.DiaryEntry.TABLE_NAME
import com.example.yourdiary.data.DiaryDBHelper
import kotlinx.android.synthetic.main.activity_new_diary.*
import java.text.SimpleDateFormat
import java.util.*

class NewDiaryActivity : AppCompatActivity() {

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_diary)

        id = intent.getIntExtra("idOfRow",0)

        if(id != 0) {
            readDiary(id)
        }

        Log.d("NewDiary","The passed Id is:$id")

        val currentDate = SimpleDateFormat("EEE, d MMM yyyy")
        date_NewDiary_text_view.text = currentDate.format(Date())
    }

    private fun readDiary(id: Int) {
        val mDBHelper = DiaryDBHelper(this)

        val db = mDBHelper.readableDatabase

        val projection = arrayOf(COLUMN_DATE, COLUMN_TITLE, COLUMN_DIARY)

        val selection = "$_ID = ?"
        val selectionArgs = arrayOf("$id")

        val cursor : Cursor = db.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val dateColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE)
        val titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
        val diaryColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_DIARY)

        while (cursor.moveToNext()) {
            val currentDate = cursor.getString(dateColumnIndex)
            val currentTitle = cursor.getString(titleColumnIndex)
            val currentDiary = cursor.getString(diaryColumnIndex)

            date_NewDiary_text_view.text = currentDate
            title_newDiary_edit_text.setText(currentTitle)
            diary_text_newDiary_text_view.setText(currentDiary)
        }

        cursor.close()
    }

    private fun insertDiary() {

        val dateString = date_NewDiary_text_view.text.toString()
        val titleString = title_newDiary_edit_text.text.toString().trim(){it <= ' '}
        val diaryString = diary_text_newDiary_text_view.text.toString().trim(){it <= ' '}

        val mDBHelper = DiaryDBHelper(this)

        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_DATE,dateString)
            put(COLUMN_TITLE,titleString)
            put(COLUMN_DIARY,diaryString)
        }

        val rowId = db.insert(TABLE_NAME,null,values)

        if (rowId.equals(-1)) {
            Toast.makeText(this,"Problem Inserting New Diary",Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this,"Saved your Diary Panda", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateDiary(id: Int) {

        val mDBHelper = DiaryDBHelper(this)

        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE,title_newDiary_edit_text.text.toString())
            put(COLUMN_DIARY,diary_text_newDiary_text_view.text.toString())
        }

        db.update(TABLE_NAME,values,"$_ID = $id", null)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.save_diary -> {
                if(id == 0) {
                    insertDiary()
                }else {
                    updateDiary(id)
                }

                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        if(id == 0) {
//            insertDiary()
//        }else {
//            updateDiary(id)
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if(id == 0) {
//            insertDiary()
//        }else {
//            updateDiary(id)
//        }
//    }
}
