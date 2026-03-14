package com.houstd.app.ui.student

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.ApartmentAdapter
import com.houstd.app.ui.shared.ApartmentDetailBottomSheet
import com.houstd.app.ui.shared.ApplyBottomSheet

class StudentSavedFragment : Fragment() {

    private lateinit var repo: AppRepository
    private lateinit var adapter: ApartmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_student_saved, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerSaved)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)

        adapter = ApartmentAdapter(
            variant = ApartmentAdapter.Variant.STUDENT,
            getSavedIds = { repo.getSavedIds() },
            onToggleSave = { apt ->
                repo.toggleSaved(apt.id)
                val saved = repo.getSavedIds()
                val list = repo.getApartments().filter { saved.contains(it.id) }
                adapter.submitList(list)
                tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            },
            onViewDetails = { apt ->
                ApartmentDetailBottomSheet(apt) {
                    ApplyBottomSheet(apt, currentUser, repo) {}
                        .show(childFragmentManager, "apply")
                }.show(childFragmentManager, "detail")
            },
            onApply = { apt ->
                ApplyBottomSheet(apt, currentUser, repo) {}
                    .show(childFragmentManager, "apply")
            }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        loadSaved(tvEmpty)
    }

    private fun loadSaved(tvEmpty: TextView) {
        val saved = repo.getSavedIds()
        val list = repo.getApartments().filter { saved.contains(it.id) }
        adapter.submitList(list)
        tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        view?.let { loadSaved(it.findViewById(R.id.tvEmpty)) }
    }
}
