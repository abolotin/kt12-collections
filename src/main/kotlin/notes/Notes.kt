package notes

object Notes {
    private var notes: MutableMap<Int, Note> = mutableMapOf()
    private var comments: MutableList<Comment> = mutableListOf()
    private var noteCounter: Int = 0

    fun clear() {
        notes.clear()
        comments.clear()
        noteCounter = 0
    }

    fun add(
        title: String,
        text: String,
        privacy: Int = 3,
        commentPrivacy: Int = 3,
        privacyView: String = "nobody",
        privacyComment: String = "nobody"
    ): Int {
        notes[noteCounter++] = Note(title, text, privacy, commentPrivacy, privacyView, privacyComment)
        return noteCounter
    }

    fun delete(noteId: Int) {
        if (notes.remove(noteId - 1) == null)
            throw NoteNotFoundException(noteId)
    }

    fun getById(noteId: Int): Note {
        return notes[noteId - 1] ?: throw NoteNotFoundException(noteId)
    }

    fun edit(
        noteId: Int,
        title: String,
        text: String,
        privacy: Int? = null,
        commentPrivacy: Int? = null,
        privacyView: String? = null,
        privacyComment: String? = null
    ) {
        val note = getById(noteId)
        note.title = title
        note.text = text
        note.privacy = privacy ?: note.privacy
        note.commentPrivacy = commentPrivacy ?: note.commentPrivacy
        note.privacyView = privacyView ?: note.privacyView
        note.privacyComment = privacyComment ?: note.privacyComment
    }

    private fun getCommentByIdAndOwner(commentId: Int, ownerId: Int): Comment {
        var comment = if (comments.size >= commentId) comments[commentId - 1] else null

        if (comment == null)
            throw CommentNotFoundException(commentId)

        if (comment.ownerId != ownerId)
            throw CommentWrongOwnerException(commentId)

        return comment
    }

    fun createComment(
        noteId: Int,
        ownerId: Int,
        message: String,
        guid: String,
        replyTo: Int? = null
    ): Int {
        val note = getById(noteId)
        comments.add(
            Comment(
                noteId = noteId,
                ownerId = ownerId,
                replyTo = replyTo,
                message = message,
                guid = guid
            )
        )
        return comments.size;
    }

    fun deleteComment(
        commentId: Int,
        ownerId: Int
    ) {
        var comment = getCommentByIdAndOwner(commentId, ownerId)

        if (comment.isDeleted)
            throw CommentNotFoundException(commentId)

        comment.isDeleted = true
    }

    fun restoreComment(
        commentId: Int,
        ownerId: Int
    ) {
        var comment = getCommentByIdAndOwner(commentId, ownerId)

        if ((comment == null) || !comment.isDeleted)
            throw CommentNotFoundException(commentId)

        comment.isDeleted = false
    }

    fun editComment(
        commentId: Int,
        ownerId: Int,
        message: String
    ) {
        var comment = getCommentByIdAndOwner(commentId, ownerId)

        if (comment.isDeleted)
            throw CommentNotFoundException(commentId)

        comment.message = message
    }
}
