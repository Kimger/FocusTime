package com.kimger.focustime

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.kimger.focustime.adpater.AbstractAdapter
import com.kimger.focustime.adpater.setUp
import com.kimger.focustime.sql.DatabaseManager
import com.kimger.focustime.sql.entity.TodoRecordEntity
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.item_record_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-13 15:06
 * @email kimger@cloocle.com
 * @description
 */
class RecordActivity : BaseActivity() {
    private lateinit var adapter: AbstractAdapter<TodoRecordEntity>
    private lateinit var recordList: MutableList<TodoRecordEntity>
    override fun getLayoutId() = R.layout.activity_record

    @SuppressLint("SetTextI18n")
    override fun init() {
        recordList = ArrayList()
        adapter =
            rv_record.setUp(recordList, R.layout.item_record_list, { holder, todoRecordEntity ->
                holder.itemView.tv_item_title.text = todoRecordEntity.title
                holder.itemView.tv_item_time.text =
                    "${Helper.get().time2m(todoRecordEntity.time)}分钟"
                holder.itemView.tv_item_create_date.text =
                    Helper.get().long2Str(todoRecordEntity.createDate)
                if (todoRecordEntity.status == 1) {
                    holder.itemView.tv_item_user_time.text = ""
                    holder.itemView.tv_item_status.text = "完成"
                    holder.itemView.tv_item_status.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.green
                        )
                    )
                } else {
                    holder.itemView.tv_item_user_time.text = "(${Helper.get().time2m(todoRecordEntity.userTime)}分钟)"
                    holder.itemView.tv_item_status.text = "放弃"
                    holder.itemView.tv_item_status.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.red
                        )
                    )
                }

            })

        launch(Dispatchers.IO) {
            DatabaseManager.dataBase.todoRecordDao().getTodoRecord().collect {
                withContext(Dispatchers.Main) {
                    val list = it.toMutableList()
                    list.sortBy { it.createDate }
                    list.reverse()
                    adapter.update(list)
                }
            }
        }
    }
}