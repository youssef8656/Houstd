package com.houstd.app.ui.admin

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.houstd.app.R
import com.houstd.app.data.model.ApartmentStatus
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.ApartmentAdapter

class AdminListingsFragment : Fragment() {

    private lateinit var repo: AppRepository
    private lateinit var adapter: ApartmentAdapter
    private var currentFilter: ApartmentStatus? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_admin_listings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerApartments)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupFilter)
        val tvCount = view.findViewById<TextView>(R.id.tvListingCount)

        adapter = ApartmentAdapter(
            variant = ApartmentAdapter.Variant.ADMIN,
            onApprove = { apt -> repo.updateApartmentStatus(apt.id, ApartmentStatus.APPROVED); loadList(tvCount) },
            onReject = { apt -> repo.updateApartmentStatus(apt.id, ApartmentStatus.REJECTED); loadList(tvCount) },
            onDelete = { apt -> repo.deleteApartment(apt.id); loadList(tvCount) }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            currentFilter = when (checkedIds.firstOrNull()) {
                R.id.chipPending -> ApartmentStatus.PENDING
                R.id.chipApproved -> ApartmentStatus.APPROVED
                R.id.chipRejected -> ApartmentStatus.REJECTED
                else -> null
            }
            loadList(tvCount)
        }
        loadList(tvCount)
    }

    private fun loadList(tvCount: TextView) {
        val all = repo.getApartments()
        val filtered = if (currentFilter == null) all else all.filter { it.status == currentFilter }
        adapter.submitList(filtered)
        tvCount.text = "${all.size} total listings"
    }
}
