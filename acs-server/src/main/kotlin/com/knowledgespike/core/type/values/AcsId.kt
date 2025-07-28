package com.knowledgespike.core.type.values

import arrow.core.raise.Raise
import com.knowledgespike.core.type.error.*
import com.knowledgespike.core.type.shared.MatchTypes.validMatchTypes

@JvmInline
value class TeamId private constructor(val id: Int) {
    companion object {

        context(Raise<TeamIdError>)
        operator fun invoke(value: Int?): TeamId {
            if (value != null && value >= 0)
                return TeamId(value)

            raise(TeamIdError("The 'teamId' $value is not a valid team id", value))
        }
    }
}

@JvmInline
value class PlayerId private constructor(val id: Int) {
    companion object {

        context(Raise<PlayerIdError>)
        operator fun invoke(value: Int?): PlayerId {
            if (value != null && value >= 0)
                return PlayerId(value)

            raise(PlayerIdError("The 'playerId' $value is not a valid player id", value))
        }
    }
}

@JvmInline
value class CountryId private constructor(val id: Int) {
    companion object {

        fun default(): CountryId = CountryId(0)

        context(Raise<CountryIdError>)
        operator fun invoke(value: Int?): CountryId {

            if (value != null && value >= 0)
                return CountryId(value)

            raise(CountryIdError("The 'countryId' $value is not a valid country id", value))
        }
    }
}


@JvmInline
value class VenueId(val id: Int) {
    companion object {
        context(Raise<VenueIdError>)
        operator fun invoke(value: Int?): VenueId {
            if (value != null && value >= 0)
                return VenueId(value)

            raise(VenueIdError("The 'venueId' $value is not a valid venue id", value))

        }
    }
}

@JvmInline
value class GroundId(val id: Int) {
    companion object {
        context(Raise<GroundIdError>)
        operator fun invoke(value: Int?): GroundId {
            if (value != null && value >= 0)
                return GroundId(value)

            raise(GroundIdError("The 'groundId' $value is not a valid ground id", value))

        }
    }
}

@JvmInline
value class CaId private constructor(val id: String?) {
    companion object {
        context(Raise<CaIdError>)
        operator fun invoke(value: String?): CaId {
            if (value == null)
                raise(CaIdError("The 'matchId' $value is not a valid match id", value))

            // CaId's are like t12345, a match type followed by an id - these are invariant in the database even after a rebuild
            val regex = Regex("^([a-zA-Z]{1,3})([0-9]{1,8})$")

            val matchResult =
                regex.matchEntire(value) ?: raise(CaIdError("The 'matchId' $value is not a valid match id", value))

            val (chars, digits) = matchResult.destructured

            if (!validMatchTypes.contains(chars) || digits.toIntOrNull() == null)
                raise(CaIdError("The 'matchId' $value is not a valid match id", value))

            return CaId(value)

        }
    }
}

