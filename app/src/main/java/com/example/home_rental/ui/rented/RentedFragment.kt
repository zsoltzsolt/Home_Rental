package com.example.home_rental.ui.rented

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home_rental.Properties
import com.example.home_rental.PropertyAdapter
import com.example.home_rental.R
import com.example.home_rental.databinding.FragmentHomeBinding
import com.example.home_rental.databinding.FragmentSlideshowBinding
import com.example.home_rental.ui.details.DetailsFragment
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.Date

class RentedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var propertiesArrayList : ArrayList<Properties>
    private lateinit var propertyAdapter : PropertyAdapter
    private lateinit var db : DatabaseReference
    private lateinit var storageRef : StorageReference
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }








    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}