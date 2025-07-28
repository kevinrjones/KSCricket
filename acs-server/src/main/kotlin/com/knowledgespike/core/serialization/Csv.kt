package com.knowledgespike.core.serialization

import com.knowledgespike.LoggerDelegate
import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.json.Envelope
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import java.nio.charset.Charset

/**
 * Registers a CSV content converter for the application configuration with a specified
 * content type and separator.
 *
 * @param contentType Specifies the content type for CSV serialization and deserialization.
 * Defaults to `Csv`.
 * @param separator Specifies the column separator used in the CSV format. Defaults to `","`.
 */
fun Configuration.csv(
    contentType: ContentType = Csv,
    separator: String = ",",
) {
    register(contentType, CsvContentConverter(separator))
}

/**
 * Represents the MIME type for CSV (Comma-Separated Values) content.
 * This is used to identify and handle content in CSV format within the system.
 *
 * - Type: `text/csv`
 * - Commonly used in data serialization and deserialization processes that involve
 *   CSV file handling or content negotiation.
 */
val Csv: ContentType = ContentType(
    "text",
    "csv"
)


/**
 * A content converter for serializing data to CSV format and deserializing from CSV format.
 *
 * This class implements the Ktor [ContentConverter] interface and provides functionality to
 * serialize objects implementing the [IToCsv] interface into CSV data and to define headers for
 * CSV export. It is also designed to work with the [Envelope] wrapper and [DatabaseResult] class
 * to handle structured response data.
 *
 * @property separator The column separator used for generating CSV data (e.g., `","`).
 */
@Suppress("UNCHECKED_CAST")
class CsvContentConverter(private val separator: String) : ContentConverter {
    private val log by LoggerDelegate()

    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?,
    ): OutgoingContent {

        val envelope = try {
            value as Envelope<DatabaseResult<*>>
        } catch (e: Exception) {
            log.error(
                "The type passed to CsvContentConvertor::serialize is invalid, it should be a 'Envelope<DatabaseResult<*>>'",
                e
            )
            throw e
        }

        // this case may fail
        val data = try {
            envelope.result.data as List<IToCsv>
        } catch (e: Exception) {
            log.error(
                "The 'List' passed to CsvContentConvertor::serialize does not implement 'IToCsv'",
                e
            )
            throw e
        }

        val buffer = StringBuilder()

        if (data.isNotEmpty()) {
            val item = data[0]
            buffer.append(item.csvHeader(separator))
            buffer.append(System.lineSeparator())
        }

        data.forEach()
        {
            buffer.append(it.toCsv(separator))
            buffer.append(System.lineSeparator())
        }

        val osc = CsvContent(buffer.toString())
        return osc
    }
}

/**
 * Represents the content of a CSV file to be sent as part of an HTTP response.
 *
 * This class extends [OutgoingContent.ByteArrayContent] and formats the content as a CSV file,
 * setting the appropriate HTTP headers for content-disposition, content-type, and access control.
 *
 * @constructor
 * @param text The text content of the CSV file to be serialized as bytes.
 *
 * Properties:
 * - `bytes`: The serialized content in byte array format, encoded with the specified or default charset.
 *
 * Overrides:
 * - [bytes]: Returns the byte array representation of the CSV content.
 * - [headers]: Constructs and returns the HTTP response headers for the CSV content.
 */
class CsvContent(text: String) : OutgoingContent.ByteArrayContent() {
    private val bytes = text.toByteArray(contentType?.charset() ?: Charsets.UTF_8)

    override fun bytes(): ByteArray = bytes

    override val headers: Headers
        get() = Headers.build {
            this.append("Content-Disposition", "exportData; filename=foo.csv")
            this.append("Content-Type", "text/csv")
            @Suppress("UastIncorrectHttpHeaderInspection")
            this.append("access-control-expose-headers", "Content-Disposition")
        }
}
