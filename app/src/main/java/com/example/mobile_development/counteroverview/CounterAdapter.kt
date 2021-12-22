package com.example.mobile_development.counteroverview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_development.R
import com.example.mobile_development.database.Counter
import com.example.mobile_development.databinding.ListItemCounterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class CounterAdapter(val clickItemListener: CounterItemListener,
                     val clickIncrementButtonListener: CounterButtonListener,
                     val clickDecrementButtonListener: CounterButtonListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(CounterDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Counter>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.CounterItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val counterItem = getItem(position) as DataItem.CounterItem
                holder.bind(counterItem.counter, clickItemListener, clickIncrementButtonListener, clickDecrementButtonListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CounterItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(val binding: ListItemCounterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Counter, clickItemListener: CounterItemListener, clickIncrementButtonListener: CounterButtonListener, clickDecrementButtonListener: CounterButtonListener) {
            binding.counter = item
            binding.clickItemListener = clickItemListener
            binding.clickIncrementButtonListener = clickIncrementButtonListener
            binding.clickDecrementButtonListener = clickDecrementButtonListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCounterBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}


class CounterDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class CounterItemListener(val clickListener: (counterId: Long) -> Boolean) {
    fun onLongClick(counter: Counter) = clickListener(counter.counterId)
}

class CounterButtonListener(val clickListener: (counterId: Long) -> Unit) {
    fun onButtonClick(counter: Counter) = clickListener(counter.counterId)
}

sealed class DataItem {

    abstract val id: Long

    data class CounterItem(val counter: Counter): DataItem() {
        override val id = counter.counterId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
}