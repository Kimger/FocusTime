package com.kimger.focustime.adpater

import androidx.recyclerview.widget.DiffUtil
import com.kimger.focustime.toJson

/**
 * @author Kimger
 * @email kimger@cloocle.com
 * @date 2019/5/17 11:15
 * @description
 */
internal class DiffUtilCallback<ITEM>(private var oldItems: List<ITEM>,
                                private var newItems: List<ITEM>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition]!!.toJson() == newItems[newItemPosition]!!.toJson()
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition]!!.toJson() == newItems[newItemPosition]!!.toJson()
    }
}

