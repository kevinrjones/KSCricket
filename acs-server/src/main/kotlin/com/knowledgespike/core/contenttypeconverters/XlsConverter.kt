package com.knowledgespike.core.contenttypeconverters

import org.apache.poi.ss.usermodel.Sheet

/**
 * Interface to define methods required for serializing data to an Excel-compatible format.
 *
 * Implementations of this interface are expected to populate Excel worksheets
 * with both header and row data. This makes it useful in scenarios where structured
 * data needs to be represented and exported as an Excel spreadsheet.
 */
interface IToXls {
    fun addHeader(worksheet: Sheet?)
    fun addLine(worksheet: Sheet?, index: Int)
}