package com.example.domes.domain.model

data class Prayer(
    val name: String,
    val arabicName: String,
    val icon: Any, // Replace 'Any' with the appropriate type for your icons
    val time: String,
)