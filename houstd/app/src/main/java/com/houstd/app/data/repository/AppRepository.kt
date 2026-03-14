package com.houstd.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.houstd.app.data.model.*

class AppRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("houstd_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        val SEED_USERS = listOf(
            User("admin-1", "Admin User", "admin@houstd.com", "admin123", Role.ADMIN),
            User("owner-1", "Ahmed Al-Rashid", "ahmed@houstd.com", "owner123", Role.OWNER),
            User("owner-2", "Sara Mansour", "sara@houstd.com", "owner123", Role.OWNER),
            User("student-1", "Youssef Khalil", "youssef@houstd.com", "student123", Role.STUDENT),
            User("student-2", "Nour Hassan", "nour@houstd.com", "student123", Role.STUDENT),
        )

        val SEED_APARTMENTS = listOf(
            Apartment("apt-1","owner-1","Ahmed Al-Rashid","Modern Studio Near Campus","123 University Ave, Downtown",450,1,4.5,"https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600&h=400&fit=crop","A cozy modern studio apartment just 5 minutes walk from the university campus. Fully furnished with high-speed internet, air conditioning, and a small kitchen.",ApartmentStatus.APPROVED, System.currentTimeMillis() - 864000000L),
            Apartment("apt-2","owner-1","Ahmed Al-Rashid","Spacious 2BR Student Flat","45 College Road, West Side",700,2,4.2,"https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=600&h=400&fit=crop","Perfect for sharing! Two separate bedrooms with a shared living room and kitchen. Close to public transportation and grocery stores.",ApartmentStatus.APPROVED, System.currentTimeMillis() - 691200000L),
            Apartment("apt-3","owner-2","Sara Mansour","Luxury Penthouse Suite","78 Park Lane, City Center",1200,3,4.8,"https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=600&h=400&fit=crop","Premium penthouse with stunning city views. Three bedrooms, two bathrooms, fully equipped kitchen, and a private terrace.",ApartmentStatus.APPROVED, System.currentTimeMillis() - 432000000L),
            Apartment("apt-4","owner-2","Sara Mansour","Budget Room in Shared House","12 Elm Street, North Quarter",280,1,3.9,"https://images.unsplash.com/photo-1484154218962-a197022b5858?w=600&h=400&fit=crop","Affordable private room in a shared house with 3 other students. Shared kitchen, bathroom, and living area. All bills included.",ApartmentStatus.PENDING, System.currentTimeMillis() - 259200000L),
            Apartment("apt-5","owner-1","Ahmed Al-Rashid","Renovated Apartment with Balcony","99 Sunset Boulevard, East End",550,2,4.4,"https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=600&h=400&fit=crop","Recently renovated apartment with a beautiful balcony. Two bedrooms, modern bathroom, and a fully equipped kitchen. Quiet neighborhood.",ApartmentStatus.PENDING, System.currentTimeMillis() - 86400000L),
            Apartment("apt-6","owner-2","Sara Mansour","Cozy Loft Near Library","33 Scholar Way, Academic District",380,1,4.1,"https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=600&h=400&fit=crop","Charming loft-style apartment right next to the university library. Perfect for focused students who love a quiet environment.",ApartmentStatus.APPROVED, System.currentTimeMillis() - 604800000L),
        )

        val SEED_REQUESTS = listOf(
            ApartmentRequest("req-1","student-1","Youssef Khalil","youssef@houstd.com","apt-1","Modern Studio Near Campus", RequestStatus.PENDING, System.currentTimeMillis() - 172800000L,"I am a 3rd year CS student looking for accommodation near campus."),
            ApartmentRequest("req-2","student-2","Nour Hassan","nour@houstd.com","apt-2","Spacious 2BR Student Flat", RequestStatus.APPROVED, System.currentTimeMillis() - 345600000L,"My friend and I would love to share this apartment for the upcoming semester."),
        )
    }

    // ─── USERS ─────────────────────────────────────────────
    fun getUsers(): List<User> {
        val json = prefs.getString("users", null) ?: return SEED_USERS.also { saveUsers(it) }
        return gson.fromJson(json, object : TypeToken<List<User>>() {}.type)
    }

    fun saveUsers(users: List<User>) {
        prefs.edit().putString("users", gson.toJson(users)).apply()
    }

    fun getCurrentUser(): User? {
        val json = prefs.getString("current_user", null) ?: return null
        return gson.fromJson(json, User::class.java)
    }

    fun saveCurrentUser(user: User?) {
        if (user == null) prefs.edit().remove("current_user").apply()
        else prefs.edit().putString("current_user", gson.toJson(user)).apply()
    }

    fun login(email: String, password: String, role: Role): Result<User> {
        val users = getUsers()
        val user = users.find { it.email.lowercase() == email.lowercase() && it.password == password }
            ?: return Result.failure(Exception("Invalid email or password"))
        if (user.role != role) return Result.failure(Exception("This account is registered as ${user.role.name.lowercase()}, not ${role.name.lowercase()}"))
        saveCurrentUser(user)
        return Result.success(user)
    }

    fun register(name: String, email: String, password: String, role: Role): Result<User> {
        val users = getUsers().toMutableList()
        if (users.any { it.email.lowercase() == email.lowercase() })
            return Result.failure(Exception("Email already registered"))
        val newUser = User("${role.name.lowercase()}-${System.currentTimeMillis()}", name, email, password, role)
        users.add(newUser)
        saveUsers(users)
        saveCurrentUser(newUser)
        return Result.success(newUser)
    }

    fun deleteUser(id: String) {
        val updated = getUsers().filter { it.id != id }
        saveUsers(updated)
    }

    // ─── APARTMENTS ────────────────────────────────────────
    fun getApartments(): List<Apartment> {
        val json = prefs.getString("apartments", null) ?: return SEED_APARTMENTS.also { saveApartments(it) }
        return gson.fromJson(json, object : TypeToken<List<Apartment>>() {}.type)
    }

    fun saveApartments(list: List<Apartment>) {
        prefs.edit().putString("apartments", gson.toJson(list)).apply()
    }

    fun addApartment(apt: Apartment) {
        val list = getApartments().toMutableList()
        list.add(0, apt)
        saveApartments(list)
    }

    fun updateApartmentStatus(id: String, status: ApartmentStatus) {
        val list = getApartments().map { if (it.id == id) it.copy(status = status) else it }
        saveApartments(list)
    }

    fun deleteApartment(id: String) {
        saveApartments(getApartments().filter { it.id != id })
    }

    // ─── REQUESTS ──────────────────────────────────────────
    fun getRequests(): List<ApartmentRequest> {
        val json = prefs.getString("requests", null) ?: return SEED_REQUESTS.also { saveRequests(it) }
        return gson.fromJson(json, object : TypeToken<List<ApartmentRequest>>() {}.type)
    }

    fun saveRequests(list: List<ApartmentRequest>) {
        prefs.edit().putString("requests", gson.toJson(list)).apply()
    }

    fun addRequest(req: ApartmentRequest) {
        val list = getRequests().toMutableList()
        list.add(0, req)
        saveRequests(list)
    }

    fun updateRequestStatus(id: String, status: RequestStatus) {
        val list = getRequests().map { if (it.id == id) it.copy(status = status) else it }
        saveRequests(list)
    }

    // ─── SAVED ─────────────────────────────────────────────
    fun getSavedIds(): Set<String> {
        return prefs.getStringSet("saved", emptySet()) ?: emptySet()
    }

    fun toggleSaved(id: String) {
        val saved = getSavedIds().toMutableSet()
        if (saved.contains(id)) saved.remove(id) else saved.add(id)
        prefs.edit().putStringSet("saved", saved).apply()
    }
}
