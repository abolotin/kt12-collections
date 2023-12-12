package notes

class CommentNotFoundException(commentId : Int) : RuntimeException("Specified comment with id=$commentId does not found")