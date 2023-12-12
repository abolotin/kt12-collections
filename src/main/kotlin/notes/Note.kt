package notes

data class Note(
    var title : String,
    var text : String,
    var privacy : Int,
    var commentPrivacy : Int,
    var privacyView : String,
    var privacyComment : String
)