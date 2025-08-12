using Elastic.Ingest.Elasticsearch;
using Elastic.Ingest.Elasticsearch.DataStreams;
using Elastic.Serilog.Sinks;
using Elastic.Transport;
using ids_mysql;
using Serilog;
using Serilog.Events;
using Serilog.Sinks.SystemConsole.Themes;

Log.Logger = new LoggerConfiguration()
    .WriteTo.Console()
    .CreateBootstrapLogger();

Log.Information("Starting up");

try
{
    var builder = WebApplication.CreateBuilder(args);

    Console.WriteLine(
        builder.Configuration.GetConnectionString("identity"));

    var elasticSearchUser = builder.Configuration["ElasticSearch:UserName"];
    var elasticSearchPassword = builder.Configuration["ElasticSearch:Password"];
    var elasticSearchApiKey = builder.Configuration["ElasticSearch:APIKey"];
    var elasticSearchUri = builder.Configuration["ElasticSearch:URI"];

    if (elasticSearchUser == null || elasticSearchPassword == null || elasticSearchApiKey == null || elasticSearchUri == null)
    {
        throw new Exception("ElasticSearch credentials not configured. Please updates the dotnet secrets or add the appropriate parameters to the command line");
    }

    builder.Host.UseSerilog((ctx, lc) =>
    {
        lc.MinimumLevel.Debug()
            .MinimumLevel.Override("Microsoft", LogEventLevel.Warning)
            .MinimumLevel.Override("Microsoft.Hosting.Lifetime", LogEventLevel.Information)
            .MinimumLevel.Override("Microsoft.AspNetCore.Authentication", LogEventLevel.Information)
            .MinimumLevel.Override("System", LogEventLevel.Warning)
            .WriteTo.Console(
                outputTemplate:
                "[{Timestamp:HH:mm:ss} {Level}] {SourceContext}{NewLine}{Message:lj}{NewLine}{Exception}{NewLine}",
                theme: AnsiConsoleTheme.Code)
            .WriteTo.Elasticsearch(new [] { new Uri(elasticSearchUri )}, opts =>
            {
                opts.DataStream = new DataStreamName("logs", "kscricket", "acsweb");
                opts.BootstrapMethod = BootstrapMethod.Failure;
            }, transport =>
            {
                transport.Authentication(new BasicAuthentication(elasticSearchUser, elasticSearchPassword));
                transport.Authentication(new ApiKey(elasticSearchApiKey));
            })
            .Enrich.WithMachineName()
            .Enrich.WithProcessId()
            .Enrich.WithProcessName()
            .Enrich.WithThreadId()
            .Enrich.WithCorrelationId()
            .Enrich.FromLogContext();
    });
    

    var app = builder
        .ConfigureServices()
        .ConfigurePipeline();

    app.Run();
}
catch (HostAbortedException)
{
    // this is expected in certain situations = will see it when creating migrations
    // https://github.com/dotnet/efcore/issues/29809#issuecomment-1344101370
}
catch (Exception ex)
{
    Log.Fatal(ex, "Unhandled exception");
}
finally
{
    Log.Information("Shut down complete");
    Log.CloseAndFlush();
}