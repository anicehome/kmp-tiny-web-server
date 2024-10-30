package io.github.anicehome.webserver.database

import androidx.room.TypeConverter

object WebServerTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String): List<Int> {
        return data.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
//                    Log.e("tag", "Cannot convert $it to number", ex)
                    null
                }
            }
        }.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>): String {
        return ints.joinToString(",")
    }

}
