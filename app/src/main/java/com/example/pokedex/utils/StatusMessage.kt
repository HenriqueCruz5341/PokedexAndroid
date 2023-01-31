package com.example.pokedex.utils

/**
 * A class that represents a status message.
 *
 * This class is used to store the status of some operation in app, like an api or a database request
 * and then show it to the user.
 *
 * @param resource The resource that is being requested
 * @param code The status code of the request
 * @param item The item that is being requested
 */
class StatusMessage (val resource: Int, val code: Int, val item : Int? = null){
}