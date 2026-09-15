package com.kakaanime.app.ui.profile

data class EditProfileUiState(
    val username: String,
    val bio: String = "",
    val status: String = "Online",
)
