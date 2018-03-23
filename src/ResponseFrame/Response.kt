package ResponseFrame

import ResponseFrame.ResponseType

interface Response {
    val type : ResponseType
    val responseNumber : Number
}

class ErrorResponse(override val responseNumber: Number) : Response {
    override val type = ResponseType.ERROR
    val args : ArrayList<String> = ArrayList()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        for (s : String in args) {
            this.args.add(s)
        }
    }
}

class SuccessResponse(override  val responseNumber: Number) : Response {
    override val type = ResponseType.SUCCESS
    val args : ArrayList<String> = ArrayList()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        for (s : String in args) {
            this.args.add(s)
        }
    }
}

class InformationResponse(override  val responseNumber: Number) : Response {
    override val type = ResponseType.INFORMATION
    var args : ArrayList<String> = ArrayList()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        for (s : String in args) {
            this.args.add(s)
        }
    }
}

class LevelResponse(override val responseNumber: Number) : Response {
    override val type = ResponseType.LEVEL
}
