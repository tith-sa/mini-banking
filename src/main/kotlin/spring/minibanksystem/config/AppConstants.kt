package spring.minibanksystem.config

object AppConstants {

    // Date-time format used when returning timestamps in API responses.
    // Example: 2026-08-11T09:43:00.123+07:00
    const val DATETIME_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    // Default timezone used for system/API timestamps.
    // UTC is recommended for storing timestamps consistently.
    const val ZONE_ID: String = "UTC"

    // Local timezone used for Cambodia/Phnom_Penh.
    const val LOCAL_TZ: String = "Asia/Phnom_Penh"
}