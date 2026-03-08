package com.houstd.app.ui.admin

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.model.ApartmentStatus
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.ApartmentAdapter

class AdminDashboardFragment : Fragment() {

    private lateinit var repo: AppRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_admin_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!
        val apts = repo.getApartments()
        val users = repo.getUsers()
        val requests = repo.getRequests()

        view.findViewById<TextView>(R.id.tvWelcome).text = "Hello, ${currentUser.name.split(" ").first()} 👋"
        view.findViewById<TextView>(R.id.tvTotalListings).text = apts.size.toString()
        view.findViewById<TextView>(R.id.tvPendingCount).text = apts.count { it.status == ApartmentStatus.PENDING }.toString()
        view.findViewById<TextView>(R.id.tvApprovedCount).text = apts.count { it.status == ApartmentStatus.APPROVED }.toString()
        view.findViewById<TextView>(R.id.tvRejectedCount).text = apts.count { it.status == ApartmentStatus.REJECTED }.toString()
        view.findViewById<TextView>(R.id.tvStudentsCount).text = users.count { it.role.name == "STUDENT" }.toString()
        view.findViewById<TextView>(R.id.tvOwnersCount).text = users.count { it.role.name == "OWNER" }.toString()
        view.findViewById<TextView>(R.id.tvRequestsCount).text = requests.size.toString()

        val pendingApts = apts.filter { it.status == ApartmentStatus.PENDING }
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPending)
        val tvNoPending = view.findViewById<TextView>(R.id.tvNoPending)
        tvNoPending.visibility = if (pendingApts.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility = if (pendingApts.isEmpty()) View.GONE else View.VISIBLE

        val adapter = ApartmentAdapter(
            variant = ApartmentAdapter.Variant.ADMIN,
            onApprove = { apt -> repo.updateApartmentStatus(apt.id, ApartmentStatus.APPROVED); onResume() },
            onReject = { apt -> repo.updateApartmentStatus(apt.id, ApartmentStatus.REJECTED); onResume() }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        adapter.submitList(pendingApts)
    }
}
