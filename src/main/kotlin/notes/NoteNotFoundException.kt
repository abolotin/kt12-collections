package notes

class NoteNotFoundException(noteId : Int) : RuntimeException("Specified note with id=$noteId does not found")