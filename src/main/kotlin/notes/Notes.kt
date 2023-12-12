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
        userId: UInt,
        privacyView: String = "nobody",
        privacyComment: String = "nobody"
    ): Int {
        notes[++noteCounter] = Note(title, text, userId, privacy, commentPrivacy, privacyView, privacyComment)
        return noteCounter
    }

    fun get(
        noteIds: Set<Int>? = null,
        userId: UInt? = null,
        offset: Int = 0,
        count: Int = 0
    ): List<Note> {
        val result = arrayListOf<Note>()
        val keys = noteIds ?: notes.keys
        var recordsCounter = 0

        for (i in keys) {
            val note = notes[i] ?: continue

            if ((userId != null) && (note.userId != userId)) continue
            if (recordsCounter++ < offset) continue

            result.add(note)
            if ((count > 0) && (result.size >= count)) break
        }

        if (result.isEmpty())
            throw NotesNotFoundException()

        return result
    }

    fun getComments(
        noteId: Int,
        ownerId: UInt? = null,
        offset: Int = 0,
        count: Int = 0
    ): List<Comment> {
        val result = arrayListOf<Comment>()
        var counter = 0

        for (comment in comments) {
            if (comment.noteId != noteId) continue
            if ((ownerId != null) && (comment.ownerId != ownerId)) continue
            if (counter++ < offset) continue

            result.add(comment)
            if ((count > 0) && (result.size >= counter)) break
        }

        if (result.isEmpty())
            throw CommentsNotFoundException()

        return result
    }

    fun delete(noteId: Int) {
        if (notes.remove(noteId) == null)
            throw NoteNotFoundException(noteId)
    }

    fun getById(noteId: Int): Note {
        return notes[noteId] ?: throw NoteNotFoundException(noteId)
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

    private fun getCommentByIdAndOwner(commentId: Int, ownerId: UInt): Comment {
        val comment = if ((commentId > 0) && (comments.size >= commentId)) comments[commentId - 1] else throw CommentNotFoundException(commentId)

        if (comment.ownerId != ownerId)
            throw CommentWrongOwnerException(commentId)

        return comment
    }

    fun createComment(
        noteId: Int,
        ownerId: UInt,
        message: String,
        guid: String,
        replyTo: UInt? = null
    ): Int {
        getById(noteId)
        comments.add(
            Comment(
                noteId = noteId,
                ownerId = ownerId,
                replyTo = replyTo,
                message = message,
                guid = guid
            )
        )
        return comments.size
    }

    fun deleteComment(
        commentId: Int,
        ownerId: UInt
    ) {
        val comment = getCommentByIdAndOwner(commentId, ownerId)

        if (comment.isDeleted)
            throw CommentNotFoundException(commentId)

        comment.isDeleted = true
    }

    fun restoreComment(
        commentId: Int,
        ownerId: UInt
    ) {
        val comment = getCommentByIdAndOwner(commentId, ownerId)

        if (!comment.isDeleted)
            throw CommentNotFoundException(commentId)

        comment.isDeleted = false
    }

    fun editComment(
        commentId: Int,
        ownerId: UInt,
        message: String
    ) {
        val comment = getCommentByIdAndOwner(commentId, ownerId)

        if (comment.isDeleted)
            throw CommentNotFoundException(commentId)

        comment.message = message
    }
}
