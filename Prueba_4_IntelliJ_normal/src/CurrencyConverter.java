import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CurrencyConverter {

    private static final String API_KEY = "673e9e213176d56a4a9eb385";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";
    private static final String[] CURRENCIES = {"USD", "EUR", "PEN"};
    private static final String[] CURRENCY_NAMES = {"Dólar Estadounidense", "Euro", "Sol Peruano"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean keepRunning = true;
        System.out.println("---------------------------------------");
        System.out.println("Sea Bienvenido/a al Conversor de Moneda");
        while (keepRunning) {
            keepRunning = displayMenu(scanner);
        }
        scanner.close();
    }

    private static boolean displayMenu(Scanner scanner) {
        System.out.println("---------------------------------------");
        System.out.println("Seleccione una opción:");
        int optionCounter = 1;

        for (int i = 0; i < CURRENCIES.length; i++) {
            for (int j = 0; j < CURRENCIES.length; j++) {
                if (i != j) {
                    System.out.printf("%d. %s a %s%n", optionCounter, CURRENCY_NAMES[i], CURRENCY_NAMES[j]);
                    optionCounter++;
                }
            }
        }
        System.out.println(optionCounter + ". Salir");
        System.out.println("---------------------------------------");

        int choice = scanner.nextInt();
        if (choice == optionCounter) return false;

        System.out.println("Ingrese la cantidad: ");
        double amount = scanner.nextDouble();

        int fromIndex = (choice - 1) / (CURRENCIES.length - 1);
        int toIndex = (choice - 1) % (CURRENCIES.length - 1);
        if (toIndex >= fromIndex) toIndex++;

        String baseCurrency = CURRENCIES[fromIndex];
        String targetCurrency = CURRENCIES[toIndex];

        double rate = fetchConversionRate(baseCurrency, targetCurrency);
        double convertedAmount = amount * rate;

        System.out.printf("La cantidad convertida es: %.2f %s%n", convertedAmount, CURRENCY_NAMES[toIndex]);
        return true;
    }

    private static double fetchConversionRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = URI.create(BASE_URL + baseCurrency).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            return json.getAsJsonObject("conversion_rates").get(targetCurrency).getAsDouble();
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}