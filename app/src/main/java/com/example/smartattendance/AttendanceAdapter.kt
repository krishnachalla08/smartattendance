package com.example.smartattendance

import android.provider.CalendarContract.Attendees
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartattendance.models.attendees

class AttendanceAdapter(var attendens: ArrayList<attendees>): RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.attendees,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: attendees = attendens[position]
        holder.rn.text = user.rollnumber


    }

    override fun getItemCount(): Int {
        return attendens.size
    }
    class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView) {
        val rn :TextView= itemView.findViewById(R.id.tvrn)
        //val st :TextView= itemView.findViewById(R.id.tvst)
    }
}