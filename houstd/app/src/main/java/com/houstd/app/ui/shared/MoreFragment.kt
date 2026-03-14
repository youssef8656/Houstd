package com.houstd.app.ui.shared

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.houstd.app.R
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.auth.LoginActivity

class MoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_more, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = AppRepository(requireContext())
        val user = repo.getCurrentUser() ?: return

        view.findViewById<TextView>(R.id.tvUserInitial).text = user.name.first().toString()
        view.findViewById<TextView>(R.id.tvUserName).text = user.name
        view.findViewById<TextView>(R.id.tvUserEmail).text = user.email
        view.findViewById<TextView>(R.id.tvUserRole).text =
            user.role.name.lowercase().replaceFirstChar { it.uppercase() }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            repo.saveCurrentUser(null)
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
