package com.example.home_rental.ui.payments

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.home_rental.R
import com.example.home_rental.databinding.FragmentDetailsBinding
import com.example.home_rental.databinding.FragmentPaymentsBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.regex.Pattern
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class Payments : Fragment() {

    private lateinit var tie_cardNum: TextInputEditText
    private lateinit var tie_cardName: TextInputEditText
    private lateinit var tie_expirare: TextInputEditText
    private lateinit var tie_cvv: TextInputEditText
    private var months = 1

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId1 = currentUser?.uid // ID-ul utilizatorului curent


    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private fun updateDates(months: Int) {
        val currentDate = Date()  // Data curentă

        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, months)

        val newDate = calendar.time

        val bundle = arguments
        if (bundle != null) {
            val det = bundle.getParcelable<Parcelable>("key1")
            if (det != null) {
                val prop = det as com.example.home_rental.PaymentDetails
                val propertyId = prop.propertyId // ID-ul proprietății

                val userId = prop.ownerID
                val calendart = Calendar.getInstance()
                calendar.add(Calendar.MONTH, months)
                val cal = calendar.time

                val database = FirebaseDatabase.getInstance()
                val propertiesRef = database.getReference("properties")
                val userPropertiesRef = propertiesRef.child(userId)
                val propertyRef = userPropertiesRef.child(propertyId)

                val propertyUpdates = HashMap<String, Any>()
                propertyUpdates["dateStart"] = Calendar.getInstance().time
                propertyUpdates["dateStop"] = cal
                propertyUpdates["clientID"] = userId1.toString()
                propertyRef.updateChildren(propertyUpdates)
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tie_cardNum = binding.tieCardNum
        tie_cardName = binding.tieCardName
        tie_expirare = binding.tieExpirare
        tie_cvv = binding.tieCvv

        val bundle = arguments
        if (bundle != null) {
            val det = bundle.getParcelable<Parcelable>("key1")
            if (det != null) {
                val prop = det as com.example.home_rental.PaymentDetails
                binding.tvTitle.text = "Titlu anunt: " + prop.title
                binding.tvMonths.text = "Numar luni: " + prop.months.toString()
                binding.tvPrice.text = "Pret: " + prop.price
                months = prop.months
            }
        }

        val cardNum = RxTextView.textChanges(tie_cardNum)
            .skipInitialValue()
            .map { cardNum ->
                !isCardNumberValid(cardNum.toString())
            }

        val cardName = RxTextView.textChanges(tie_cardName)
            .skipInitialValue()
            .map { cardName ->
                !isCardHolderValid(cardName.toString())
            }

        val expirationDate = RxTextView.textChanges(tie_expirare)
            .skipInitialValue()
            .map { expirationDate ->
                !isExpirationDateValid(expirationDate.toString())
            }

        val cvv = RxTextView.textChanges(tie_cvv)
            .skipInitialValue()
            .map { cvv ->
                !isCVVValid(cvv.toString())
            }

        val invalidFields = Observable.combineLatest(
            cardNum,
            cardName,
            expirationDate,
            cvv,
            { cardNumInvalid: Boolean, cardNameInvalid: Boolean, expirationDateInvalid: Boolean, cvvInvalid: Boolean ->
                !cardNumInvalid && !cardNameInvalid && !expirationDateInvalid && !cvvInvalid
            }
        )

        invalidFields.subscribe { isValid ->
            if (isValid) {
                binding.btnPlateste.isEnabled = true
                binding.btnPlateste.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
            } else {
                binding.btnPlateste.isEnabled = false
                binding.btnPlateste.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }

        invalidFields.subscribe { isValid ->
            if (isValid) {
                binding.btnPlateste.isEnabled = true
                binding.btnPlateste.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.purple_500)
            } else {
                binding.btnPlateste.isEnabled = false
                binding.btnPlateste.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey)
            }
        }
        // Afișarea avertizărilor
        cardNum.subscribe { isValid ->
            if (isValid) {
                binding.tieCardNum.error = "Numărul cardului nu este valid"
            } else {
                binding.tieCardNum.error = null
            }
        }

        cardName.subscribe { isValid ->
            if (isValid) {
                binding.tieCardName.error = "Numele titularului de card nu este valid"
            } else {
                binding.tieCardName.error = null
            }
        }

        expirationDate.subscribe { isValid ->
            if (isValid) {
                binding.tieExpirare.error = "Data de expirare nu este validă"
            } else {
                binding.tieExpirare.error = null
            }
        }

        cvv.subscribe { isValid ->
            if (isValid) {
                binding.tieCvv.error = "CVV-ul nu este valid"
            } else {
                binding.tieCvv.error = null
            }
        }

        binding.btnPlateste.setOnClickListener {
            updateDates(months)
            findNavController().navigate(R.id.nav_home)
        }

        return root
    }

    private fun isCardNumberValid(cardNumber: String): Boolean {
        val pattern = Pattern.compile("^\\d{16}$")
        return pattern.matcher(cardNumber).matches()
    }

    private fun isCardHolderValid(cardHolder: String): Boolean {
        return cardHolder.length <= 50 && cardHolder.length > 0
    }

    private fun isExpirationDateValid(expirationDate: String): Boolean {
        val pattern = Pattern.compile("^(0[1-9]|1[0-2])/((20)\\d\\d)$")
        return pattern.matcher(expirationDate).matches()
    }

    private fun isCVVValid(cvv: String): Boolean {
        val pattern = Pattern.compile("^\\d{3}$")
        return pattern.matcher(cvv).matches()
    }




}