package com.del.demoinsta.ui.base.adapter

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 *1) Parent LC
 * // this is for, when parent RV goes into ON STOP, our viewHolder should also go to ON STOP
 * // same for onDestroy.
 *
 *2) Takes Data of List
 * and BIVH
 *
 *    BIVH - T data and BIVM
 *
 *         BIVM - take T Data
 */
@SuppressLint("LogNotTimber")
abstract class BaseAdapter<T : Any, VH : BaseItemViewHolder<T, out BaseItemViewModel<T>>>
    (
    //
    parentLifeCycle: Lifecycle,

    //
    private val dataList: ArrayList<T>

) : RecyclerView.Adapter<VH>() {

    //
    private val TAG = BaseAdapter::class.simpleName + " : ";


    //
    private var recyclerView: RecyclerView? = null

    /**
     * Associating an observer to our parent LC
     * Lol, just read LCCC1
     * either could have implemented the infy or use anony class just like below.
     */
    init {
        Log.i(TAG, "init()")
        //
        parentLifeCycle.addObserver(object : LifecycleObserver {


            // let's hear the callback of parent's onDestroy

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onParentDestroy() {
                Log.d(TAG, "onParentDestroy: () ")
                //
                recyclerView?.run {
                    for (i in 0 until childCount) {
                        //
                        getChildAt(i)?.let {
                            (getChildViewHolder(it) as BaseItemViewHolder<*, *>)
                                .run {
                                    //
                                    onDestroy()
                                    //
                                    viewModel.manualOnCleared()
                                }
                        }
                    }
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onParentStop() {
                Log.d(TAG, "onParentStop: () ")
                //
                recyclerView?.run {
                    for (i in 0 until childCount) {
                        //
                        getChildAt(i)?.let {
                            (getChildViewHolder(it) as BaseItemViewHolder<*, *>).onStop()
                        }
                    }
                }
            }


            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onParentStart() {
                Log.d(TAG, "onParentStart: () ")

                // only for visible items to start
                recyclerView?.run {
                    if (layoutManager is LinearLayoutManager) {
                        val first =
                            (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val last =
                            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        if (first in 0..last)
                            for (i in first..last) {
                                findViewHolderForAdapterPosition(i)?.let {
                                    (it as BaseItemViewHolder<*, *>).onStart()
                                }
                            }
                    }
                }
            }


        })
    }


    ///
    override fun onBindViewHolder(holder: VH, position: Int) {
        //
        holder.bind(dataList[position])
    }

    //
    override fun getItemCount(): Int = dataList.size

    /*
     * Notifying BUVH that view is attached into window and visible.
     */
    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        //
        holder.onStart()
    }

    /*
     * Notifying BIVH that view is removed from window and not visible..
     */
    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        //
        holder.onStop()
    }


    /*
     * Assign rv to this.rv when adapter is attached
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        Log.d(TAG, "onAttachedToRecyclerView: () ")
        super.onAttachedToRecyclerView(recyclerView)
        //

        this.recyclerView = recyclerView
    }

    /*
     * Assign  null to this.rv when adapter is detached
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        Log.d(TAG, "onDetachedFromRecyclerView: () ")
        super.onAttachedToRecyclerView(recyclerView)
        //

        this.recyclerView = null
    }


    /**
     * Method to append data into current list
     *
     * Normally we create new list, instead of that app.
     */
    public fun appendData(dataList: List<T>) {
        Log.d(TAG, "appendData: () ")

        // later
//        for (i in dataList) {
//            this.dataList.add(i)
//        }


        // cool too.. YEAH YEAH, we can explore later.
        val oldCount = itemCount
        this.dataList.addAll(dataList)
        val currentCount = itemCount
        if (oldCount == 0 && currentCount > 0)
            notifyDataSetChanged()
        else if (oldCount > 0 && currentCount > oldCount)
            notifyItemRangeChanged(oldCount - 1, currentCount - oldCount)

    }

    /**
     * Method to clear and addAll
     *
     */
    public fun update(@NonNull dataList: List<T>) {
        Log.d(TAG, "update: () ")

        //
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()

    }

}
