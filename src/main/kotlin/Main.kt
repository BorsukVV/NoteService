fun main() {
    fillInWithTestData()

    val user = Util.usersDB.getOrElse(2) { println("User not found") }

    //println(user)

    if (user is User) {
        Util.noteAdd(user, "note text")
        Util.noteAdd(user, "note text1")
        Util.noteAdd(user, "note text2", "Title", Privacy.ALL_USERS, Privacy.ALL_USERS)

        val userNotes = Util.notesGetAllFromUser(user)
        printNotes(userNotes)
        val notesById = Util.notesGet(1, 3)
        println("\n")
        printNotes(notesById)
//        printAllNotes()
        println("\n")
        println("\n")
        Util.noteDelete(2)
        printAllNotes()
        Util.commentCreate(3,1, null, "Some text comment to note")
        println("\n")
        printNoteWithComments(3)
    }

}

private fun printNotes(userNotes: MutableList<Doc>) {
    if (!userNotes.isNullOrEmpty()) {
        for (note in userNotes) {
            println(note)
        }
    }
}

private fun printAllNotes() {

    for (note in Repository.notes) {
        println(note)
    }
}

fun printNoteWithComments(noteID: Int){
    val note = Repository.notes[noteID] as Note
    println(note)
    println("*********")
    for(comment in note.comments) {
        println(Repository.comments[comment])
    }
}

fun fillInWithTestData() {
    val user1 = User("Bob")
    val user2 = User("Bonkey")
    val user3 = User("Jobbs")
    val user4 = User("Bill")
    val user5 = User("Jeff")

}
