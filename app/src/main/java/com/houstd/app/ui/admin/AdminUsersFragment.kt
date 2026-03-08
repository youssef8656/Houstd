package com.houstd.app.ui.admin

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.shared.UserAdapter

class AdminUsersFragment : Fragment() {

    private lateinit var repo: AppRepository
    private lateinit var adapter: UserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_admin_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        repo = AppRepository(requireContext())
        val currentUser = repo.getCurrentUser()!!

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerUsers)
        val tvCount = view.findViewById<TextView>(R.id.tvUserCount)

        adapter = UserAdapter(currentUserId = currentUser.id) { userId ->
            repo.deleteUser(userId)

            val updated = repo.getUsers()
            tvCount.text = "${updated.size} registered users"

            adapter.submitList(updated)
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val users = repo.getUsers()
        tvCount.text = "${users.size} registered users"
        adapter.submitList(users)
    }
}