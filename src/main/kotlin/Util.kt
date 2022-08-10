import org.jetbrains.annotations.TestOnly

object Util {

    val usersDB = mutableMapOf<Int, User>()

    fun noteAdd(
        user: User,
        text: String,
        title: String = "Title",
        privacy: Privacy = Privacy.USER_ONLY,
        commentPrivacy: Privacy = Privacy.USER_ONLY,
    ): Boolean {
        val note = Note(text, user.id, title, privacy, commentPrivacy)
        return addToRepository(note)
    }

    private fun <D : Doc> addToRepository(doc: D): Boolean {
        var addToRepositoryResult = false
        when (doc) {
            is Note -> {
                addToRepositoryResult = usersDB[doc.ownerID]?.notes?.add(doc.id) == true
                Repository.notes[doc.id] = doc
            }
            is Comment -> {
                val note = Repository.notes[doc.noteID] as Note
                Repository.comments[doc.id] = doc
                addToRepositoryResult = note.comments.add(doc.id)
            }
        }
        return addToRepositoryResult
    }

    private fun <D : Doc> removeFromRepository(doc: D): Boolean {
        var removeFromRepositoryResult = false
        when (doc) {
            is Note -> {
                Repository.notesRecyclerBin[doc.id] = doc
                Repository.notes.remove(doc.id)
                removeFromRepositoryResult = usersDB[doc.ownerID]?.notes?.remove(doc.id) == true
            }
            is Comment -> {
                val note = Repository.notes[doc.noteID] as Note
                Repository.commentsRecyclerBin[doc.id] = doc
                Repository.comments.remove(doc.id)
                removeFromRepositoryResult = note.comments.remove(doc.id)
            }
        }
        return removeFromRepositoryResult
    }

    fun notesGetAllFromUser(user: User): MutableList<Doc> {
        val allUserNotes: MutableList<Doc> = emptyList<Doc>().toMutableList()
        for (id in user.notes) {
            Repository.notes[id]?.let { allUserNotes.add(it) }
        }
        return allUserNotes

    }

    fun notesGet(vararg noteIDs: Int): MutableList<Doc> {

        val notesByID: MutableList<Doc> = emptyList<Doc>().toMutableList()
        for (id in noteIDs) {
            if (id in Repository.notes.keys) Repository.notes[id]?.let { notesByID.add(it) }
        }
        return notesByID
    }

    fun noteDelete(noteID: Int): Boolean {
        var noteDeleteResult: Boolean = false
        if (noteID in Repository.notes.keys) {
            val note = Repository.notes[noteID]
            note?.let {
                noteDeleteResult = removeFromRepository(it)
            }
        }
        return noteDeleteResult
    }

    fun noteRestore(noteID: Int): Boolean {
        var noteRestoreResult = false
        if (noteID in Repository.notesRecyclerBin.keys) {
            val note = Repository.notesRecyclerBin[noteID]
            val user = usersDB[note?.ownerID]

            if (user != null) {
                note?.let {
                    Repository.notes[noteID] = it
                    noteRestoreResult = user.notes.add(noteID)
                    Repository.notesRecyclerBin.remove(noteID)
                }
            } else {
                println("User not found")
            }
        }
        return noteRestoreResult
    }

    fun requestToFriends(senderID: Int, recipientID: Int): Boolean {
        return if (senderID in usersDB.keys && recipientID in usersDB.keys) {
            usersDB[recipientID]?.requestsToFriends?.plusAssign(senderID)
            true
        } else false
    }

    fun acceptFriendRequestCurrentUser(user: User): Boolean {
        val acceptResult = user.friends.addAll(user.requestsToFriends)
        user.requestsToFriends.clear()
        return acceptResult
    }

    fun getFriendsNotes(currentUser: User): MutableList<Int> {
        val allFriendsNotes: MutableList<Int> = emptyList<Int>().toMutableList()
        for (friend in currentUser.friends) {
            usersDB[friend]?.let { allFriendsNotes.addAll(it.notes) }
        }
        return allFriendsNotes
    }

    fun <D : Doc> noteCommentEdit(user: User, doc: D, newTitle: String, newText: String): Boolean {
        var successfulEdit = false
        if (doc.ownerID == user.id) {
            when (doc) {
                is Note -> {
                    val originalNote = Repository.notes[doc.id] as Note
                    val editedNote = originalNote.copy(title = newTitle, text = newText)
                    originalNote.let {
                        successfulEdit = Repository.notes.replace(doc.id, it, editedNote)
                    }
                }
                is Comment -> {
                    val originalComment = Repository.comments[doc.id] as Comment
                    val editedComment = originalComment.copy(text = newText)
                    originalComment.let {
                        successfulEdit = Repository.comments.replace(doc.id, it, editedComment)
                    }
                }
            }
        } else throw RuntimeException("181 - Access denied.")
        return successfulEdit
    }

    fun commentCreate(
        noteID: Int,
        ownerID: Int,
        replyTo: Int?,
        text: String,
    ): Boolean {
        if (noteID in Repository.notesRecyclerBin.keys) throw RuntimeException("181 - Access to note denied.")
        val note = Repository.notes[noteID] as Note
        val noteOwner = usersDB[note.ownerID]
        var commentCreated = false

        noteOwner?.let {
            val commentsNotAllowed = when {
                note.privacy == Privacy.FRIENDS_ONLY && note.commentPrivacy == Privacy.USER_ONLY -> true
                note.privacy == Privacy.FRIENDS_AND_FRIENDS_OF_FRIENDS && note.commentPrivacy == Privacy.USER_ONLY -> true
                note.privacy == Privacy.ALL_USERS && note.commentPrivacy == Privacy.USER_ONLY -> true
                note.privacy == Privacy.FRIENDS_ONLY && !userInFriends(noteOwner, ownerID) -> true
                note.privacy == Privacy.FRIENDS_AND_FRIENDS_OF_FRIENDS && !userInFriendsOfFriends(
                    noteOwner,
                    ownerID
                ) -> true
                else -> {
                    false
                }
            }
            if (commentsNotAllowed) throw RuntimeException("182 - You can't comment this note")

            val parametersConflict = when {
                note.privacy == Privacy.FRIENDS_ONLY && note.commentPrivacy == Privacy.ALL_USERS -> true
                note.privacy == Privacy.FRIENDS_ONLY && note.commentPrivacy == Privacy.FRIENDS_AND_FRIENDS_OF_FRIENDS -> true
                note.privacy == Privacy.FRIENDS_AND_FRIENDS_OF_FRIENDS && note.commentPrivacy == Privacy.ALL_USERS -> true
                note.privacy == Privacy.USER_ONLY && note.commentPrivacy != Privacy.USER_ONLY -> true
                else -> {
                    false
                }
            }
            if (parametersConflict) throw IllegalArgumentException("Privacy arguments conflict")
            commentCreated = when (replyTo) {
                null -> {
                    val comment = Comment(noteID, ownerID, null, text)
                    addToRepository(comment)
                }
                in Repository.commentsRecyclerBin -> {
                    throw RuntimeException("You can't comment deleted record")
                }
                in Repository.comments -> {
                    val comment = Comment(noteID, ownerID, replyTo, text)
                    addToRepository(comment)
                }
                else -> {
                    throw RuntimeException("You can't comment non - existent record")
                }

            }
        }

        return commentCreated
    }

    private fun userInFriendsOfFriends(noteOwner: User, ownerID: Int): Boolean {
        val friendsOfFriends: MutableList<Int> = emptyList<Int>().toMutableList()
        for (friend in noteOwner.friends) {
            usersDB[friend]?.let { friendsOfFriends.addAll(it.friends) }
        }
        return friendsOfFriends.contains(ownerID) || userInFriends(noteOwner, ownerID)
    }

    private fun userInFriends(noteOwner: User, ownerID: Int): Boolean = noteOwner.friends.contains(ownerID)

    @TestOnly
    fun callPrivateUserInFriends(noteOwner: User, ownerID: Int) = userInFriends(noteOwner, ownerID)

    @TestOnly
    fun callPrivateUserInFriendsOfFriends(noteOwner: User, ownerID: Int) = userInFriendsOfFriends(noteOwner, ownerID)

}

enum class Privacy(val privacyCode: Int) {
    ALL_USERS(0),
    FRIENDS_ONLY(1),
    FRIENDS_AND_FRIENDS_OF_FRIENDS(2),
    USER_ONLY(3)
}