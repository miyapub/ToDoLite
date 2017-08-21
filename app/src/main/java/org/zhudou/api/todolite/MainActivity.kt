package org.zhudou.api.todolite

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    val todolist = ArrayList<Map<String, Any>>()
    var simpleAdapter: MyAdapter? =null
    lateinit var dbHelper:DbHelper

    fun upUI(){
        val c = dbHelper.cursor("select * from todolist order by _id")
        if (c != null) {
            todolist.clear()
            while (c.moveToNext()) {
                val id = c.getInt(c.getColumnIndex("_id"))
                val title = c.getString(c.getColumnIndex("title"))
                val status = c.getString(c.getColumnIndex("status"))
                val map = HashMap<String, Any>()
                map.put("id", id);
                map.put("title", title);
                map.put("status", status);
                todolist.add(map)
            }
            simpleAdapter?.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper=DbHelper(this)



        if(!dbHelper.isTableExist("todolist")){
            dbHelper.exec("create table todolist(_id integer primary key autoincrement,title text,status text)")
        }

        simpleAdapter = MyAdapter(baseContext, todolist,
                R.layout.item, arrayOf("id","title","status"), intArrayOf(R.id._id,R.id.checkBox,R.id.status))
        list.adapter=simpleAdapter

        upUI()

        imageView_button.setOnClickListener(View.OnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setTitle("添加代办记事")
            var title_input=EditText(this)
            alertDialog.setView(title_input)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"确定", DialogInterface.OnClickListener({ dialogInterface: DialogInterface, i: Int ->
                var title=title_input.text
                dbHelper.exec("insert into todolist (title,status) values('$title','unfinish')")
                upUI()
            }))
            alertDialog.show()
        })

        imageView_button.setOnLongClickListener(View.OnLongClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setTitle("删除全部的代办记事？")
            var title_input=EditText(this)
            //alertDialog.setView(title_input)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"删除", DialogInterface.OnClickListener({ dialogInterface: DialogInterface, i: Int ->
                //var title=title_input.text
                //dbHelper.exec("insert into todolist (title,status) values('$title','unfinish')")
                dbHelper.exec("delete from todolist")
                upUI()
            }))
            alertDialog.show()
            return@OnLongClickListener true
        })

        list.onItemClickListener=AdapterView.OnItemClickListener{adapterView, view, i, l ->
            var _id=view._id.text

            var status_value=view.status.text
            if(status_value.equals("finish")){
                //view.title.paint.flags=Paint.STRIKE_THRU_TEXT_FLAG
                status_value="unfinish"
            }else if(status_value.equals("unfinish")){
                //view.title.paint.flags=Paint.ANTI_ALIAS_FLAG
                status_value="finish"
            }
            dbHelper.exec("update todolist set status='$status_value' where _id='$_id'")
            upUI()
        }


        list.onItemLongClickListener= AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            var _id=view._id.text
            val alertDialogBuilder = AlertDialog.Builder(this)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setTitle("删除代办记事？")
            var title_input=EditText(this)
            //alertDialog.setView(title_input)
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"删除", DialogInterface.OnClickListener({ dialogInterface: DialogInterface, i: Int ->
                //var title=title_input.text
                //dbHelper.exec("insert into todolist (title,status) values('$title','unfinish')")
                dbHelper.exec("delete from todolist where _id='$_id'")
                upUI()
            }))
            alertDialog.show()
            return@OnItemLongClickListener true
        }
    }
}
