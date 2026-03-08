package com.houstd.app.ui.shared

import android.os.Bundle
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.houstd.app.R
import com.houstd.app.data.model.Apartment

class ApartmentDetailBottomSheet(
    private val apartment: Apartment,
    private val onApply: () -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.bottom_sheet_apartment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(this).load(apartment.imageUrl)
            .centerCrop().placeholder(R.drawable.ic_apartment_placeholder)
            .into(view.findViewById(R.id.ivImage))

        view.findViewById<TextView>(R.id.tvTitle).text = apartment.title
        view.findViewById<TextView>(R.id.tvAddress).text = apartment.address
        view.findViewById<TextView>(R.id.tvPrice).text = "$${apartment.price}"
        view.findViewById<TextView>(R.id.tvBeds).text = "${apartment.beds} ${if (apartment.beds == 1) "Bedroom" else "Bedrooms"}"
        view.findViewById<TextView>(R.id.tvRating).text = apartment.rating.toString()
        view.findViewById<TextView>(R.id.tvOwner).text = "Listed by ${apartment.ownerName}"
        view.findViewById<TextView>(R.id.tvDescription).text = apartment.description

        view.findViewById<Button>(R.id.btnApply).setOnClickListener {
            dismiss()
            onApply()
        }
        view.findViewById<ImageButton>(R.id.btnClose).setOnClickListener { dismiss() }
    }
}
