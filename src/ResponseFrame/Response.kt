package ResponseFrame

class Response constructor (val type : ResponseType, val responseNumber : Number, val args : Array<String>) {

    var footer : String?
    var image : String?
    var title : String?
    var description : String?
    var fields : Array<Field>?

    init {
        footer = null
        image = null
        title = null
        description = null
        fields = null
    }
}

class Field constructor(val title : String, val text : String, val inline : Boolean)