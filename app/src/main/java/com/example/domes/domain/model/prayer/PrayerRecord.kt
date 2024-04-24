package com.example.domes.domain.model.prayer

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domes.R
import java.time.LocalDate

data class MissedReason(
    val name: String,
    @DrawableRes val iconResId: Int,
) {
    companion object {
        val TRAVEL = MissedReason("Travel", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val SICK = MissedReason("Sick", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val PERIOD = MissedReason("Period", R.drawable.routine_fill0_wght400_grad0_opsz24)
        val PREGNANCY = MissedReason("Pregnancy", R.drawable.routine_fill0_wght400_grad0_opsz24)

        val ALL = listOf(TRAVEL, SICK, PERIOD, PREGNANCY)
    }
}

data class PrayerRecordDetail(
    val prayer: Prayer,
    var prayed: Boolean = false,
    var notes: String = "",
    var missedReason: MissedReason? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrayerRecordDetail

        if (prayer != other.prayer) return false
        if (prayed != other.prayed) return false
        if (notes != other.notes) return false
        if (missedReason != other.missedReason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prayer.hashCode()
        result = 31 * result + prayed.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + (missedReason?.hashCode() ?: 0)
        return result
    }
}

@Entity
data class PrayerRecord(
    @PrimaryKey val date: LocalDate,
    val details: List<PrayerRecordDetail> = List(Prayer.ALL.size) { index ->
        PrayerRecordDetail(Prayer.ALL[index])
    }
) {
}