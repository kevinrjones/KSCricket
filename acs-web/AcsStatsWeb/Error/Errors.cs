using CSharpFunctionalExtensions;

// ReSharper disable InconsistentNaming

namespace AcsStatsWeb.Error;

public class Error : ICombine
{
    public virtual string Message { get; }
    public override string ToString() => Message;

    public virtual ICombine Combine(ICombine value)
    {
        var maybeError = value as Error;    
        if (maybeError == null) return this;

        if (value is CombinedError other)
        {
            other.Add(this);
            return other;
        }


        return new CombinedError(maybeError).Add(this);
    }

    protected Error()
    {
    }

    protected Error(string Message)
    {
        this.Message = Message;
    }

    public static implicit operator Error(string m) => new(m);
}

public class CombinedError : Error
{
    public readonly List<Error> Errors = new();

    public override ICombine Combine(ICombine value)
    {
        if (value is not Error maybeError) return this;

        if (value is CombinedError other)
        {
            foreach (var error in other.Errors)
            {
                Add(error);
            }

            return this;
        }


        return Add(maybeError);
    }

    public CombinedError(Error error)
    {
        Errors.Add(error);
    }

    public CombinedError Add(Error error)
    {
        Errors.Add(error);
        return this;
    }
}

public static class Errors
{
    public static NullError NullError() => new();

    public static UnexpectedError GetUnexpectedError(string message) => new(message);

    public static InvalidId InvalidIdError(int id) => new(id);

    public static InvalidDate InvalidDateError(string startDate, string endDate) => new(startDate, endDate);

    public static InvalidData InvalidDataError(string message) => new(message);
    public static InvalidDate InvalidDateError(string date) => new(date);

    public static InvalidId InvalidIdError(string message, int id) => new(message, id);

    public static InvalidMatchType InvalidMatchTypeError() => new();

    public static InvalidDateType InvalidDateTypeError() => new();

    public static HttpError HttpError(Exception e) => new(e);

    public static ModelError ModelError(string key, string errorMessage) => new(key, errorMessage);

}

public sealed class UnexpectedError : Error
{
    public UnexpectedError(string message) : base(message)
    { }
}

public sealed class NullError : Error
{
    public override string Message { get; }
        = "Unexpected null value";
}

public sealed class InvalidId : Error
{
    int Id { get; }

    public InvalidId(int id)
    {
        Id = id;
        Message = $"Unable to create an AcsId with id {Id}";
    }

    public InvalidId(string message, int id)
    {
        Id = id;
        Message = $"{message} {Id}";
    }

    public override string Message { get; }
}

public sealed class InvalidDate : Error
{
    string date { get; }

    public InvalidDate(string date)
    {
        this.date = date;
        Message = $"Unable to parse a date with value {this.date}";
    }

    public InvalidDate(string startDate, string endDate)
    {
        Message = $"End date {endDate} cannot be before start date {startDate}";
    }


    public override string Message { get; }
}

public sealed class InvalidData : Error
{
    public InvalidData(string message)
    {
        Message = message;
    }
    
    public override string Message { get; }
}

public sealed class InvalidMatchType : Error
{
    public override string Message
        => $"You must specify a match/tournament type";
}

public sealed class InvalidDateType : Error
{
    public override string Message
        => $"You must specify a valid date";
}

public sealed class HttpError : Error
{
    private readonly Exception _e;

    public HttpError(Exception e)
    {
        _e = e;
    }

    public override string Message
        => $"{_e.Message}";
}

public sealed class ModelError : Error
{
    public string Key { get; }
    

    public ModelError(string key, string errorMessage) : base(errorMessage)
    {
        Key = key;
    }
}