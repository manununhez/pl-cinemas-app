package today.kinema.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val datePattern = "yyyy-MM-dd"
    private const val weekDayPattern = "EEE"
    private const val dayPattern = "dd"
    private const val monthPattern = "MMM"

    private fun timestamp(date: String): Long? =
        simpleDateFormat(datePattern).parse(date)?.time

    private fun simpleDateFormat(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, Locale.getDefault())

    fun dateParse(date: String): Date =
        simpleDateFormat(datePattern).parse(date)

    fun dateFormat(date: String): String =
        simpleDateFormat(datePattern).format(timestamp(date))

    fun dateFormat(date: Date): String =
        simpleDateFormat(datePattern).format(date)

    fun weekDateFormat(date: Date): String = simpleDateFormat(weekDayPattern).format(date)

    fun weekDateFormat(date: String): String =
        simpleDateFormat(weekDayPattern).format(timestamp(date))

    fun dayDateFormat(date: String): String =
        simpleDateFormat(dayPattern).format(timestamp(date))

    fun monthDateFormat(date: String): String =
        simpleDateFormat(monthPattern).format(timestamp(date))
            .toUpperCase(Locale.getDefault())
            .replace(".", "")

    fun today(): Date = dateParse(simpleDateFormat(datePattern).format(Date()))

    fun getDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return dateFormat(calendar.time)
    }

    fun getComingDays(comingDays: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, comingDays)

        return dateFormat(calendar.time)
    }

}