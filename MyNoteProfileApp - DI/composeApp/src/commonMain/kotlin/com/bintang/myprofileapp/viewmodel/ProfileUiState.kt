package com.bintang.myprofileapp.viewmodel

import com.bintang.myprofileapp.model.ProfileData

data class ProfileUiState(
    val profile: ProfileData = ProfileData(
        name = "Muhammad Bintang Alfasya",
        title = "Cyber Security Enthusiast",
        bio = "Informatics Engineering student at Institut Teknologi Sumatera (ITERA) " +
                "with strong interest in Cyber Security. Experienced in learning " +
                "ethical hacking, web security, and Capture The Flag (CTF) challenges. " +
                "Currently developing skills in penetration testing, vulnerability " +
                "analysis, and secure software development.",
        email = "muhammad.123140098@student.itera.ac.id",
        phone = "+62 845-7766-9988",
        location = "Lampung Selatan, Indonesia",
        github = "github.com/bintangalfasya",
        avatarInitials = "MB"
    ),
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false
)
