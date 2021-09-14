package com.kimger.focustime.adpater

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kimger.focustime.inflate

/**
 * @author Kimger
 * @email kimger@cloocle.com
 * @date 2019/5/17 11:17
 * @description
 */
public abstract class AbstractAdapter<ITEM>(var itemList: MutableList<ITEM>, private val initHolder: (Holder, Int) -> Unit,
                                            private var emptyLayout: Int = 0)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var position = 0

    /**
     * 空数据时，显示空布局类型
     */
    private val EMPTY_VIEW = 1
    /**
     * 控制空布局是否显示
     */
    private var mEmptyType = 0

    override fun getItemCount(): Int {
        if (itemList.isEmpty()) {
            return mEmptyType
        }
        return itemList.size + mEmptyType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (EMPTY_VIEW == viewType && emptyLayout != 0) {
            val view = parent inflate emptyLayout
            return EmptyHolder(view)
        }
        val view = createItemView(parent, viewType)
        val viewHolder = Holder(view)
        val itemView = viewHolder.itemView
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
        initHolder(viewHolder, viewType)
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        if (mEmptyType == EMPTY_VIEW && emptyLayout != 0) {
            return EMPTY_VIEW
        }
        return super.getItemViewType(position)
    }

    fun setSelected(position: Int) {
        this.position = position
        notifyDataSetChanged()
    }

    fun getSelected() = position


    fun update(items: MutableList<ITEM>, bySize: Boolean = false) {

        if (items.isNotEmpty()) {
            if (mEmptyType == 1) {
                mEmptyType = 0
                notifyItemRemoved(0)
            }
            //刷新，新添加的数据
            if (bySize) {
                val calculateDiff = calculateDiffBySize(items)
                updateAdapterWithDiffResult(calculateDiff)
            } else {
                val calculateDiff = calculateDiff(items)
                updateAdapterWithDiffResult(calculateDiff)
            }
            itemList = items
        } else {
            itemList = items
            if (mEmptyType != 1 && emptyLayout != 0) {
                mEmptyType = 1
                notifyItemChanged(0)
            }else{
                itemList = items
                notifyDataSetChanged()
            }
        }
    }


    fun setData(items: MutableList<ITEM>) {

//        if (itemList.isNotEmpty()) {
//            val size = itemList.size
//            itemList.clear()
//            notifyItemRangeRemoved(0, size)
//        }
        if (items.isNotEmpty()) {
            if (mEmptyType == 1) {
                mEmptyType = 0
                notifyItemRemoved(0)
            }
            itemList = items
            notifyDataSetChanged()
        } else {
            itemList = items
            if (mEmptyType != 1 && emptyLayout != 0) {
                mEmptyType = 1
                notifyItemChanged(0)
            }else{
                notifyDataSetChanged()
            }
        }

    }

    private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
    }

    private fun calculateDiff(newItems: MutableList<ITEM>) =
            DiffUtil.calculateDiff(DiffUtilCallback(itemList, newItems), true)

    private fun calculateDiffBySize(newItems: MutableList<ITEM>) =
            DiffUtil.calculateDiff(DiffUtilCallbackBySize(itemList, newItems), true)

    fun add(item: MutableList<ITEM>) {
        val list = ArrayList<ITEM>()
        list.addAll(itemList)
        list.addAll(item)
        update(list)
    }

//    fun addAll(items: List<ITEM>) {
//        itemList.toMutableList().addAll(items)
//        notifyItemInserted(itemList.size)
//    }

    fun add(item: ITEM) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
        notifyItemRangeChanged(0, itemList.size - 1)
    }

    fun remove(position: Int) {
        itemList.removeAt(position)
        update(itemList)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(0, itemList.size - position)
    }

    final override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        onViewRecycled(holder.itemView)
    }

    protected open fun onViewRecycled(itemView: View) {
    }

    protected open fun onItemClick(itemView: View, position: Int) {
    }

    protected abstract fun createItemView(parent: ViewGroup, viewType: Int): View

    open class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val views = SparseArray<View>()

        fun <T : View> getView(viewId: Int): T {
            var view = views[viewId]
            if (view == null) {
                view = itemView.findViewById(viewId)
                views.put(viewId, view)
            }
            return view as T
        }

    }

    class EmptyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}