package notes

data class Comment(
    val noteId : Int,
    val ownerId : Int,
    val replyTo: Int?,
    var message: String,
    val guid : String,
    var isDeleted : Boolean = false
)
