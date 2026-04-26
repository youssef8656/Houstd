package com.example.houstd.ui.Users.owner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.houstd.ApartmentAdapter
import com.example.houstd.R

class OwnerApartmentDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_owner_apartment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.apartmentRecycler)

        val sampleList = listOf(
            "Hotel Tower",
            "Sky View Apartment",
            "Downtown Residence",
            "City Lights Flat",
            "Hotel Tower",
            "Sky View Apartment",
            "Downtown Residence",
            "City Lights Flat",
            "Hotel Tower",
            "Sky View Apartment",
            "Downtown Residence",
            "City Lights Flat",
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ApartmentAdapter(sampleList)
    }
}