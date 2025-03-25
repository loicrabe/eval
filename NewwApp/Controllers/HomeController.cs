using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using NewwApp.Models;
using NewwApp.Services;
using System.Threading.Tasks;

namespace NewwApp.Controllers;

public class HomeController : Controller
{
    private readonly ILogger<HomeController> _logger;
    private readonly BudgetService _budgetService;

    public HomeController(ILogger<HomeController> logger, BudgetService budgetService)
    {
        _logger = logger;
        _budgetService = budgetService;
    }

    public IActionResult Index()
    {
        return View();
    }

    public IActionResult Privacy()
    {
        return View();
    }

    public async Task<IActionResult> Dashboard()
    {
        var budgets = await _budgetService.GetAllBudgetsAsync();
        var total = await _budgetService.GetTotalBudgetsAsync(); // Appel de la nouvelle méthode
        ViewBag.TotalBudgets = total; // Passer le total à la vue
        return View(budgets);
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}