import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

enum class MissedReason {
    FORGOT, SLEPT_THROUGH, SICK, TRAVELING, PERIOD, PREGNANT, POSTPARTUM, BREASTFEEDING, INCONTINENCE, OTHER
}

@Entity
data class PrayerRecord(
    @PrimaryKey val date: LocalDate,
    val fajr: PrayerRecordData = PrayerRecordData(),
    val dhuhr: PrayerRecordData = PrayerRecordData(),
    val asr: PrayerRecordData = PrayerRecordData(),
    val maghrib: PrayerRecordData = PrayerRecordData(),
    val isha: PrayerRecordData = PrayerRecordData()
)

data class PrayerRecordData(
    val prayed: Boolean = false,
    val timestamp: LocalDateTime? = null,
    val notes: String? = null,
    val missed: Boolean = false,
    val missedReason: MissedReason? = null,
    val makeup: Boolean = false
)