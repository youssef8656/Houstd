package com.houstd.app.ui.student

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.model.Apartment
import com.houstd.app.data.model.ApartmentStatus
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.ApartmentAdapter
import com.houstd.app.ui.shared.ApartmentDetailBottomSheet
import com.houstd.app.ui.shared.ApplyBottomSheet

class StudentHomeFragment : Fragment() {

    private lateinit var repo: AppRepository
    private lateinit var adapter: ApartmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_student_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerApartments)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        fun getApartments(query: String = "") =
            repo.getApartments()
                .filter { it.status == ApartmentStatus.APPROVED }
                .filter { query.isEmpty() || it.title.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true) }

        adapter = ApartmentAdapter(
            variant = ApartmentAdapter.Variant.STUDENT,
            getSavedIds = { repo.getSavedIds() },
            onToggleSave = { apt -> repo.toggleSaved(apt.id); adapter.notifyDataSetChanged() },
            onViewDetails = { apt ->
                ApartmentDetailBottomSheet(apt) {
                    ApplyBottomSheet(apt, currentUser, repo) { adapter.notifyDataSetChanged() }
                        .show(childFragmentManager, "apply")
                }.show(childFragmentManager, "detail")
            },
            onApply = { apt ->
                ApplyBottomSheet(apt, currentUser, repo) { adapter.notifyDataSetChanged() }
                    .show(childFragmentManager, "apply")
            }
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        adapter.submitList(getApartments())

        etSearch.addTextChangedListener { adapter.submitList(getApartments(it.toString())) }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(repo.getApartments().filter { it.status == ApartmentStatus.APPROVED })
    }
}
