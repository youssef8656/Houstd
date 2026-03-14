package com.houstd.app.ui.student

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.RequestAdapter

class StudentRequestsFragment : Fragment() {

    private lateinit var repo: AppRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_student_requests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerRequests)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)

        val myRequests = repo.getRequests().filter { it.studentId == currentUser.id }
        tvEmpty.visibility = if (myRequests.isEmpty()) View.VISIBLE else View.GONE

        val adapter = RequestAdapter(isOwnerView = false)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        adapter.submitList(myRequests)
    }
}
