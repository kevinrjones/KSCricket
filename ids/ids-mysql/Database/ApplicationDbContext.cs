using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace ids.Database;

public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
    {
    }
}

public class ApplicationUser : IdentityUser
{
    public ApplicationUser()
    {
        FirstName = "";
        LastName = "";
    }

    public ApplicationUser(string userName) : base(userName)
    {
        FirstName = "";
        LastName = "";
    }

    public bool IsBlocked { get; set; }
    public bool IsDeleted { get; set; }
    public string FirstName { get; set; }
    public string LastName { get; set; }
}