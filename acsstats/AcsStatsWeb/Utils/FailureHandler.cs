using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Serilog.Core;

namespace AcsStatsWeb.Utils;

public sealed class FailureHandler
{
    private readonly RequestDelegate _next;
    private readonly Logger _logger;

    class Envelope
    {
        public string ErrorMessage { get; set; } = "";
        public string Result { get; set; } = "";
        public DateTime TimeGenerated { get; set; }
    }

    public FailureHandler(RequestDelegate next, Logger logger)
    {
        _next = next;
        _logger = logger;
    }

    public async Task Invoke(HttpContext context)
    {
        Stream originalBody = context.Response.Body;

        try
        {
            using (var memStream = new MemoryStream())
            {
                context.Response.Body = memStream;

                await _next(context);

                if (context.Response.StatusCode == StatusCodes.Status400BadRequest)
                {
                    memStream.Position = 0;
                    string responseBody = await new StreamReader(memStream).ReadToEndAsync();
                    try
                    {
                        var error = JsonConvert.DeserializeObject<Envelope>(responseBody);
                        _logger.Error("A failure was returned from a controller: {error}", error?.ErrorMessage);
                    }
                    catch (Exception)
                    {
                        Console.WriteLine("");
                    }
                }

                memStream.Position = 0;
                await memStream.CopyToAsync(originalBody);
            }
        }
        finally
        {
            context.Response.Body = originalBody;
        }
    }

    // private Task HandleExceptionAsync(HttpContext context, Exception exception)
    // {
    //     // Log exception here
    //     _logger.Error(exception, "Something unexpected happened");
    //     
    //     string result = JsonConvert.SerializeObject(Envelope.Error(exception.Message));
    //     context.Response.ContentType = "application/json";
    //     context.Response.StatusCode = (int)HttpStatusCode.InternalServerError;
    //     return context.Response.WriteAsync(result);
    // }
}