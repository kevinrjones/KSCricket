using MailKit.Net.Smtp;
using MailKit.Security;
using Microsoft.AspNetCore.Identity.UI.Services;
using MimeKit;

namespace Ids.EMail;

public class MailKitEmailSender(IConfiguration configuration, ILogger<SendGridEmailSender> logger)
    : IEmailSender
{

    public async Task SendEmailAsync(string email, string subject, string htmlMessage)
    {
        try
        {
            var smtpServer = configuration["MailKit:SmtpServer"];
            var port = int.Parse(configuration["MailKit:Port"]);
            var user = configuration["MailKit:UserName"];
            var fromMail = configuration["MailKit:From"];
            var password = configuration["MailKit:Password"];

            var emailMessage = new MimeMessage();
            emailMessage.From.Add(new MailboxAddress("no-reply@knowledgespike.com", fromMail));
            emailMessage.To.Add(new MailboxAddress(email, email));
            emailMessage.Subject = subject;
            emailMessage.Body = new TextPart("html") { Text = htmlMessage };

            using var smtp = new SmtpClient();
            await smtp.ConnectAsync(smtpServer, port, SecureSocketOptions.StartTls);
            await smtp.AuthenticateAsync(user, password);
            await smtp.SendAsync(emailMessage);
            await smtp.DisconnectAsync(true);
            
        }
        catch (Exception ex)
        {
            // Log the exception (not done here for simplicity)
            logger.LogError(ex, "Error sending email");
        }    
    }
}