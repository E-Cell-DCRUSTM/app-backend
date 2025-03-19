package dcrustm.ecell.backend.auth.util

object TokenStore {
    // Here we simply maintain a set of emails for which tokens have been invalidated.
    val invalidatedEmails: MutableSet<String> = mutableSetOf()
}