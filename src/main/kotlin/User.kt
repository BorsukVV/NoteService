class User (val name:String){
    private companion object {
        var numberOfInstance:Int = 0
    }
    init {
        numberOfInstance++
        Util.usersDB[numberOfInstance] = this
    }

    val id = numberOfInstance

    val notes: MutableList <Int> = emptyList<Int>().toMutableList()

    val friends: MutableSet <Int> = emptyList<Int>().toMutableSet()

    val requestsToFriends: MutableList <Int> = emptyList<Int>().toMutableList()

    override fun toString(): String {
        return "User ID: ${this.id}; user name: ${this.name}"
    }

}