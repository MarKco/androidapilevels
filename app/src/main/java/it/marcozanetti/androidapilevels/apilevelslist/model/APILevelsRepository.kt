package it.marcozanetti.androidapilevels.apilevelslist.model

/**
 * Generic repository in order to retrieve the list of API levels.
 * Should a Database be implemented we'd just need to change the
 * implementation of the repository
 */
interface APILevelsRepository {
    suspend fun getAPILevelsCompose(): List<SingleAPILevel>
}