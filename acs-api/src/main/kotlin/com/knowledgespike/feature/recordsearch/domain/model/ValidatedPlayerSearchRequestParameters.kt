package com.knowledgespike.feature.recordsearch.domain.model

import com.knowledgespike.core.type.shared.SortDirection
import com.knowledgespike.core.type.shared.SortOrder
import com.knowledgespike.core.type.values.EpochDate

/**
 * Encapsulates validated search parameters for querying player data, ensuring that the
 * provided search criteria conform to the expected format and rules.
 *
 * This class represents a combination of search fields such as the player's name,
 * associated team, filtering options, date range, and sorting configuration. It also
 * processes the player's name to derive additional parts useful for sorting or filtering.
 *
 * @property name The full name of the player to search for.
 * @property team The name of the team associated with the player.
 * @property exactMatch Indicates whether the search should be an exact match for the player's name.
 * @property startDate The inclusive start date for filtering results, represented as an `EpochDate`.
 * @property endDate The inclusive end date for filtering results, represented as an `EpochDate`.
 * @property sortOrder Specifies the criteria by which the result set is sorted.
 * @property sortDirection Specifies the direction of the sort (ascending or descending).
 *
 * @constructor Initializes the search parameters and processes the player's name to extract
 *              additional components like `sortNamePart` and `otherNamePart` for sorting purposes.
 */
data class ValidatedPlayerSearchRequestParameters(
    val name: String,
    val team: String,
    val exactMatch: Boolean,
    val startDate: EpochDate,
    val endDate: EpochDate,
    val sortOrder: SortOrder,
    val sortDirection: SortDirection,


    ) {
    /**
     * A variable used to store a part of the name for sorting purposes.
     *
     * This variable is initialized during object construction and represents the portion of the `name`
     * field that follows the first space, if a space is present. If no space exists in the `name`,
     * the entire `name` is assigned to this variable. It is primarily used to determine the sorting logic
     * for player search requests.
     */
    var sortNamePart: String = ""
    /**
     * Stores the first part of the player's name when the full name contains a space.
     *
     * This variable is a dynamically derived attribute based on the `name` parameter
     * of the enclosing data class. If the `name` contains a space, this variable will
     * hold the first part (substring before the space). If no space is present in the
     * name, this variable remains unmodified.
     *
     * This is intended to assist in sorting or filtering operations by enabling the
     * separation of name components.
     */
    var otherNamePart: String = ""
    init {
        if (name.contains(" "))
        {
            otherNamePart = name.split(" ").first()
            sortNamePart = name.substring(name.indexOf(" ") + 1)
        }
        else
        {
            sortNamePart = name
        }

    }
}