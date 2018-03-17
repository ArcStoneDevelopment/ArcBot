package ResponseFrame

import Utility.LevelUser

interface Response {
    val type : ResponseType
    val responseNumber : Number
}

class ErrorResponse(override val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.ERROR
}

class SuccessResponse(override  val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.SUCCESS
}

class InformationResponse(override  val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.INFORMATION
}

class LevelResponse(override  val responseNumber: Number, user : LevelUser?) : Response {
    override val type : ResponseType = ResponseType.LEVEL
}
