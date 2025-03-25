using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using NewwApp.Models;
using System.Collections.Generic;
using System.Text.Json;

namespace NewwApp.Services
{
    public class BudgetService
    {
        private readonly HttpClient _httpClient;

        public BudgetService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<List<Budget>> GetAllBudgetsAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync("http://localhost:8080/api/budgets/all-budgets");
                var responseContent = await response.Content.ReadAsStringAsync(); // Lire le contenu de la réponse
        
                // Vérifiez le code de statut de la réponse
                if (!response.IsSuccessStatusCode)
                {
                    throw new Exception($"Erreur lors de l'appel à l'API : {response.StatusCode} - {responseContent}");
                }

                // Affichez le contenu de la réponse pour le débogage
                Console.WriteLine(responseContent); // Pour le débogage

                // Désérialisation avec vérification
                var budgets = JsonSerializer.Deserialize<List<Budget>>(responseContent, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
                return budgets;
            }
            catch (HttpRequestException e)
            {
                // Gérer l'exception ici (par exemple, journaliser l'erreur)
                throw new Exception("Erreur lors de l'appel à l'API : " + e.Message);
            }
            catch (JsonException jsonEx)
            {
                throw new Exception("Erreur de désérialisation JSON : " + jsonEx.Message);
            }
        }
        // ... code existant ...
        public async Task<decimal> GetTotalBudgetsAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync("http://localhost:8080/api/budgets/total-amount"); // Assurez-vous que cette API existe
                var responseContent = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                {
                    throw new Exception($"Erreur lors de l'appel à l'API : {response.StatusCode} - {responseContent}");
                }

                return JsonSerializer.Deserialize<decimal>(responseContent);
            }
            catch (HttpRequestException e)
            {
                throw new Exception("Erreur lors de l'appel à l'API : " + e.Message);
            }
            catch (JsonException jsonEx)
            {
                throw new Exception("Erreur de désérialisation JSON : " + jsonEx.Message);
            }
        }
    }
}