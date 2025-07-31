using System;
using System.Net;
using System.Threading.Tasks;
using AcsStatsWeb.Json;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Serilog.Core;

// ReSharper disable InconsistentNaming

namespace AcsStatsWeb.Utils;

public sealed class ExceptionHandler
{
    private readonly RequestDelegate _next;
    private readonly Logger _logger;

    public ExceptionHandler(RequestDelegate next, Logger logger)
    {
        _next = next;
        _logger = logger;
    }

    public async Task Invoke(HttpContext context)
    {
        try
        {
            await _next(context);
        }
        catch (Exception ex)
        {
            await HandleExceptionAsync(context, ex);
        }
    }

    private Task HandleExceptionAsync(HttpContext context, Exception exception)
    {
        // Log exception here
        _logger.Error(exception, "Something unexpected happened");

        string result = JsonConvert.SerializeObject(Envelope.Error(exception.Message));
        context.Response.ContentType = "application/json";
        context.Response.StatusCode = (int) HttpStatusCode.InternalServerError;
        return context.Response.WriteAsync(result);
    }
}