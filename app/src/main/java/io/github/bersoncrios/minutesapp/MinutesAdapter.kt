package io.github.bersoncrios.minutesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.bersoncrios.minutesapp.databinding.MinuteRowBinding
import io.github.bersoncrios.minutesapp.models.Minute

class MinutesAdapter(val context: Context, var minuteList:List<Minute>, val onClickListener: OnClickListener)
    : RecyclerView.Adapter<MinutesAdapter.MyViewHolder>() {

    lateinit var binding: MinuteRowBinding

    // View Holder
    class MyViewHolder(var binding: MinuteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(minute: Minute){
            binding.minute  = minute
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = MinuteRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val minute = minuteList[position]
        holder.bind(minute)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(minute)
        }
    }

    class OnClickListener(val clickListener: (minute: Minute) -> Unit) {
        fun onClick(minute: Minute) = clickListener(minute)
    }

    override fun getItemCount(): Int = minuteList.size
}