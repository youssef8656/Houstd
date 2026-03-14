package com.houstd.app.ui.owner

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.model.RequestStatus
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.RequestAdapter

class OwnerRequestsFragment : Fragment() {

    private lateinit var repo: AppRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_owner_requests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerRequests)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmpty)

        val ownerAptIds = repo.getApartments()
            .filter { it.ownerId == currentUser.id }
            .map { it.id }.toSet()
        val requests = repo.getRequests().filter { it.apartmentId in ownerAptIds }
        tvEmpty.visibility = if (requests.isEmpty()) View.VISIBLE else View.GONE

        val adapter = RequestAdapter(
            isOwnerView = true,
            onApprove = { req ->
                repo.updateRequestStatus(req.id, RequestStatus.APPROVED)
            },
            onReject = { req ->
                repo.updateRequestStatus(req.id, RequestStatus.REJECTED)
            }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        adapter.submitList(requests)
    }
}
