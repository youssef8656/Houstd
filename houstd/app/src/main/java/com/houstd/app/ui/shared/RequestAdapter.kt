package com.houstd.app.ui.shared

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.houstd.app.R
import com.houstd.app.data.model.ApartmentRequest
import com.houstd.app.data.model.RequestStatus
import java.text.SimpleDateFormat
import java.util.*

class RequestAdapter(
    private val isOwnerView: Boolean,
    private val onApprove: ((ApartmentRequest) -> Unit)? = null,
    private val onReject: ((ApartmentRequest) -> Unit)? = null,
) : ListAdapter<ApartmentRequest, RequestAdapter.VH>(DIFF) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvApartmentTitle: TextView = view.findViewById(R.id.tvApartmentTitle)
        val tvStudentName: TextView = view.findViewById(R.id.tvStudentName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val layoutActions: View = view.findViewById(R.id.layoutActions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val req = getItem(position)
        val fmt = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        holder.tvApartmentTitle.text = req.apartmentTitle
        holder.tvStudentName.text = if (isOwnerView) "From: ${req.studentName}" else "Your application"
        holder.tvMessage.text = if (req.message.isNotEmpty()) req.message else "No message provided"
        holder.tvDate.text = fmt.format(Date(req.createdAt))

        val (statusText, colorRes) = when (req.status) {
            RequestStatus.APPROVED -> "Approved" to R.color.status_approved
            RequestStatus.REJECTED -> "Rejected" to R.color.status_rejected
            RequestStatus.PENDING -> "Pending" to R.color.status_pending
        }
        holder.tvStatus.text = statusText
        holder.tvStatus.setTextColor(holder.itemView.context.getColor(colorRes))

        holder.layoutActions.visibility =
            if (isOwnerView && req.status == RequestStatus.PENDING) View.VISIBLE else View.GONE

        holder.btnApprove.setOnClickListener { onApprove?.invoke(req) }
        holder.btnReject.setOnClickListener { onReject?.invoke(req) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ApartmentRequest>() {
            override fun areItemsTheSame(a: ApartmentRequest, b: ApartmentRequest) = a.id == b.id
            override fun areContentsTheSame(a: ApartmentRequest, b: ApartmentRequest) = a == b
        }
    }
}
