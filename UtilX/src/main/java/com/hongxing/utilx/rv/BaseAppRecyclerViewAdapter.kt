package com.hongxing.utilx.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import java.lang.reflect.ParameterizedType

abstract class BaseAppRecyclerViewAdapter<VB : ViewBinding, T>(context: Context?) :
    RecyclerArrayAdapter<T>(context) {
    lateinit var binding: VB

    protected var myItemClickListeners: MyItemClickListener? = null

    override fun getAllData(): ArrayList<T> {
        return mObjects as ArrayList<T>
    }

    fun setOnItemClickListener(listener: MyItemClickListener?) {
        super.setOnItemClickListener { position ->
            if (myItemClickListeners != null) {
                myItemClickListeners!!.onItemClick(binding.root, position)
            }
        }
        myItemClickListeners = listener
    }


    /**
     * 删除某一位置的元素
     *秒 分 时 周 月
     * * 14 12 * *
     * @param position
     */
    fun removeItem(position: Int) {
//        if (mObjects != null) {
//            mObjects.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, mObjects.size)
//            notifyDataSetChanged()
//        }
        super.remove(position)
    }

    /**
     * 绑定数据
     *
     * @param holder   具体的viewHolder
     * @param position 对应的索引
     */
    abstract fun bindData(
        holder: BaseAppViewHolder<VB>,
        position: Int
    )

    override fun OnBindViewHolder(holder: BaseViewHolder<*>?, position: Int) {
        super.OnBindViewHolder(holder, position)
        bindData(holder as BaseAppViewHolder<VB>, position)
    }

    open fun onItemMove(fromPosition: Int, toPosition: Int) {}

    override fun OnCreateViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): BaseAppViewHolder<*>? {
        return BaseAppViewHolder<VB>(
            getViewBinding(
                viewType,
                parent!!
            ), myItemClickListeners
        )
    }

    /**
     * 获取子item
     *
     * @return
     */
    fun getViewBinding(viewType: Int, parent: ViewGroup): VB {
        val superClass = javaClass.genericSuperclass
        val clazz = (superClass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        binding = method.invoke(null, LayoutInflater.from(context), parent, false) as VB
        return binding
    }


    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    class BaseAppViewHolder<VB : ViewBinding?>(
        var viewBinding: VB,
        private val mListener: MyItemClickListener?
    ) :
        BaseViewHolder<Any?>(viewBinding!!.root) {
    }
    interface MyItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}