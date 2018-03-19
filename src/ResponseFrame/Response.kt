package ResponseFrame

import Utility.LevelUser

interface Response {
    val type : ResponseType
    val responseNumber : Number
}

class ErrorResponse(override val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.ERROR
    val args : ArrayList<String> = ArrayList()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        for (s : String in args) {
            this.args.add(s)
        }
    }
}

class SuccessResponse(override  val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.SUCCESS
    val args : ArrayList<String> = ArrayList()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        for (s : String in args) {
            this.args.add(s)
        }
    }
}

class InformationResponse(override  val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.INFORMATION
    var args : ArrayList<String> = ArrayList()

    constructor(responseNumber: Number, args: ArrayList<String>) : this(responseNumber) {
        for (s : String in args) {
            this.args.add(s)
        }
    }
}

class LevelResponse private constructor(override  val responseNumber: Number) : Response {
    override val type : ResponseType = ResponseType.LEVEL
    val user : ArrayList<LevelUser> = ArrayList()

    constructor(responseNumber: Number, topTen : ArrayList<LevelUser>) : this(responseNumber) {
        for (idx in 0 until topTen.size-1) {
            user.add(topTen[idx])
        }
    }

    constructor(responseNumber: Number, levelUser: LevelUser) : this(responseNumber) {
        this.user.add(levelUser)
    }
}
