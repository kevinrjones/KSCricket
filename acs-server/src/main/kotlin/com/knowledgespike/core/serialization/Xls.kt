package com.knowledgespike.core.serialization

import com.knowledgespike.LoggerDelegate
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.json.Envelope
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset


/**
 * Registers an XLS content converter for the application configuration.
 *
 * @param contentType Specifies the content type for XLS serialization and deserialization.
 * Defaults to `Xls`.
 */
fun Configuration.xls(
    contentType: ContentType = Xls,
) {
    register(contentType, XlsContentConverter())
}

/**
 * Represents the MIME type for Microsoft Excel (.xls) files.
 * This value is commonly used for identifying and handling content
 * in Microsoft's Excel binary file format (XLS) within the system.
 *
 * - Type: `application/vnd.ms-excel`
 * - Often associated with older versions of Excel file handling,
 *   particularly prior to the adoption of the XLSX format.
 */
val Xls: ContentType = ContentType(
    "application",
    "vnd.ms-excel"
)


/**
 * A content converter for serializing data to XLS (Excel spreadsheet) format
 * and deserializing from XLS format.
 *
 * This class implements the Ktor [ContentConverter] interface and provides the functionality
 * to serialize objects extending the [IToXls] interface into Excel-compatible data.
 * It is designed to work with the [Envelope] wrapper and [DatabaseResult] class to handle
 * structured response data.
 *
 * The output is configured to expose headers suitable for serving the XLS file
 * as a downloadable resource in HTTP requests.
 */
@Suppress("UNCHECKED_CAST")
class XlsContentConverter : ContentConverter {
    /**
     * A logger instance delegated by the [LoggerDelegate] to provide logging
     * capabilities within the `XlsContentConverter` class.
     *
     * This property allows for structured and reusable logging throughout the
     * lifecycle of the content converter, capturing errors and debugging information
     * during operations, such as serializing data to Excel-compatible formats.
     */
    private val log by LoggerDelegate()

    /**
     * Deserializes the content from the specified [ByteReadChannel] into an object of the specified type.
     *
     * @param charset The character set used for decoding the content.
     * @param typeInfo The type information of the expected deserialized object.
     * @param content The [ByteReadChannel] containing the content to be deserialized.
     * @return The deserialized object, or null if deserialization is unsuccessful.
     */
    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        TODO("Not yet implemented")
    }

    /**
     * Serializes the given input into an Excel-compatible format, encapsulated as an `OutgoingContent`.
     * Converts the provided `value` into an XLSX file representation containing structured data, by leveraging
     * implementations of `IToXls` for data population.
     *
     * @param contentType The content type describing the format of the serialized output.
     * @param charset The character set to be used during serialization.
     * @param typeInfo Metadata about the type of the object to be serialized.
     * @param value The data object to serialize, expected to be an `Envelope<DatabaseResult<*>>`.
     *
     * @return The serialized content as an `XlsContent`, representing the binary XLSX file.
     * @throws Exception If the provided `value` does not conform to the expected structure or
     * if the data elements do not implement `IToXls`.
     */
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
                "The type passed to XlsContentConvertor::serialize is invalid, it should be a 'Envelope<DatabaseResult<*>>'",
                e
            )
            throw e
        }

        // this case may fail
        val data = try {
            envelope.result.data as List<IToXls>
        } catch (e: Exception) {
            log.error(
                "The 'List' passed to XlsContentConvertor::serialize does not implement 'IToXls'",
                e
            )
            throw e
        }

        val workbook: Workbook = XSSFWorkbook()
        val byteArrayOutputStream = ByteArrayOutputStream()
        workbook.use {


            if (data.isNotEmpty()) {
                val worksheet: Sheet = workbook.createSheet("Sheet1")
                val firstItem = data[0]
                firstItem.addHeader(worksheet)

                data.forEachIndexed { ndx, item ->
                    item.addLine(worksheet, ndx + 1)
                }
            }

            workbook.write(byteArrayOutputStream)
        }
        val osc = XlsContent(byteArrayOutputStream.toByteArray())
        return osc
    }
}

/**
 * Represents an outgoing HTTP content payload specifically for Excel (XLSX) files.
 *
 * This class extends [OutgoingContent.ByteArrayContent] to generate and serve
 * an Excel file as the response content. It sets appropriate HTTP headers
 * to instruct the client regarding the content type and content disposition.
 *
 * @constructor
 * @param content A byte array representing the serialized XLSX file data.
 *
 * Overrides:
 * - [bytes]: Returns the byte array of the content.
 * - [headers]: Defines HTTP headers for the XLSX file response, including
 *   content type, file name, and content disposition.
 */
class XlsContent(content: ByteArray) : OutgoingContent.ByteArrayContent() {
    /**
     * Stores the byte array content for this instance.
     *
     * This property holds the raw byte data for the content to be used in overridden methods,
     * such as [bytes]. It is initialized with the `content` parameter provided to the class
     * constructor.
     */
    private val bytes = content

    /**
     * Returns the byte array representation of the content.
     *
     * @return A byte array containing the content.
     */
    override fun bytes(): ByteArray = bytes

    /**
     * Represents the HTTP headers associated with the exported XLS content.
     *
     * This property overrides the [headers] property from the base class and defines
     * specific headers to inform the client of the file's content disposition, type, and
     * accessibility. The headers include:
     *
     * - `Content-Disposition`: Indicates the content is an XLS file with a specified filename.
     * - `Content-Type`: Specifies the MIME type for XLS files.
     * - `access-control-expose-headers`: Allows the `Content-Disposition` header to be exposed to the client.
     */
    override val headers: Headers
        get() = Headers.build {
            this.append("Content-Disposition", "exportData; filename=\"exportData.xlsx\"")
            this.append("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            @Suppress("UastIncorrectHttpHeaderInspection")
            this.append("access-control-expose-headers", "Content-Disposition")
        }
}

/**
 * Creates a new cell in the specified row at the given index and assigns a value to it.
 *
 * @param index The index within the row where the new cell should be created.
 * @param value The value to be assigned to the newly created cell.
 */
fun Row.createCellAndAddValue(index: Int, value: String) {
    val cell = this.createCell(index)
    cell.setCellValue(value)
}

/**
 * Creates a new cell in the given row at the specified index and sets its value to the provided double value.
 *
 * @param index The index at which the cell should be created in the row.
 * @param value The double value to be set in the created cell.
 */
fun Row.createCellAndAddValue(index: Int, value: Double) {
    val cell = this.createCell(index)
    cell.setCellValue(value)
}

/**
 * Creates a new cell in the given row at the specified index and sets its value to the provided double value.
 *
 * @param index The index at which the cell should be created in the row.
 * @param value The double value to be set in the created cell.
 */
fun Row.createCellAndAddValue(index: Int, value: Int) {
    createCellAndAddValue(index, value.toDouble())
}

/**
 * Creates a cell at the specified index in the row and sets its value using the boolean parameter.
 *
 * @param index The index of the cell to be created in the row.
 * @param value The boolean value to be set in the created cell.
 */
fun Row.createCellAndAddValue(index: Int, value: Boolean) {
    val cell = this.createCell(index)
    cell.setCellValue(value)
}