package notes

class CommentWrongOwnerException(noteId : Int) : RuntimeException("Comment with id=$noteId owned by another owner")