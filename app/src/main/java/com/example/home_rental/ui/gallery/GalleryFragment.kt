package com.example.home_rental.ui.gallery


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.home_rental.R
import com.example.home_rental.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.UUID

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val tip = arrayOf<String>("Garsoniera", "Apratament", "Casa", "Vila", "Birou", "Spatiu Comercial", "Spatiu industrial")
    private val judete = arrayOf("Alba", "Arad", "Arges", "Bacau", "Bihor", "Bistrita-Nasaud", "Botosani", "Braila", "Brasov", "Bucuresti", "Buzau", "Calarasi", "Caras-Severin", "Cluj", "Constanta", "Covasna", "Dambovita", "Dolj", "Galati", "Giurgiu", "Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi", "Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt", "Olt", "Prahova", "Salaj", "Satu Mare", "Sibiu", "Suceava", "Teleorman", "Timis", "Tulcea", "Valcea", "Vaslui", "Vrancea")
    private val money = arrayOf(" lei", "  €")
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private var image_count = 0
    private lateinit var propertyID: UUID

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        propertyID = UUID.randomUUID();

        binding.tvImageCount.setText("Au fost adaugate $image_count imagini")

        autoCompleteTextView = binding.actType
        adapterItems = ArrayAdapter<String>(requireActivity(), R.layout.list_item, tip)

        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }

        autoCompleteTextView = binding.actJudet
        adapterItems = ArrayAdapter<String>(requireActivity(), R.layout.list_item, judete)

        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }

        autoCompleteTextView = binding.actMoney
        adapterItems = ArrayAdapter<String>(requireActivity(), R.layout.list_item, money)

        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }

        storageRef = Firebase.storage.reference

        binding.btnAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }

        colorCheckBox(binding.cb1)
        colorCheckBox(binding.cb2)
        colorCheckBox(binding.cb3)
        colorCheckBox(binding.cb4)
        colorCheckBox(binding.cb5)
        colorCheckBox(binding.cb6)
        colorCheckBox(binding.cb7)
        colorCheckBox(binding.cb8)
        colorCheckBox(binding.cb9)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                val imageUri: Uri? = result.data?.data
                val sd = getFileName(requireContext(), imageUri!!)
                val uid = auth.currentUser?.uid
                val uploadTask = storageRef.child("$uid/$propertyID/$sd").putFile(imageUri)
                ++image_count
                binding.tvImageCount.setText("Au fost adaugate $image_count imagini")
                /*uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storageRef.child("upload/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this)
                            .load(it)
                            .into(binding.ivImage)

                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }*/
            }
        }

    @SuppressLint("ResourceType")
    private fun colorCheckBox(checkBox: android.widget.CheckBox){
        val colors = resources.getColorStateList(R.drawable.checkbox_purple_grey_selector)
        checkBox.buttonTintList = colors
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox.setTextColor(resources.getColor(R.color.purple_500))
            } else {
                checkBox.setTextColor(resources.getColor(R.color.grey))
            }
        }
    }

    private fun getFileName(context: android.content.Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return uri.path?.substring(uri.path!!.lastIndexOf("/") + 1)
    }


}
