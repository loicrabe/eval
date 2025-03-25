namespace NewwApp.Models
{
    public class Budget
    {
        public int IdBudget { get; set; } // Identifiant du budget
        public string Titre { get; set; } = string.Empty; // Initialisation par défaut
        public string Description { get; set; } = string.Empty; // Initialisation par défaut
        public decimal Amount { get; set; } // Montant du budget
        public DateTime Date { get; set; } // Date du budget
    }
}