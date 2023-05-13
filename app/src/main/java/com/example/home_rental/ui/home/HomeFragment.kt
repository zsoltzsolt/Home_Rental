package com.example.home_rental.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home_rental.Properties
import com.example.home_rental.PropertyAdapter
import com.example.home_rental.R
import com.example.home_rental.User
import com.example.home_rental.databinding.FragmentHomeBinding
import com.example.home_rental.ui.details.DetailsFragment
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.time.LocalDate

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var propertiesArrayList : ArrayList<Properties>
    private lateinit var propertyAdapter : PropertyAdapter
    private lateinit var db : DatabaseReference
    private lateinit var storageRef : StorageReference
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storageRef = Firebase.storage.reference

        val onClickListener = OnClickListener { view ->
            val position = view.tag as Int
            val property = propertiesArrayList[position]
            val newFragment = DetailsFragment()


            findNavController().navigate(R.id.detailsFragment)

        }

        db = FirebaseDatabase.getInstance().getReference("properties")


        recyclerView = binding.recycleView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        propertiesArrayList = arrayListOf()

        propertyAdapter = PropertyAdapter(propertiesArrayList, onClickListener)

        recyclerView.adapter = propertyAdapter

        EventChangeListener()

        return root
    }




    private fun EventChangeListener() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                propertiesArrayList.clear()

                for (userSnapshot in dataSnapshot.children) {
                    val userID = userSnapshot.key

                    for (propertySnapshot in userSnapshot.children) {
                        val propertyID = propertySnapshot.key

                        val title = propertySnapshot.child("title").getValue(String::class.java)
                        val type = propertySnapshot.child("type").getValue(String::class.java)
                        val year = propertySnapshot.child("year").getValue(Int::class.java)
                        val judet = propertySnapshot.child("judet").getValue(String::class.java)
                        val city = propertySnapshot.child("city").getValue(String::class.java)
                        val surface = propertySnapshot.child("surface").getValue(Int::class.java)
                        val price = propertySnapshot.child("price").getValue(Double::class.java)
                        val money = propertySnapshot.child("money").getValue(String::class.java)
                        val rooms = propertySnapshot.child("rooms").getValue(Int::class.java)
                        val bath = propertySnapshot.child("bath").getValue(Int::class.java)
                        val parking = propertySnapshot.child("parking").getValue(Boolean::class.java)
                        val garage = propertySnapshot.child("garage").getValue(Boolean::class.java)
                        val airConditioner = propertySnapshot.child("airConditioner").getValue(Boolean::class.java)
                        val garden = propertySnapshot.child("garden").getValue(Boolean::class.java)
                        val balcon = propertySnapshot.child("balcon").getValue(Boolean::class.java)
                        val centrala = propertySnapshot.child("centrala").getValue(Boolean::class.java)
                        val pool = propertySnapshot.child("pool").getValue(Boolean::class.java)
                        val internet = propertySnapshot.child("internet").getValue(Boolean::class.java)
                        val mobilat = propertySnapshot.child("mobilat").getValue(Boolean::class.java)
                        val description = propertySnapshot.child("description").getValue(String::class.java)
                        val phone = propertySnapshot.child("phone").getValue(String::class.java)
                        var first_image_ad = propertySnapshot.child("firstImage").getValue(String::class.java)
                        var date = propertySnapshot.child("date").getValue(String::class.java)

                        val imageUrls = ArrayList<String>()

                        if (first_image_ad != null) {
                            first_image_ad = "$userID/$propertyID/$first_image_ad"
                            val imageRef1 = storageRef.child(first_image_ad)
                            imageRef1.downloadUrl.addOnSuccessListener { uri ->
                                first_image_ad = uri.toString()
                            }
                        }




                        val imageRef = storageRef.child("$userID/$propertyID")
                        imageRef.listAll().addOnSuccessListener { result ->
                            val downloadUrls = ArrayList<String>()

                            // Obține toate URL-urile de descărcare pentru fiecare imagine
                            val downloadTasks = result.items.map { imageFile ->
                                imageFile.downloadUrl.addOnSuccessListener { uri ->
                                    // Uri-ul imaginii descărcate
                                    val imageUrl = uri.toString()

                                    // Adaugă URL-ul imaginii în lista de URL-uri
                                    downloadUrls.add(imageUrl)

                                }
                            }

                            // Așteaptă finalizarea tuturor descărcărilor de imagini
                            Tasks.whenAllSuccess<Void>(downloadTasks).addOnCompleteListener {
                                // Adaugă toate URL-urile descărcate în lista finală de URL-uri
                                imageUrls.addAll(downloadUrls)

                                // Crează un obiect Properties cu valorile extrase și URL-urile imaginilor
                                val property = Properties(propertyID!!, title!!, type!!, year!!, judet!!, city!!, surface!!, price!!, money!!,
                                    rooms!!, bath!!, parking!!, garage!!, airConditioner!!, garden!!, balcon!!, centrala!!, pool!!,
                                    internet!!, mobilat!!, description!!, phone!!, imageUrls?.toTypedArray(), first_image_ad!!, date!!)

                                propertiesArrayList.add(property)
                                propertyAdapter.notifyDataSetChanged() // Actualizează lista și reafișează imaginile
                            }
                        }

                    }
                }

                propertyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Tratează cazurile de eroare
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}