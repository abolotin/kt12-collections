package notes

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.exp

class NotesTest {

    @Before
    fun setUp() {
        Notes.clear()
    }

    @Test
    fun addNote() {
        assertNotEquals(Notes.add("Title", "Text"), 0)
    }

    @Test
    fun deleteNoteOk() {
        val noteId = Notes.add("Title", "Text")

        assertNotNull(Notes.delete(noteId))
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteNoteFail() {
        assertNotNull(Notes.delete(1000))
    }

    @Test
    fun getByIdOk() {
        val noteId = Notes.add("Title", "Text")

        assertNotNull(Notes.getById(noteId))
    }

    @Test(expected = NoteNotFoundException::class)
    fun getByIdFail() {
        assertNotNull(Notes.getById(1000))
    }

    @Test
    fun editOk() {
        val noteId = Notes.add("Title", "Text")

        assertNotNull(Notes.edit(noteId, "Title 2", "Text 2"))
    }

    @Test(expected = NoteNotFoundException::class)
    fun editFail() {
        assertNotNull(Notes.edit(1000, "Title 2", "Text 2"))
    }

    @Test
    fun createCommentOk() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")

        assertNotEquals(0, Notes.createComment(noteId, ownerId, "Message", "GUID"))
    }

    @Test(expected = NoteNotFoundException::class)
    fun createCommentFailNote() {
        val ownerId = 1;

        assertNotEquals(0, Notes.createComment(1000, ownerId, "Message", "GUID"))
    }

    @Test
    fun deleteCommentOk() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");

        assertNotNull(Notes.deleteComment(commentId, ownerId))
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteCommentFailComment() {
        assertNotNull(Notes.deleteComment(1000, 1000))
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteCommentFailDoubleDelete() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");

        Notes.deleteComment(commentId, ownerId)
        assertNotNull(Notes.deleteComment(commentId, ownerId))
    }

    @Test(expected = CommentWrongOwnerException::class)
    fun deleteCommentFailOwner() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");

        assertNotNull(Notes.deleteComment(commentId, 1000))
    }

    @Test
    fun restoreCommentOk() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");
        Notes.deleteComment(commentId, ownerId)

        assertNotNull(Notes.restoreComment(commentId, ownerId))
    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreCommentFailComment() {
        assertNotNull(Notes.restoreComment(1000, 1000))
    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreCommentFailDoubleRestore() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");
        Notes.deleteComment(commentId, ownerId)
        Notes.restoreComment(commentId, ownerId)

        assertNotNull(Notes.restoreComment(commentId, ownerId))
    }

    @Test(expected = CommentWrongOwnerException::class)
    fun restoreCommentFailOwner() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");
        Notes.deleteComment(commentId, ownerId)

        assertNotNull(Notes.restoreComment(commentId, 1000))
    }

    @Test
    fun editCommentOk() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");

        assertNotNull(Notes.editComment(commentId, ownerId, "Message 2"))
    }

    @Test(expected = CommentNotFoundException::class)
    fun editCommentFailComment() {
        assertEquals(Unit::class, Notes.editComment(1000, 1000, "Message 2"))
    }

    @Test(expected = CommentWrongOwnerException::class)
    fun editCommentFailOwner() {
        val ownerId = 1;
        val noteId = Notes.add("Title", "Text")
        val commentId = Notes.createComment(noteId, ownerId, "Message", "GUID");

        assertNotNull(Notes.editComment(commentId, 1000, "Message 2"))
    }
}