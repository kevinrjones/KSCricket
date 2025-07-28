package com.knowledgespike

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

/**
 * Determines the appropriate class to use for logging purposes. If the provided class is a
 * companion object, it returns the enclosing class. Otherwise, it returns the provided class.
 *
 * @param javaClass The class whose logging context is to be resolved. It must be a non-null class reference.
 * @return The class to use for logging, either the enclosing class if the provided class is a companion object,
 * or the provided class itself.
 */
fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}

/**
 * A delegate class for providing a Logger instance tied to the class using the delegate.
 * The Logger instance is lazily resolved and specific to the enclosing class of the receiver.
 *
 * @param R The type of the receiver class that the Logger is bound to.
 */
class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    /**
     * Retrieves a logger instance for the specified property of a given class reference.
     * This method is used to delegate logging behavior dynamically based on the class
     * that owns the property. It provides a logger suitable for logging purposes in the
     * context of the class hierarchy.
     *
     * @param thisRef The reference to the class instance that contains the delegated property.
     * @param property The metadata representation of the property being delegated to this logger.
     * @return A logger instance appropriate for the specified class context.
     */
    override fun getValue(thisRef: R, property: KProperty<*>)
            = getLogger(getClassForLogging(thisRef.javaClass))
}

/**
 * Retrieves a logger instance for the specified class.
 *
 * @param forClass The class for which the logger will be retrieved.
 * @return A logger instance configured for the given class.
 */
fun getLogger(forClass: Class<*>): Logger =
    LoggerFactory.getLogger(forClass)