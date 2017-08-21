package org.zhudou.api.todolite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.item.view.*


class MyAdapter(context: Context, items: ArrayList<Map<String, Any>>, resource: Int, from: Array<String>, to: IntArray) : SimpleAdapter(context, items, resource, from, to) {
    var resource=resource
    var context=context
    var to=to
    var items=items
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{
        var view=super.getView(position, convertView, parent)
        var _check=view.checkBox
        var status_value=view.status.text
        var _id=view._id.text
        if(status_value.equals("unfinish")){
            _check.isChecked=false
            //view.title.paint.flags=Paint.ANTI_ALIAS_FLAG
        }else if(status_value.equals("finish")){
            _check.isChecked=true
            //view.title.paint.flags=Paint.STRIKE_THRU_TEXT_FLAG
        }
        return view
    }
}
