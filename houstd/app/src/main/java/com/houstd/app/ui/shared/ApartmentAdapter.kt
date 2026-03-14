package com.houstd.app.ui.shared

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.houstd.app.R
import com.houstd.app.data.model.Apartment
import com.houstd.app.data.model.ApartmentStatus

class ApartmentAdapter(
    private val variant: Variant,
    private val getSavedIds: (() -> Set<String>)? = null,
    private val onToggleSave: ((Apartment) -> Unit)? = null,
    private val onViewDetails: ((Apartment) -> Unit)? = null,
    private val onApply: ((Apartment) -> Unit)? = null,
    private val onApprove: ((Apartment) -> Unit)? = null,
    private val onReject: ((Apartment) -> Unit)? = null,
    private val onDelete: ((Apartment) -> Unit)? = null,
) : ListAdapter<Apartment, ApartmentAdapter.VH>(DIFF) {

    enum class Variant { STUDENT, OWNER, ADMIN }

    inner class VH(val view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivApartmentImage)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvBeds: TextView = view.findViewById(R.id.tvBeds)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnSave: ImageButton = view.findViewById(R.id.btnSave)
        val btnDetails: Button = view.findViewById(R.id.btnDetails)
        val btnApply: Button = view.findViewById(R.id.btnApply)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val layoutStudentActions: View = view.findViewById(R.id.layoutStudentActions)
        val layoutAdminActions: View = view.findViewById(R.id.layoutAdminActions)
        val layoutOwnerActions: View = view.findViewById(R.id.layoutOwnerActions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_apartment, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val apt = getItem(position)

        Glide.with(holder.ivImage.context)
            .load(apt.imageUrl)
            .placeholder(R.drawable.ic_apartment_placeholder)
            .centerCrop()
            .into(holder.ivImage)

        holder.tvTitle.text = apt.title
        holder.tvAddress.text = apt.address
        holder.tvPrice.text = "$${apt.price}/mo"
        holder.tvBeds.text = "${apt.beds} ${if (apt.beds == 1) "Bed" else "Beds"}"
        holder.tvRating.text = apt.rating.toString()

        val (statusText, statusColor) = when (apt.status) {
            ApartmentStatus.APPROVED -> "Approved" to R.color.status_approved
            ApartmentStatus.REJECTED -> "Rejected" to R.color.status_rejected
            ApartmentStatus.PENDING -> "Pending" to R.color.status_pending
        }
        holder.tvStatus.text = statusText
        holder.tvStatus.setTextColor(holder.view.context.getColor(statusColor))

        // Show/hide action groups
        holder.layoutStudentActions.visibility = if (variant == Variant.STUDENT) View.VISIBLE else View.GONE
        holder.layoutAdminActions.visibility = if (variant == Variant.ADMIN && apt.status == ApartmentStatus.PENDING) View.VISIBLE else View.GONE
        holder.layoutOwnerActions.visibility = if (variant == Variant.OWNER) View.VISIBLE else View.GONE
        holder.btnSave.visibility = if (variant == Variant.STUDENT) View.VISIBLE else View.GONE

        // Save button
        if (variant == Variant.STUDENT) {
            val isSaved = getSavedIds?.invoke()?.contains(apt.id) == true
            holder.btnSave.setImageResource(if (isSaved) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
            holder.btnSave.setOnClickListener { onToggleSave?.invoke(apt) }
        }

        holder.btnDetails.setOnClickListener { onViewDetails?.invoke(apt) }
        holder.btnApply.setOnClickListener { onApply?.invoke(apt) }
        holder.btnApprove.setOnClickListener { onApprove?.invoke(apt) }
        holder.btnReject.setOnClickListener { onReject?.invoke(apt) }
        holder.btnDelete.setOnClickListener { onDelete?.invoke(apt) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Apartment>() {
            override fun areItemsTheSame(a: Apartment, b: Apartment) = a.id == b.id
            override fun areContentsTheSame(a: Apartment, b: Apartment) = a == b
        }
    }
}
