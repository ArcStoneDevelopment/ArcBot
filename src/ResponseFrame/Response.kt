package ResponseFrame

interface Response {
    val type : ResponseType
    val responseNumber : Number
}

class MasterResponse(override val responseNumber: Number) : Response {
    override val type = ResponseType.MASTER
}

class ErrorResponse(override val responseNumber: Number) : Response {
    override val type = ResponseType.ERROR
    val args = ArrayList<String>()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        args.addAll(args)
    }
}

class SuccessResponse(override  val responseNumber: Number) : Response {
    override val type = ResponseType.SUCCESS
    val args = ArrayList<String>()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        args.addAll(args)
    }
}

class InformationResponse(override  val responseNumber: Number) : Response {
    override val type = ResponseType.INFORMATION
    var args = ArrayList<String>()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        args.addAll(args)
    }
}

class LevelResponse(override val responseNumber: Number) : Response {
    override val type = ResponseType.LEVEL
    var args = ArrayList<String>()

    constructor (responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        args.addAll(args)
    }

}
