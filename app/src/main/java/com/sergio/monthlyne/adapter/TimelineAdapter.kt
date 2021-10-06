package com.sergio.monthlyne.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sergio.monthlyne.R
import com.sergio.monthlyne.entity.TimelineInformation

class TimelineAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {
    private var timelineList : List<TimelineInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.set(timelineList[position])
    }

    override fun getItemCount(): Int {
        return timelineList.size
    }

    fun update(timelineList: List<TimelineInformation>) {
        this.timelineList = timelineList
        notifyDataSetChanged()
    }

    inner class TimelineViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        private val timelineDate : TextView = itemView.findViewById(R.id.text_timeline_date)

        fun set(timeline : TimelineInformation){
            timelineDate.text = timeline.timelineDate
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}