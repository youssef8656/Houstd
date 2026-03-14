package com.houstd.app.ui.owner

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.houstd.app.R
import com.houstd.app.data.model.*
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.ApartmentAdapter
import com.houstd.app.ui.shared.AddApartmentBottomSheet

class OwnerListingsFragment : Fragment() {

    private lateinit var repo: AppRepository
    private lateinit var adapter: ApartmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_owner_listings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerApartments)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddApartment)

        adapter = ApartmentAdapter(
            variant = ApartmentAdapter.Variant.OWNER,
            onDelete = { apt ->
                repo.deleteApartment(apt.id)
                loadListings(tvEmpty)
            }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        loadListings(tvEmpty)

        fab.setOnClickListener {
            AddApartmentBottomSheet(currentUser) { newApt ->
                repo.addApartment(newApt)
                loadListings(tvEmpty)
            }.show(childFragmentManager, "add_apt")
        }
    }

    private fun loadListings(tvEmpty: TextView) {
        val currentUser = repo.getCurrentUser()!!
        val list = repo.getApartments().filter { it.ownerId == currentUser.id }
        adapter.submitList(list)
        tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        view?.let { loadListings(it.findViewById(R.id.tvEmpty)) }
    }
}
