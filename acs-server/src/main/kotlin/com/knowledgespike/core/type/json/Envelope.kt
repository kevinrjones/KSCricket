package com.knowledgespike.core.type.json

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Represents a generic envelope structure to encapsulate API responses or result sets
 * along with metadata such as error messages and timestamps.
 *
 * This class is designed to provide a consistent response structure for successful
 * results or failure cases with appropriate context. The `result` property holds the
 * main data payload in case of success, while the `errorMessage` property stores error
 * details in failure cases.
 *
 * @param T The type of the result data to be contained within the envelope.
 * @property result The data or payload for a successful operation.
 * @property errorMessage A message describing the error in case of failure.
 * @property timeGenerated The timestamp when the envelope was generated.
 */
@Serializable
data class Envelope<T>(val result: T, val errorMessage: String, val timeGenerated: Instant) {
    /**
     * Companion object providing utility functions to create `Envelope` instances
     * for both successful and failure cases.
     */
    companion object {
        /**
         * Creates an `Envelope` object representing a successful operation with the given result.
         *
         * @param result The successful result data to be wrapped in the envelope.
         * @return An `Envelope` containing the result, an empty error message, and the current timestamp.
         */
        fun <T> success(result: T) : Envelope<T> {
            return Envelope(result, "", Clock.System.now())
        }

        /**
         * Creates an `Envelope` instance representing a failure with the specified error message.
         *
         * @param message A `String` containing the error message to be encapsulated in the failure envelope.
         * @return An `Envelope<String>` instance with an empty result, the provided error message, and the current timestamp.
         */
        fun failure(message: String) : Envelope<String> {
            return Envelope("", message, Clock.System.now())
        }
    }
}

