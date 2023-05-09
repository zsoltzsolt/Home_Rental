package com.example.home_rental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PropertyAdapter(private val propertiesList : ArrayList<Properties>) : RecyclerView.Adapter<PropertyAdapter.MyViewHolder> (){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PropertyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PropertyAdapter.MyViewHolder, position: Int) {

        val property: Properties = propertiesList[position]
        holder.title.text = property.title
        holder.price.text = "Pret: " + property.price
        holder.location.text = property.city + "," + property.judet

    }

    override fun getItemCount(): Int {
       return propertiesList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.anouncement_title)
        val price : TextView = itemView.findViewById(R.id.anouncement_price)
        val location: TextView = itemView.findViewById(R.id.anouncement_location)
    }

}