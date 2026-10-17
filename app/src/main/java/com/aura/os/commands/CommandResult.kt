package com.aura.os.commands

sealed interface CommandResult {
    val message: String
    data class Success(override val message: String) : CommandResult
    data class Failure(override val message: String) : CommandResult
    data class Unknown(override val message: String) : CommandResult
}
