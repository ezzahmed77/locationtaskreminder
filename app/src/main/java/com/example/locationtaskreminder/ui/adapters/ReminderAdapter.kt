package com.example.locationtaskreminder.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtaskreminder.R
import com.example.locationtaskreminder.data.model.Reminder

class ReminderAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    private val reminders = mutableListOf<Reminder>()

    fun clearData() {
        reminders.clear()
        notifyDataSetChanged()
    }

    fun addData(data: List<Reminder>) {
        reminders.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder = reminders[position]
        // Set the click listener on the item view
        holder.itemView.setOnClickListener {
            onItemClick(reminder.id)
        }
        holder.bind(reminder)
    }

    override fun getItemCount(): Int {
        return reminders.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(reminder: Reminder) {
            titleTextView.text = reminder.title
            descriptionTextView.text = reminder.description
        }
    }
}