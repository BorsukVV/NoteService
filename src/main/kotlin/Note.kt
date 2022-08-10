import java.lang.IllegalArgumentException

data class Note(
    override val text: String,
    override val ownerID: Int,
    private val title: String,
    val privacy: Privacy,
    val commentPrivacy: Privacy
) : Doc {

    private companion object {
        var numberOfInstance:Int = 0
    }

    init {
        numberOfInstance++
    }

    override val id = numberOfInstance

    val comments: MutableList <Int> = emptyList<Int>().toMutableList()

    override fun toString(): String {
        val userSign = Util.usersDB.getValue(ownerID).let {
            "User ID: ${it.id} (${it.name})"
        }
        return "Note ID: ${this.id}; Title: ${this.title} \nNote: ${this.text} \n$userSign"
    }
}