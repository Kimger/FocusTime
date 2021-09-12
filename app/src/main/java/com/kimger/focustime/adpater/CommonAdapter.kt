package com.kimger.focustime.adpater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kimger.focustime.inflate

/**
 * @author Kimger
 * @email kimger@cloocle.com
 * @date 2019/5/17 11:27
 * @description
 */
public class SingleAdapter<ITEM>(private var items: MutableList<ITEM>,
                          private val layoutResId: Int,
                          initHolder: (Holder, Int) -> Unit,
                          emptyLayoutResId: Int)
    : AbstractAdapter<ITEM>(items, initHolder, emptyLayoutResId) {

    private var itemClick: (ITEM, Int) -> Unit = { _: ITEM, _: Int -> }
    private lateinit var bindHolder: (Holder, ITEM) -> Unit

    constructor(items: MutableList<ITEM>,
                layoutResId: Int,
                initHolder: (RecyclerView.ViewHolder, Int) -> Unit,
                emptyLayoutResId: Int,
                bindHolder: (Holder, ITEM) -> Unit,
                itemClick: (ITEM, Int) -> Unit = { _: ITEM, _: Int -> }) : this(items, layoutResId, initHolder, emptyLayoutResId) {
        this.itemClick = itemClick
        this.bindHolder = bindHolder
    }

    override fun createItemView(parent: ViewGroup, viewType: Int): View {
        var view = parent inflate layoutResId
//        if (view.tag?.toString()?.contains("layout/") == true) {
//            DataBindingUtil.bind<ViewDataBinding>(view)
//        }
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            bindHolder(holder, itemList[position])
        }
    }

    override fun onItemClick(itemView: View, position: Int) {
        itemClick(itemList[position], position)
    }
}


class MultiAdapter<ITEM : ListItemI>(private val items: MutableList<ITEM>,
                                     initHolder: (RecyclerView.ViewHolder, Int) -> Unit)
    : AbstractAdapter<ITEM>(items, initHolder, 0) {

    private var itemClick: (ITEM) -> Unit = {}
    private lateinit var bindHolder: (RecyclerView.ViewHolder, ITEM) -> Unit
    private lateinit var listItems: Array<out ListItem<ITEM>>

    constructor(items: MutableList<ITEM>,
                listItems: Array<out ListItem<ITEM>>,
                initHolder: (RecyclerView.ViewHolder, Int) -> Unit,
                bindHolder: (RecyclerView.ViewHolder, ITEM) -> Unit,
                itemClick: (ITEM) -> Unit = {}) : this(items, initHolder) {
        this.itemClick = itemClick
        this.listItems = listItems
        this.bindHolder = bindHolder
    }


    override fun createItemView(parent: ViewGroup, viewType: Int): View {
        var view = parent inflate getLayoutId(viewType)
//        if (view.tag?.toString()?.contains("layout/") == true) {
//            DataBindingUtil.bind<ViewDataBinding>(view)
//        }
        return view
    }

    private fun getLayoutId(viewType: Int): Int {
        var layoutId = -1
        listItems.forEach {
            if (it.layoutResId == viewType) {
                layoutId = it.layoutResId
                return@forEach
            }
        }
        return layoutId
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindHolder(holder, itemList[position])
    }

    override fun onItemClick(itemView: View, position: Int) {
        itemClick(itemList[position])
    }
}


public fun <ITEM> RecyclerView.setUp(items: MutableList<ITEM>,
                                     layoutResId: Int,
                                     bindHolder: (AbstractAdapter.Holder, ITEM) -> Unit,
                                     itemClick: (ITEM, Int) -> Unit = { _: ITEM, _: Int -> },
                                     emptyLayoutResId: Int = 0,
                                     initHolder: (RecyclerView.ViewHolder) -> Unit = {},
                                     manager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
): AbstractAdapter<ITEM> {
    val singleAdapter by lazy {
        SingleAdapter(items, layoutResId, { holder, _ ->
            initHolder(holder)
        }, emptyLayoutResId, { holder, item ->
            bindHolder(holder, item)
        }, { item, position ->
            itemClick(item, position)
        })
    }
    layoutManager = manager
    adapter = singleAdapter
    return singleAdapter
}

//fun <ITEM> RecyclerView.setUpWithEmptyView(items: MutableList<ITEM>,
//                                           layoutResId: Int,
//                                           emptyLayoutResId: Int = 0,
//                                           bindHolder: (AbstractAdapter.Holder, ITEM) -> Unit,
//                                           itemClick: (ITEM, Int) -> Unit = { _: ITEM, _: Int -> },
//                                           initHolder: (RecyclerView.ViewHolder) -> Unit = {},
//                                           manager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
//): AbstractAdapter<ITEM> {
//    val singleAdapter by lazy {
//        SingleAdapter(items, layoutResId, { holder, _ ->
//            initHolder(holder)
//        }, emptyLayoutResId, { holder, item ->
//            bindHolder(holder, item)
//        }, { item, position ->
//            itemClick(item, position)
//        })
//    }
//    layoutManager = manager
//    adapter = singleAdapter
//    return singleAdapter
//}

/**
 * 适配多布局，用不到暂时注释
 */
//fun <ITEM : ListItemI> RecyclerView.setUP(items: MutableList<ITEM>,
//                                          vararg listItems: ListItem<ITEM>,
//                                          manager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
//): AbstractAdapter<ITEM> {
//
//    val multiAdapter by lazy {
//        MultiAdapter(items, listItems, { holder, viewType ->
//            var listItem: ListItem<ITEM>? = getListItem(listItems, viewType)
//            listItem?.initHolder?.invoke(holder)
//        }, { holder, item ->
//            var listItem: ListItem<ITEM>? = getListItem(listItems, item.getType())
//            listItem?.bindHolder?.invoke(holder, item)
//        }, { item ->
//            var listItem: ListItem<ITEM>? = getListItem(listItems, item.getType())
//            listItem?.itemClick?.invoke(item)
//        })
//    }
//    layoutManager = manager
//    adapter = multiAdapter
//    return multiAdapter
//}

private fun <ITEM : ListItemI> getListItem(listItems: Array<out ListItem<ITEM>>, type: Int): ListItem<ITEM>? {
    var listItem: ListItem<ITEM>? = null
    listItems.forEach {
        if (it.layoutResId == type) {
            listItem = it
            return@forEach
        }
    }
    return listItem
}

class ListItem<ITEM>(val layoutResId: Int,
                     val bindHolder: (holder: RecyclerView.ViewHolder, item: ITEM) -> Unit,
                     val itemClick: (item: ITEM) -> Unit = {},
                     val initHolder: (RecyclerView.ViewHolder) -> Unit = {}
)


interface ListItemI {
    fun getType(): Int
}

class ListItemAdapter<ITEM>(var data: ITEM, private val viewType: Int) : ListItemI {

    override fun getType(): Int {
        return viewType
    }
}