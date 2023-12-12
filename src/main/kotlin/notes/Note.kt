package notes

data class Note(
    var title: String,
    var text: String,
    val userId: UInt,
    var privacy: Int,
    var commentPrivacy: Int,
    var privacyView: String,
    var privacyComment: String
)