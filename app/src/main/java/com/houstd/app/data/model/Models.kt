package com.houstd.app.data.model

import java.io.Serializable

enum class Role { STUDENT, OWNER, ADMIN }
enum class ApartmentStatus { APPROVED, PENDING, REJECTED }
enum class RequestStatus { PENDING, APPROVED, REJECTED }

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: Role
) : Serializable

data class Apartment(
    val id: String,
    val ownerId: String,
    val ownerName: String,
    val title: String,
    val address: String,
    val price: Int,
    val beds: Int,
    val rating: Double,
    val imageUrl: String,
    val description: String,
    val status: ApartmentStatus,
    val createdAt: Long
) : Serializable

data class ApartmentRequest(
    val id: String,
    val studentId: String,
    val studentName: String,
    val studentEmail: String,
    val apartmentId: String,
    val apartmentTitle: String,
    val status: RequestStatus,
    val createdAt: Long,
    val message: String = ""
) : Serializable
