package com.houstd.app.ui.shared

import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.houstd.app.R
import com.houstd.app.data.model.*
import com.houstd.app.data.repository.AppRepository

class ApplyBottomSheet(
    private val apartment: Apartment,
    private val currentUser: User,
    private val repo: AppRepository,
    private val onDone: () -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bottom_sheet_apply, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val alreadyApplied = repo.getRequests()
            .any { it.studentId == currentUser.id && it.apartmentId == apartment.id }

        val layoutForm = view.findViewById<View>(R.id.layoutForm)
        val layoutSuccess = view.findViewById<View>(R.id.layoutSuccess)
        val layoutAlready = view.findViewById<View>(R.id.layoutAlready)
        val etMessage = view.findViewById<EditText>(R.id.etMessage)
        val tvAptTitle = view.findViewById<TextView>(R.id.tvApartmentTitle)
        val btnSend = view.findViewById<Button>(R.id.btnSend)

        tvAptTitle.text = apartment.title

        when {
            alreadyApplied -> {
                layoutForm.visibility = View.GONE
                layoutSuccess.visibility = View.GONE
                layoutAlready.visibility = View.VISIBLE
            }
            else -> {
                layoutForm.visibility = View.VISIBLE
                layoutSuccess.visibility = View.GONE
                layoutAlready.visibility = View.GONE
            }
        }

        btnSend.setOnClickListener {
            val req = ApartmentRequest(
                id = "req-${System.currentTimeMillis()}",
                studentId = currentUser.id,
                studentName = currentUser.name,
                studentEmail = currentUser.email,
                apartmentId = apartment.id,
                apartmentTitle = apartment.title,
                status = RequestStatus.PENDING,
                createdAt = System.currentTimeMillis(),
                message = etMessage.text.toString()
            )
            repo.addRequest(req)
            layoutForm.visibility = View.GONE
            layoutSuccess.visibility = View.VISIBLE
            onDone()
        }
        view.findViewById<ImageButton>(R.id.btnClose).setOnClickListener { dismiss() }
    }
}

class AddApartmentBottomSheet(
    private val owner: User,
    private val onSave: (Apartment) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bottom_sheet_add_apartment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etAddress = view.findViewById<EditText>(R.id.etAddress)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val etBeds = view.findViewById<EditText>(R.id.etBeds)
        val etDesc = view.findViewById<EditText>(R.id.etDescription)
        val etImageUrl = view.findViewById<EditText>(R.id.etImageUrl)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val tvError = view.findViewById<TextView>(R.id.tvError)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val price = etPrice.text.toString().toIntOrNull()
            val beds = etBeds.text.toString().toIntOrNull()
            val desc = etDesc.text.toString().trim()

            if (title.isEmpty() || address.isEmpty() || price == null || beds == null || desc.isEmpty()) {
                tvError.visibility = View.VISIBLE
                tvError.text = "Please fill all required fields"
                return@setOnClickListener
            }

            val apt = Apartment(
                id = "apt-${System.currentTimeMillis()}",
                ownerId = owner.id,
                ownerName = owner.name,
                title = title,
                address = address,
                price = price,
                beds = beds,
                rating = 0.0,
                imageUrl = etImageUrl.text.toString().trim().ifEmpty {
                    "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600&h=400&fit=crop"
                },
                description = desc,
                status = ApartmentStatus.PENDING,
                createdAt = System.currentTimeMillis()
            )
            onSave(apt)
            dismiss()
        }
        view.findViewById<ImageButton>(R.id.btnClose).setOnClickListener { dismiss() }
    }
}
