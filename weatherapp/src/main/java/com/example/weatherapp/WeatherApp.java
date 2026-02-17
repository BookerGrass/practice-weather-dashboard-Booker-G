import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;



public class WeatherApp {

    private static final String API_KEY = "fb3f51dfa76224c9432f0163955ce1bb";
    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Morning Routine Weather Dashboard ====");
            System.out.println("1. Check New York");
            System.out.println("2. Check Los Angeles");
            System.out.println("3. Check Chicago");
            System.out.println("4. Enter Custom City");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> getWeather("New York");
                case 2 -> getWeather("Los Angeles");
                case 3 -> getWeather("Chicago");
                case 4 -> {
                    System.out.print("Enter city name: ");
                    String city = scanner.nextLine();
                    getWeather(city);
                }
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }



    private static void getWeather(String city) {
        try {
            // Encode the city to handle spaces and special characters
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String url = BASE_URL + "?q=" + encodedCity
                    + "&appid=" + API_KEY
                    + "&units=imperial";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Error fetching weather data: " + response.body());
                return;
            }

            JSONObject json = new JSONObject(response.body());

            double temperature = json.getJSONObject("main").getDouble("temp");
            int humidity = json.getJSONObject("main").getInt("humidity");
            String description = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");

            displayDashboard(city, temperature, description, humidity);

        } catch (Exception e) {
            System.out.println("Failed to fetch weather: " + e.getMessage());
        }
    }


    private static void displayDashboard(String city,
                                         double temp,
                                         String description,
                                         int humidity) {

        System.out.println("\n==== Weather Summary for " + city + " ====");
        System.out.println("Temperature: " + temp + "°F");
        System.out.println("Condition: " + description);
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("=========================================");
    }
}
