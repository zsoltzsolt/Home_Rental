package com.example.home_rental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class PropertyAdapter(private val propertiesList : ArrayList<Properties>, private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<PropertyAdapter.MyViewHolder> (){

    /*private val imageUrlsList: ArrayList<String> = ArrayList()

    fun setImageUrls(imageUrls: List<String>) {
        imageUrlsList.clear()
        imageUrlsList.addAll(imageUrls)
        notifyDataSetChanged()
    }*/


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PropertyAdapter.MyViewHolder {
        val layoutId = if (viewType == 0) R.layout.list_item else R.layout.list_item_right
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val viewHolder = MyViewHolder(itemView)

        viewHolder.itemView.setOnClickListener(onClickListener)

        return viewHolder
    }

    override fun onBindViewHolder(holder: PropertyAdapter.MyViewHolder, position: Int) {

        val property: Properties = propertiesList[position]
        holder.title.text = property.title
        holder.price.text = "Pret: " + property.price + " " + property.money + "/luna"
        holder.location.text = property.city + "," + property.judet
        holder.phone.text = "Telefon: " + property.phone

        holder.itemView.tag = position
        val imageUrlsList = ArrayList<String>()
        imageUrlsList.addAll(property.image!!)

        val first = property.firstImage


        if(first.isNotEmpty()){
            Picasso.get().load(first).into(holder.image)
        }

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
        val image : ImageView = itemView.findViewById(R.id.image1)
    }

}