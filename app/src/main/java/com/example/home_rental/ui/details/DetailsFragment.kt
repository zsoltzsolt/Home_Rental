package com.example.home_rental.ui.details


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.Transition
import com.example.home_rental.ImageSliderAdapter
import com.example.home_rental.Properties
import com.example.home_rental.R
import com.example.home_rental.databinding.FragmentDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.StringBuilder
import java.util.*

class DetailsFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var imageList: List<String>
    private lateinit var money: String
    private var price  = 1.0

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        val bundle = arguments
        if (bundle != null) {
            val property = bundle.getParcelable<Parcelable>("key")
            if (property != null) {
                val prop = property as com.example.home_rental.Properties
                money = property.money
                price = property.price
                binding.tvDate.text = property.date
                binding.tvTitle.text = property.title
                binding.tvPrice.text = property.price.toString() + " " + property.money + "/luna"
                binding.tvCalculatedPrice.text = property.price.toString() + "  " + property.money
                binding.tvType.text = "Tip proprietate: " + property.type
                binding.tvDescription.text = property.description
                binding.tvProprietar.text = "Proprietar: " + property.username
                binding.tvPhone.text = "Telefon: " + property.phone
                binding.tvYear.text = property.year.toString()
                binding.tvSurface.text = property.surface.toString()
                binding.tvRooms.text = property.rooms.toString()
                binding.tvBath.text = property.bath.toString()
                imageList = property.image?.toList() ?: emptyList()

                val adapter = ImageSliderAdapter(imageList)
                viewPager.adapter = adapter
                TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

                val facilitiesTv = binding.facilitati

                val facilities = StringBuilder()

                if (property.parking) {
                    facilities.append("Parcare\n")
                }

                if (property.garage) {
                    facilities.append("Garaj\n")
                }

                if (property.airConditioner) {
                    facilities.append("Aer conditionat\n")
                }

                if (property.garden) {
                    facilities.append("Gradina\n")
                }

                if (property.balcon) {
                    facilities.append("Balcon\n")
                }

                if (property.centrala) {
                    facilities.append("Centrala\n")
                }

                if (property.pool) {
                    facilities.append("Piscina\n")
                }

                if (property.internet) {
                    facilities.append("Internet\n")
                }

                if (property.mobilat) {
                    facilities.append("Mobilat\n")
                }

                facilitiesTv.text = facilities.toString()

            }
        }

        binding.btnCalculatePrice.setOnClickListener {
            binding.tvCalculatedPrice.text = (binding.tieMonths.text.toString().toDouble() * price).toString() + " " + money
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

