package com.example.domes.domain.model.prayer

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.domes.R
import kotlinx.parcelize.Parcelize

//sealed class Icon {
//    data class Vector(val imageVector: ImageVector) : Icon()
//    data class Paint(val painter: Painter) : Icon()
//}

@Parcelize
data class Prayer(
    val name: String,
    val arabicName: String,
    @DrawableRes val iconResId: Int,
    val displayable: Boolean = true,
): Parcelable {
    companion object {
        val FAJR = Prayer("Fajr", "فجر", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val SUNRISE = Prayer("Sunrise", "شروق", R.drawable.routine_fill0_wght400_grad0_opsz24, false)
        val DHUHR = Prayer("Dhuhr", "ظهر", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val ASR = Prayer("Asr", "عصر", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val MAGHRIB = Prayer("Maghrib", "مغرب", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val ISHA = Prayer("Isha", "عشاء", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val TEST = Prayer("Test", "فجر", R.drawable.routine_fill0_wght400_grad0_opsz24, false)
    }
}