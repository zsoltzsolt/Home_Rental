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
        val layoutId = if (viewType == 0) R.layout.list_item else R.layout.list_item_right
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PropertyAdapter.MyViewHolder, position: Int) {

        val property: Properties = propertiesList[position]
        holder.title.text = property.title
        holder.price.text = "Pret: " + property.price + " " + property.money + "/luna"
        holder.location.text = property.city + "," + property.judet
        holder.phone.text = "Telefon: " + property.phone

    }

    override fun getItemViewType(position: Int): Int {
        // Returnează 0 pentru pozițiile pare și 1 pentru pozițiile impare
        return position % 2
    }

    override fun getItemCount(): Int {
       return propertiesList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.anouncement_title)
        val price : TextView = itemView.findViewById(R.id.anouncement_price)
        val location: TextView = itemView.findViewById(R.id.anouncement_location)
        val phone : TextView = itemView.findViewById(R.id.anouncement_phone)
    }

}