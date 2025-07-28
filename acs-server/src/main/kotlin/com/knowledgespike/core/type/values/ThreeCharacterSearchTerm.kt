package com.knowledgespike.core.type.values

import arrow.core.raise.Raise
import com.knowledgespike.core.type.error.SearchTeamNameError

@JvmInline
value class ThreeCharacterSearchTerm private constructor(val value: String) {
    companion object {
        context(Raise<SearchTeamNameError>)
        operator fun invoke(value: String): ThreeCharacterSearchTerm {
            if (value.isNotEmpty()
                && value.length >= 3
            )
                return ThreeCharacterSearchTerm(value)
            raise(SearchTeamNameError("Search term ($value) is invalid"))

        }
    }
}