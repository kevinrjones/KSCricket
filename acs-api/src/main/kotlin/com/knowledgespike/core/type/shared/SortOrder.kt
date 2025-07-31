package com.knowledgespike.core.type.shared


/**
 * Represents the sorting criterion by which data can be organized or filtered in a query.
 *
 * Each sorting criterion is represented by a unique integer value. This enum is typically
 * used in scenarios requiring sorting based on predefined fields such as runs, wickets,
 * matches, etc., making it easier to map user-friendly terms to internal logic for sorting operations.
 *
 * @property value The integer value corresponding to the sorting criteria.
 */
enum class SortOrder(val value: Int) {
    Unknown(0),
    SortNamePart(1),
    Teams(2),
    Opponents(3),
    Runs(4),
    Wickets(5),
    Balls(6),
    Year(7),
    Matches(8),
    Won(9),
    Lost(10),
    Tied(11),
    Drawn(12),
    Byes(13),
    LegByes(14),
    Wides(15),
    NoBalls(16),
    MatchStartDateAsOffset(17),
    VictoryMargin(18),
    SeriesDate(19),
    KnownAs(20),
    CountryName(21),
    MatchStartYear(22),
    Penalties(23),
    Extras(24),
    Percentage(25),
    Dismissals(26),
    Caught(27),
    Stumpings(28),
    bestCaughtKeeper(29),
    bestCaughtFielder(30),
    BestDismissals(31),
    Innings(32),
    Fours(33),
    Sixes(34),
    InningsOrder(35),
    Bat1(36),
    Bat2(37),
    Minutes(38),
    NotOuts(39),
    HighestScore(40),
    Avg(41),
    Fifties(42),
    Hundreds(43),
    Ducks(44),
    BBI(45),
    BBM(46),
    Maidens(47),
    Dots(48),
    TenFor(49),
    FiveFor(50),
    Rpo(51),
    SR(52),
    BallsPerOver(53),
    BI(54),
    Result(55),
    Margin(56),
    LS(57),
    Wicket(58),
    In(59),
    Out(60),
    DebutAsOffset(61),
    ActiveUntilAsOffset(62),
    WicketKeeperDismissals(63),
    Totals(64),
    Ground(65),
    caughtKeeper(66),
    caughtFielder(67),

    ;
    companion object {
        /**
         * Provides a utility function to retrieve a SortOrder enum constant based on its integer value.
         * If no matching constant is found, the default value `Unknown` is returned.
         *
         * @function get Retrieves the SortOrder corresponding to the given integer value.
         * @param value The integer value for which to find the corresponding SortOrder.
         * @return The SortOrder matching the given value, or `Unknown` if no match is found.
         */
        operator fun get(value: Int) = entries.firstOrNull { it.value == value } ?: Unknown
    }
}

