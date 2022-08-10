data class Comment(
    val noteID: Int,
    override val ownerID: Int,
    val replyTo: Int?,
    override val text: String) : Doc {
    private companion object {
        var numberOfInstance: Int = 0
    }

    init {
        numberOfInstance++
    }

    override val id = numberOfInstance

    override fun toString(): String {
        return if (replyTo != null) {
            "Comment to note $noteID\n [$id] Reply to $replyTo \n$text"
        } else {
            "Comment to note $noteID\n [$id] \n$text"
        }
    }
}