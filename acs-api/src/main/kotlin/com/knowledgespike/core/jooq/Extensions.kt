
package com.knowledgespike.core.jooq

/**
 * Retrieves the value of a specified field from the record, or returns null if the field does not exist.
 *
 * @param fieldName the name of the field to retrieve the value from
 * @param type the class type to which the field value should be cast
 * @return the value of the field cast to the specified type, or null if the field does not exist or cannot be retrieved
 */
fun <T> org.jooq.Record.getValueOrNull(fieldName: String, type: Class<T>): T? {
    return try {
        this.getValue(fieldName, type) as T
    } catch (e: IllegalArgumentException) {
        null
    }
}


