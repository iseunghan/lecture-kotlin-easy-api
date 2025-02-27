package me.iseunghan.lectureeasykopringapi.core.response

data class ErrorResponse(
    val message: String,
    val errorType: String = "Invalid Argument"
)
