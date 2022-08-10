import Util.acceptFriendRequestCurrentUser
import Util.callPrivateUserInFriends
import Util.callPrivateUserInFriendsOfFriends
import Util.commentCreate
import Util.getFriendsNotes
import Util.noteAdd
import Util.noteCommentEdit
import Util.noteDelete
import Util.noteRestore
import Util.requestToFriends
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilTest {

    @Test
    fun noteAdd_ShouldAddNote() {

        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        val result = noteAdd(user, "note text")
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun notesGetAllFromUser() {
    }

    @Test
    fun notesGet() {
    }

    @Test
    fun noteDelete_ShouldDeleteNote() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        val note = Repository.notes.keys.last()
        val result = noteDelete(note)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun noteDelete_ShouldNotDeleteNoteOutOfRange() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        val result = noteDelete(20)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun noteRestore_ShouldRestoreDeletedNote() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        noteDelete(1)
        val result = noteRestore(1)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun noteRestore_ShouldNotRestoreDeletedNoteOutOfRange() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        noteDelete(1)
        val result = noteRestore(10)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun noteRestore_ShouldNotRestoreDeletedNoteUserNotFound() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        noteDelete(1)
        Util.usersDB.clear()
        val result = noteRestore(10)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun requestToFriends_ShouldAddUserIDToRequestsToFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        val result = requestToFriends(sender.id, recipient.id)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun requestToFriends_ShouldNotAddUserIDToRequestsToFriendsWrongSenderID() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val result = requestToFriends(15, recipient.id)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun requestToFriends_ShouldNotAddUserIDToRequestsToFriendsWrongRecipientID() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val sender = User("Bonkey")
        val result = requestToFriends(sender.id, 15)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun requestToFriends_ShouldNotAddUserIDToRequestsToFriendsWrongID() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val result = requestToFriends(10, 15)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun acceptFriendRequestCurrentUser_ShouldMoveFromRequestsToFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        requestToFriends(sender.id, recipient.id)
        val result = acceptFriendRequestCurrentUser(recipient)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun acceptFriendRequestCurrentUser_ShouldNotMoveFromRequestsToFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        requestToFriends(sender.id, recipient.id)
        val result = acceptFriendRequestCurrentUser(sender)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun getFriendsNotes_ShouldGetFriendsNotes() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        requestToFriends(sender.id, recipient.id)
        acceptFriendRequestCurrentUser(recipient)
//        println(recipient.friends)
        noteAdd(sender, "note 1 text")
        noteAdd(sender, "note 2 text")
        noteAdd(sender, "note 3 text")
        val friendsNotes = getFriendsNotes(recipient)
//        println(friendsNotes)
//        println()
//        println(Repository.notes.keys)
        val result = friendsNotes.containsAll(Repository.notes.keys) && Repository.notes.keys.containsAll(friendsNotes)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun getFriendsNotes_ShouldNotGetFriendsNotes() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        noteAdd(sender, "note 1 text")
        noteAdd(sender, "note 2 text")
        noteAdd(sender, "note 3 text")
        val friendsNotes = getFriendsNotes(recipient)
        val result = friendsNotes.containsAll(Repository.notes.keys) && Repository.notes.keys.containsAll(friendsNotes)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun noteCommentEdit_ShouldEditNote() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        val noteID = Repository.notes.keys.last()
        val note = Repository.notes[noteID] as Note
        val result = noteCommentEdit(user, note, "edited title", "edited text")
        val expected = true
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun noteCommentEdit_ShouldNotEditNoteBecauseNotOwner() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        val anotherUser = User("Bonkey")
        val noteID = Repository.notes.keys.last()
        val note = Repository.notes[noteID] as Note
        noteCommentEdit(anotherUser, note, "edited title", "edited text")

    }

    @Test
    fun commentCreate_ShouldCreateComment() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        val noteID = Repository.notes.keys.last()
        val result = commentCreate(noteID, user.id, null, "comment text")
        val expected = true
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun commentCreate_WrongReplyToParameter() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        noteAdd(user, "note text")
        val noteID = Repository.notes.keys.last()
        val result = commentCreate(noteID, user.id, 10, "comment text")
    }

    @Test
    fun callPrivateUserInFriends_TrueIfUserInFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        requestToFriends(sender.id, recipient.id)
        acceptFriendRequestCurrentUser(recipient)
        val result = callPrivateUserInFriends(recipient, sender.id)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun callPrivateUserInFriends_FalseIfUserNotInFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val recipient = User("Bob")
        val sender = User("Bonkey")
        requestToFriends(sender.id, recipient.id)
        val result = callPrivateUserInFriends(recipient, sender.id)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun callPrivateUserInFriendsOfFriends_TrueIfUserInFriendsOfFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        val friend = User("Bonkey")
        val friendOfFriend = User("Jobbs")
        requestToFriends(friendOfFriend.id, friend.id)
        acceptFriendRequestCurrentUser(friend)
        requestToFriends(friend.id, user.id)
        acceptFriendRequestCurrentUser(user)
        val result = callPrivateUserInFriendsOfFriends(user, friendOfFriend.id)
        val expected = true
        assertEquals(expected, result)
    }

    @Test
    fun callPrivateUserInFriendsOfFriends_FalseIfUserNotInFriendsOfFriends() {
        Repository.notes.clear()
        Util.usersDB.clear()
        val user = User("Bob")
        val friend = User("Bonkey")
        val notFriendOfFriend = User("Jobbs")
        requestToFriends(friend.id, user.id)
        acceptFriendRequestCurrentUser(user)
        val result = callPrivateUserInFriendsOfFriends(user, notFriendOfFriend.id)
        val expected = false
        assertEquals(expected, result)
    }
}