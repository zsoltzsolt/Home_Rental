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
import java.util.*

class DetailsFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var imageList: List<String>

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
                binding.tvDate.text = property.date
                imageList = property.image?.toList() ?: emptyList()

                val adapter = ImageSliderAdapter(imageList)
                viewPager.adapter = adapter
                TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
            }
        }

        return root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

