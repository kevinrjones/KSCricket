package com.knowledgespike.feature.scorecard.domain.model

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing a detailed scorecard of a match.
 *
 * @property notes A list of additional notes or information related to the match.
 * @property debuts A list of players making their debuts in the match, represented by `DebutDto`.
 * @property header Metadata about the match such as teams, match result, umpires, and other contextual details, represented by `ScorecardHeaderDto`.
 * @property innings A list of innings in the match, represented by `InningDto`, capturing the performance of batting and bowling teams and other key statistics.
 */
@Serializable
data class ScorecardDto(
    val debuts: List<DebutDto>,
    val header: ScorecardHeaderDto,
    val innings: List<InningDto>
)

/**
 * Represents the header information of a cricket scorecard.
 *
 * @property toss Indicates the team that won the toss and chose to bat or field. Can be null if the toss data is unavailable.
 * @property where Location details where the match took place.
 * @property result Outcome of the match, including information on the winning and losing teams, and victory type.
 * @property scorers List of persons associated with scoring in the match.
 * @property umpires List of umpires officiating the match.
 * @property awayTeam Team designated as the away team for the match.
 * @property awayTeamScores List of scores associated with the away team across various innings.
 * @property dayNight Boolean indicating if the match was played as a day-night match.
 * @property homeTeam Team designated as the home team for the match.
 * @property homeTeamScores List of scores associated with the home team across various innings.
 * @property tvUmpires List of TV umpires responsible for reviewing on-field decisions.
 * @property matchDate Date on which the match was played.
 * @property matchType Type of the match (e.g., Test, ODI, T20).
 * @property matchTitle Title or name of the match/event.
 * @property seriesDate Series duration in which the match occurred.
 * @property closeOfPlay List of notes recorded at the close of play for specific days of the match.
 * @property playerOfMatch List of players awarded as the Player of the Match.
 * @property ballsPerOver Number of balls bowled in an over for the match.
 * @property matchReferee List of referees overseeing the match.
 * @property matchDesignator A descriptor denoting the match’s designation (e.g., league match, finals).
 */
@Serializable
data class ScorecardHeaderDto(
    val toss: ScorecardTeamDto?,
    val where: LocationDto,
    val result: ResultDto,
    val scorers: List<PersonDto>,
    val umpires: List<PersonDto>,
    val awayTeam: ScorecardTeamDto,
    val awayTeamScores: List<String>,
    val dayNight: Boolean,
    val homeTeam: ScorecardTeamDto,
    val homeTeamScores: List<String>,
    val tvUmpires: List<PersonDto>,
    val matchDate: String,
    val matchType: String,
    val matchTitle: String,
    val seriesDate: String,
    val closeOfPlay: List<CloseOfPlayDto>,
    val playerOfMatch: List<PersonDto>,
    val ballsPerOver: Int,
    val matchReferee: List<PersonDto>,
    val matchDesignator: String
)

/**
 * Represents the type of victory in a match. Various victory conditions are defined
 * to describe the outcome of a match based on specific scenarios.
 */
@Serializable
enum class VictoryType {
    /**
     * Represents a type of victory that is assigned or awarded, rather than achieved through conventional
     * gameplay circumstances. This is typically used in scenarios where a team or participant wins by
     * external decision or ruling, such as forfeiture, disqualification, or other administrative factors.
     */
    Awarded,
    /**
     * Represents a type of victory outcome where the match ends with no definitive winner,
     * categorized as a draw.
     *
     * This value is a part of the `VictoryType` enumeration, which defines various outcomes
     * for a match. The type `Drawn` is used in contexts where neither team achieves a win
     * due to circumstances such as equal scores or completion of the match without decisive results.
     */
    Drawn,
    /**
     * Represents a collection of runs or sequences.
     *
     * This class serves as a way to manage and manipulate runs,
     * which could represent sequential data or event occurrences.
     * It provides methods to add, retrieve, and handle
     * the sequences encapsulated in it.
     */
    Runs,
    /**
     * The `Wickets` class represents the concept of cricket wickets which is a crucial part of the game.
     * It may be used to handle operations or maintain data related to wickets in a cricket match.
     *
     * The class can include functionalities such as tracking the number of wickets taken, managing
     * information related to different bowlers, and keeping score updates.
     *
     * Designed to be part of a cricket match management or statistics tracking system,
     * this class helps model the domain-specific concept of wickets effectively.
     */
    Wickets,
    /**
     * Represents an innings in a cricket match.
     *
     * This class provides the structure for storing and managing data related
     * to an innings in cricket, which might include information about the number
     * of runs scored, wickets lost, overs played, and other details pertinent
     * to the match's progression.
     *
     * Typically, an innings contributes to the overall match outcome either by
     * setting a total score or chasing a target. Instances of this class can be
     * used to encapsulate relevant data and operations associated with an innings.
     *
     * The class may allow updates to the innings details dynamically as the game
     * progresses and provide accessors to read information regarding the current
     * state of the innings.
     */
    Innings,
    /**
     * Represents a type of victory condition in a match where the result is tied.
     *
     * This is one of the possible enumerations in the VictoryType enum, used to indicate
     * that both competing sides have achieved an equal outcome in the match, resulting in no winner.
     */
    Tied,
    /**
     * Represents one of the victory types where a match has been abandoned.
     *
     * This enumeration value is a part of the `VictoryType` enum and is used to denote scenarios where
     * no result could be determined due to the match being called off. This might occur for reasons such
     * as inclement weather, safety concerns, or other unforeseen circumstances halting the match.
     */
    Abandoned,
    /**
     * Represents a state where a match has concluded without a definitive result,
     * often due to factors such as weather conditions or interruptions.
     * It is part of the VictoryType enumeration used for classifying match outcomes.
     */
    NoResult,
    /**
     * Represents a victory type in a cricket match based on the calculation of the run rate.
     *
     * This enumeration member is used to depict scenarios where the victory is determined
     * by comparing the run rates of the competing teams. It is primarily utilized within
     * the context of determining match results and handling specific cases aligning with
     * run rate calculations.
     */
    RunRate,
    /**
     * Represents a victory type indicating that a team lost while conceding fewer wickets than their opponent.
     *
     * This enum value is used in scenarios where the match result highlights the number of wickets lost
     * as a determining factor for the outcome. It emphasizes a situation where the losing team performed better
     * in terms of preserving wickets compared to the winning team.
     */
    LosingFewerWickets,
    /**
     * Represents a victory type determined by a faster scoring rate.
     * This victory type indicates that a team achieved a better scoring rate during
     * the match, which was used as a determining factor for the result.
     *
     * It is part of the broader VictoryType enumeration that encapsulates various
     * methods by which a team can achieve or define outcomes in a match context.
     */
    FasterScoringRate,
    /**
     * The `Unknown` class serves as a placeholder or representation for unspecified or undefined functionality.
     *
     * This class can be utilized in scenarios where specific behavior or data has yet to be determined.
     * Its purpose is to act as a flexible and adaptable entity, allowing developers to define custom
     * implementations or extend its functionality based on the requirements.
     */
    Unknown
}

/**
 * Represents a data transfer object for a location.
 *
 * This class is used to encapsulate information about a specific location,
 * typically associated with a cricket match or sporting event.
 *
 * @property key The unique identifier for the location.
 * @property name The descriptive name of the location.
 */
@Serializable
data class LocationDto(
    val key: Int,
    val name: String
)

/**
 * Represents the summary of the close of play for a specific day in a match.
 *
 * This data class is used to encapsulate the details of the close of play,
 * including the day of the match and any related notes or comments about the day's play.
 *
 * @property day Indicates the day of the match for which the close of play information is recorded.
 * @property note Provides additional information or context about the events or status at the close of play for the specified day.
 */
@Serializable
data class CloseOfPlayDto(
    val day: Int,
    val note: String
)

/**
 * Represents the debut details of a player in a sports context.
 *
 * @property playerId Unique identifier for the player.
 * @property fullName Full name of the player.
 * @property teamId Unique identifier for the team the player belongs to.
 * @property teamName Name of the team the player belongs to.
 */
@Serializable
data class DebutDto(
    val playerId: Int,
    val fullName: String,
    val teamId: Int,
    val teamName: String
)

/**
 * This data class represents the result of a match, including information about the winning and losing teams,
 * the result description, and the type of victory achieved.
 *
 * @property whoWon The team that won the match, represented by a `ScorecardTeamDto`. It can be null if no team won.
 * @property whoLost The team that lost the match, represented by a `ScorecardTeamDto`. It can be null if no team lost.
 * @property resultString A string describing the result of the match in detail.
 * @property victoryType The type of victory achieved in the match, represented by the `VictoryType` enum.
 */
@Serializable
data class ResultDto(
    val whoWon: ScorecardTeamDto?,
    val whoLost: ScorecardTeamDto?,
    val resultString: String,
    val victoryType: VictoryType
)

/**
 * Represents the details of an inning in a cricket scorecard.
 *
 * @property team The team playing this inning as represented by ScorecardTeamDto.
 * @property opponents The opposing team for this inning as represented by ScorecardTeamDto.
 * @property inningsNumber The number indicating which inning this is in the match.
 * @property inningsOrder The order of the inning in the context of all innings played.
 * @property declared Indicates if the inning was declared closed by the batting team.
 * @property complete Indicates if the inning is completed.
 * @property total The total score details of the inning as represented by TotalDto.
 * @property extras The additional runs (e.g., byes, leg-byes) as represented by ExtrasDto.
 * @property battingLines The list of batting performance details for the inning as represented by BattingLineDto.
 * @property bowlingLines The list of bowling performance details for the inning as represented by BowlingLineDto.
 * @property fallOfWickets The list of details for each wicket that fell during the inning as represented by FallOfWicketDto.
 * @property partnershipDetails The details of partnerships formed during the inning as represented by PartnershipDetailsDto.
 */
@Serializable
data class InningDto(
    val team: ScorecardTeamDto,
    val opponents: ScorecardTeamDto,
    val inningsNumber: Int,
    val inningsOrder: Int,
    val declared: Boolean,
    val complete: Boolean,
    val total: TotalDto,
    val extras: ExtrasDto,
    val battingLines: List<BattingLineDto>,
    val bowlingLines: List<BowlingLineDto>,
    val fallOfWickets: List<FallOfWicketDto>,
    val partnershipDetails: List<PartnershipDetailsDto>
)

/**
 * Represents a team in a scorecard with basic identifying information.
 *
 * This data transfer object is commonly used in various contexts within a scorecard,
 * such as representing the home team, away team, toss winner, or result details.
 *
 * @property key A unique identifier for the team.
 * @property name The name of the team.
 */
@Serializable
data class ScorecardTeamDto(
    val key: Int,
    val name: String
)

/**
 * Represents a person with a unique identifier and a name.
 *
 * This data transfer object is used in various cricket-related contexts such as matches,
 * scorecard details, and player-specific statistics.
 *
 * The `key` property uniquely identifies the person, while the `name` property
 * holds the name of the person. This object is serialized for persistent
 * storage or network transmission and used in multiple data models.
 */
@Serializable
data class PersonDto(
    val key: Int,
    val name: String
)

/**
 * Data transfer object representing the total score of an innings in a cricket match.
 *
 * @property wickets The number of wickets lost.
 * @property declared Indicates whether the innings has been declared.
 * @property overs The overs played in the innings, represented as a string (e.g., "50.3" for 50 overs and 3 balls).
 * @property minutes The total time in minutes for which the innings lasted.
 * @property total The total runs scored in the innings.
 */
@Serializable
data class TotalDto(
    val wickets: Int,
    val declared: Boolean,
    val overs: String,
    val minutes: Int,
    val total: Int
)

/**
 * Data model representing the extras in a cricket innings scorecard.
 *
 * @property byes Number of runs scored as byes.
 * @property legByes Number of runs scored as leg-byes.
 * @property wides Number of runs scored through wide deliveries.
 * @property noBalls Number of runs given as no-balls.
 * @property pens Number of penalty runs awarded.
 * @property total Total number of extras in the innings.
 */
@Serializable
data class ExtrasDto(
    val byes: Int,
    val legByes: Int,
    val wides: Int,
    val noBalls: Int,
    val pens: Int,
    val total: Int
)

/**
 * Represents the batting performance of a player in a cricket match.
 *
 * @property player The player whose batting performance this represents.
 * @property runs The number of runs scored by the player, or null if not available.
 * @property balls The number of balls faced by the player, or null if not available.
 * @property fours The number of fours hit by the player, or null if not available.
 * @property sixes The number of sixes hit by the player, or null if not available.
 * @property notOut Indicates whether the player remained not out.
 * @property minutes The time in minutes the player spent batting, or null if not available.
 * @property position The batting position of the player in the order.
 * @property dismissal Details regarding the player's dismissal, including type and involved players.
 * @property isCaptain Indicates whether the player was the captain during their innings.
 * @property isWicketKeeper Indicates whether the player was the wicketkeeper during their innings.
 */
@Serializable
data class BattingLineDto(
    val player: PersonDto,
    val runs: Int?,
    val balls: Int?,
    val fours: Int?,
    val sixes: Int?,
    val notOut: Boolean,
    val minutes: Int?,
    val position: Int,
    val dismissal: DismissalDto,
    val isCaptain: Boolean,
    val isWicketKeeper: Boolean
)

/**
 * Data transfer object representing the details of a player's dismissal in a cricket match.
 *
 * @property dismissalType An integer representing the type of dismissal, typically corresponding to a specific dismissal category (e.g., bowled, caught, run out, etc.).
 * @property dismissal A string describing the dismissal, providing more details or context about how the player was dismissed.
 * @property bowler The bowler involved in the dismissal, encapsulated in a [PersonDto].
 * @property fielder The fielder involved in the dismissal, encapsulated in a [PersonDto]. For dismissals that do not involve a fielder, this field may still hold a value for additional
 *  context.
 */
@Serializable
data class DismissalDto(
    val dismissalType: Int,
    val dismissal: String,
    val bowler: PersonDto,
    val fielder: PersonDto
)

/**
 * Represents detailed statistics of a bowler's performance in an inning.
 *
 * @property dots The number of dot balls bowled by the bowler.
 * @property runs The number of runs conceded by the bowler.
 * @property balls The total number of balls bowled by the bowler.
 * @property fours The number of boundaries (fours) conceded by the bowler.
 * @property sixes The number of sixes conceded by the bowler.
 * @property overs The number of overs bowled by the bowler, formatted as a string.
 * @property wides The number of wide balls delivered.
 * @property player The player represented by the bowler, containing player-specific details.
 * @property maidens The number of maiden overs bowled by the bowler.
 * @property noBalls The number of no-ball deliveries bowled by the bowler.
 * @property wickets The number of wickets taken by the bowler.
 * @property isCaptain Indicates whether the bowler is the team's captain.
 */
@Serializable
data class BowlingLineDto(
    val dots: Int?,
    val runs: Int?,
    val balls: Int?,
    val fours: Int?,
    val sixes: Int?,
    val overs: String,
    val wides: Int?,
    val player: PersonDto,
    val maidens: Int?,
    val noBalls: Int?,
    val wickets: Int?,
    val isCaptain: Boolean
)

/**
 * Represents the details of a fall of wicket during a cricket match.
 *
 * @property wicket The number of the wicket that fell (e.g., 1 for first wicket).
 * @property score The team score at the time the wicket fell. Nullable in cases where the score information is unavailable.
 * @property player The batsman who got out at this wicket.
 * @property overs The over and ball at which the wicket fell, represented as a string (e.g., "15.3" for 15 overs and 3 balls).
 */
@Serializable
data class FallOfWicketDto(
    val wicket: Int,
    val score: Int?,
    val player: PersonDto,
    val overs: String
)

/**
 * Data Transfer Object representing the details of a batting partnership in a cricket match.
 *
 * @property wicket The number of the wicket at which the partnership occurred.
 * @property playerNames The names of the players involved in the partnership.
 * @property partnership The total number of runs scored in the partnership.
 * @property unbroken Indicates if the partnership was unbroken (true) or not (false).
 * @property team The name of the team involved in the partnership.
 * @property opponents The name of the opposing team.
 * @property previousScore The team's score before the partnership began.
 * @property previousWicket The number of wickets fallen before the partnership began.
 * @property multiple Indicates if multiple partnerships for the same wicket exist (true) or not (false).
 * @property partial Indicates if the partnership details are partial or incomplete (true) or not (false).
 */
@Serializable
data class PartnershipDetailsDto(
    val wicket: Int,
    val playerNames: String,
    val partnership: Int,
    val unbroken: Boolean,
    val team: String,
    val opponents: String,
    val previousScore: Int,
    val previousWicket: Int,
    val multiple: Boolean,
    val partial: Boolean
)

