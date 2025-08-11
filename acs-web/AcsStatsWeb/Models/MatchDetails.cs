namespace AcsStatsWeb.Models;

public class MatchDetails
{
    public required string HomeTeam { get; set; }
    public required string AwayTeam { get; set; }
    public required string ResultString { get; set; }
    public required string DurationType { get; set; }
    public required string Tournament { get; set; }
    public required string MatchType { get; set; }
    public required int? HomeTotal1 { get; set; }
    public required int? HomeWickets1 { get; set; }
    public required string? HomeOvers1 { get; set; }
    public required bool? HomeDeclared1 { get; set; }
    public required int? AwayTotal1 { get; set; }
    public required int? AwayWickets1 { get; set; }
    public required string? AwayOvers1 { get; set; }
    public required bool? AwayDeclared1 { get; set; }
    public required int? HomeTotal2 { get; set; }
    public required int? HomeWickets2 { get; set; }
    public required string? HomeOvers2 { get; set; }
    public required bool? HomeDeclared2 { get; set; }
    public required int? AwayTotal2 { get; set; }
    public required int? AwayWickets2 { get; set; }
    public required string? AwayOvers2 { get; set; }
    public required bool? AwayDeclared2 { get; set; }
}