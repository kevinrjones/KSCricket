package com.knowledgespike.core.contenttypeconverters


/**
 * Interface to provide CSV serialization capabilities.
 *
 * This interface is intended for entities that can be serialized into CSV format
 * and need to define both CSV data and header representations.
 */
interface IToCsv {
    fun toCsv(separator: String): String?
    fun csvHeader(separator: String): String?
}

