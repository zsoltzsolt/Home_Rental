package com.example.home_rental.ui.gallery


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val tip = arrayOf<String>("Garsoniera", "Apratament", "Casa", "Vila", "Birou", "Spatiu Comercial", "Spatiu industrial")
    private val judete = arrayOf("Alba", "Arad", "Arges", "Bacau", "Bihor", "Bistrita-Nasaud", "Botosani", "Braila", "Brasov", "Bucuresti", "Buzau", "Calarasi", "Caras-Severin", "Cluj", "Constanta", "Covasna", "Dambovita", "Dolj", "Galati", "Giurgiu", "Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi", "Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt", "Olt", "Prahova", "Salaj", "Satu Mare", "Sibiu", "Suceava", "Teleorman", "Timis", "Tulcea", "Valcea", "Vaslui", "Vrancea")
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth
    private var image_count = 0
    private lateinit var propertyID: UUID

    private val binding get() = _binding!!

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        propertyID = UUID.randomUUID()

        binding.tvImageCount.setText("Au fost adaugate $image_count imagini")

        autoCompleteTextView = binding.actType
        adapterItems = ArrayAdapter<String>(requireActivity(), R.layout.list_item, tip)

        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Ai selectat: $selectedItem", Toast.LENGTH_SHORT)
                .show()
        }

        autoCompleteTextView = binding.actJudet
        adapterItems = ArrayAdapter<String>(requireActivity(), R.layout.list_item, judete)

        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Ai selectat: $selectedItem", Toast.LENGTH_SHORT)
                .show()
        }

        val title = RxTextView.textChanges(binding.tieTitle)
            .skipInitialValue()
            .map { title ->
                title.isEmpty()
            }

        val type = RxTextView.textChanges(binding.actType)
            .skipInitialValue()
            .map {
                type ->
                type.isEmpty()
            }

        val judet = RxTextView.textChanges(binding.actJudet)
            .skipInitialValue()
            .map {
                judet ->
                judet.isEmpty()
            }

        val city = RxTextView.textChanges(binding.tieCity)
            .skipInitialValue()
            .map {
                city ->
                city.isEmpty()
            }

        val surface = RxTextView.textChanges(binding.tieSurface)
            .skipInitialValue()
            .map {
                surface ->
                surface.isEmpty()
            }

        val price = RxTextView.textChanges(binding.tiePrice)
            .skipInitialValue()
            .map {
                price ->
                price.isEmpty()
            }

        val rooms = RxTextView.textChanges(binding.tieRooms)
            .skipInitialValue()
            .map {
                rooms ->
                rooms.isEmpty()
            }

        val bath = RxTextView.textChanges(binding.tieBath)
            .skipInitialValue()
            .map{
                bath ->
                bath.isEmpty()
            }

        val description = RxTextView.textChanges(binding.tieDescription)
            .skipInitialValue()
            .map {
                description ->
                description.isEmpty()
            }

        val year = RxTextView.textChanges(binding.tieYear)
            .skipInitialValue()
            .map { year ->
                year.toString().toInt() < 1800 || year.toString().toInt() > Calendar.getInstance().get(Calendar.YEAR)
            }
        year.subscribe {
            showDateIsNotValid(it)
        }

        val phone1 = RxTextView.textChanges(binding.tiePhone)
            .skipInitialValue()
            .map { phone ->
                !phone.matches(Regex("^(?:(?:\\+|00)40|0)(?:7\\d{8}|2\\d{7}|3[0-8]\\d{6})\$"))
            }
       phone1.subscribe{
           showPhoneIsNotValid(it)
       }

        val invalidFields1 = Observable.combineLatest(
            title,
            type,
            judet,
            surface,
            price,
            rooms,
            bath,
            description,
            year,
            {titleInvalid: Boolean, typeInavlid: Boolean, judetInvalid: Boolean, surafaceInvalid: Boolean,priceInvalid: Boolean, roomsInvalid: Boolean, bathInvalid: Boolean , descriptionInvalid: Boolean, yearInvalid: Boolean
                ->  !titleInvalid && !typeInavlid && !judetInvalid && !surafaceInvalid && !priceInvalid && !roomsInvalid && !bathInvalid && !descriptionInvalid && !yearInvalid
            })

        val invalidFields = Observable.combineLatest(
            invalidFields1,
            phone1,
            {filedsInvalid: Boolean, phoneInvalid: Boolean ->
                filedsInvalid && !phoneInvalid
            })

        invalidFields.subscribe { isValid: Boolean ->
            if (isValid) {
                binding.btnPost.isEnabled = true
                binding.btnPost.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            }else{
                binding.btnPost.isEnabled = false
                binding.btnPost.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

        storageRef = Firebase.storage.reference

        binding.btnAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDateIsNotValid(isNotValid : Boolean){
        binding.tieYear.error = if(isNotValid) "An invalid" else null
    }

    private fun showPhoneIsNotValid(isNotValid : Boolean){
        binding.tiePhone.error = if(isNotValid) "Numarul telefon invalid" else null
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

