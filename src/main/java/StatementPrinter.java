import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class StatementPrinter {

  public String print(Invoice invoice, HashMap<String, Play> plays) {
    double totalAmount = 0.0;
    int volumeCredits = 0;
    StringBuilder result = new StringBuilder();
    result.append(String.format("Statement for %s\n", invoice.customer));

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      double thisAmount = 0.0;

      switch (play.type) {
        case TRAGEDY:
          thisAmount = 400.00; // Utilisation de valeurs en dollars avec des centimes
          if (perf.audience > 30) {
            thisAmount += 10.00 * (perf.audience - 30);
          }
          break;
        case COMEDY:
          thisAmount = 300.00; // Utilisation de valeurs en dollars avec des centimes
          if (perf.audience > 20) {
            thisAmount += 100.00 + 5.00 * (perf.audience - 20);
          }
          thisAmount += 3.00 * perf.audience;
          break;
        default:
          throw new Error("unknown type: " + play.type);
      }

      volumeCredits += Math.max(perf.audience - 30, 0);
      if ( PlayType.COMEDY.equals(play.type)) {
        volumeCredits += Math.floor(perf.audience / 5);
      }

      result.append(String.format("  %s: %s (%s seats)\n", play.name, currencyFormatter.format(thisAmount), perf.audience));
      totalAmount += thisAmount;
    }
    result.append(String.format("Amount owed is %s\n", currencyFormatter.format(totalAmount)));
    result.append(String.format("You earned %s credits\n", volumeCredits));
    return result.toString();
  }
}