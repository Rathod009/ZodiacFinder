package xyz.aboutdhruv.zodiacfinder.Modal

data class UserModal(
    val day:Int = 0,
    val month:Int = 0,
    val year:Int = 0,
    val hour:Int = 0,
    val min:Int = 0,
    val lat: Double = 22.29940,
    val lon:Double = 73.20812,
    val tzone: Double = 5.5,
    val sign:String = "",
    val SignLord:String = "",
    val Naksahtra:String = "",
    val Varna:String = "",
    val Tithi:String = ""
    )