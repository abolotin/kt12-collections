package notes

data class Comment(
    val noteId : Int,
    val ownerId : UInt,
    val replyTo: UInt?,
    var message: String,
    val guid : String,
    var isDeleted : Boolean = false
)
