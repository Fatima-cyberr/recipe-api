package packagee; //sends http requests to the springboot backend

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
public class ConsoleClient {
    private static final String BASE_URL = "https://recipe-api-3qty.onrender.com";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== RECIPE MANAGEMENT SYSTEM ===");
            System.out.println("1. Create Recipe");
            System.out.println("2. View All Recipes");
            System.out.println("3. View Recipe by ID");
            System.out.println("4. Update Recipe");
            System.out.println("5. Delete Recipe");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1" -> createRecipe();
                    case "2" -> getAllRecipes();
                    case "3" -> getRecipeById();
                    case "4" -> updateRecipe();
                    case "5" -> deleteRecipe();
                    case "0" -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createRecipe() throws IOException {
        System.out.println("\n=== CREATE NEW RECIPE ===");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Ingredients (comma separated): ");
        String[] ingredients = scanner.nextLine().split(",");
        
        System.out.print("Instructions: ");
        String instructions = scanner.nextLine();
        
        System.out.print("Cooking Time (minutes): ");
        int cookingTime = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Category: ");
        String category = scanner.nextLine();
        
        String jsonInput = String.format(
            "{\"title\":\"%s\",\"ingredients\":[\"%s\"],\"instructions\":\"%s\",\"cookingTime\":%d,\"category\":\"%s\"}",
            title, String.join("\",\"", ingredients), instructions, cookingTime, category
        );
        
        HttpURLConnection conn = createConnection("", "POST");
        sendRequest(conn, jsonInput);
        printResponse(conn);
    }

    private static void getAllRecipes() throws IOException {
        HttpURLConnection conn = createConnection("", "GET");
        printResponse(conn);
    }

    private static void getRecipeById() throws IOException {
        System.out.print("Enter Recipe ID: ");
        String id = scanner.nextLine();
        HttpURLConnection conn = createConnection("/" + id, "GET");
        printResponse(conn);
    }
    private static String getResponse(HttpURLConnection conn) throws IOException {
        try {
            int status = conn.getResponseCode();
            InputStream is;
            
            if (status >= 200 && status < 300) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }

            if (is == null) {
                return "{\"error\":\"No response from server\",\"status\":" + status + "}";
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } finally {
            conn.disconnect(); // Ensure connection is closed
        }
        
    }
    private static void updateRecipe() throws IOException {
        System.out.println("\n=== UPDATE RECIPE ===");
        System.out.print("Enter Recipe ID to update: ");
        String id = scanner.nextLine().trim();
        
        if (id.isEmpty()) {
            System.out.println("Error: Recipe ID cannot be empty!");
            return;
        }

        // Get existing recipe
        HttpURLConnection getConn = createConnection("/" + id, "GET");
        String existingRecipeJson = getResponse(getConn);
        
        if (existingRecipeJson.contains("404 Not Found") || existingRecipeJson.isEmpty()) {
            System.out.println("Error: Recipe not found!");
            return;
        }

        // Parse the existing recipe (simplified parsing - in production use a JSON library)
        String currentTitle = extractField(existingRecipeJson, "title");
        String currentIngredients = extractField(existingRecipeJson, "ingredients");
        String currentInstructions = extractField(existingRecipeJson, "instructions");
        String currentCookingTime = extractField(existingRecipeJson, "cookingTime");
        String currentCategory = extractField(existingRecipeJson, "category");

        // Display current values
        System.out.println("\nCurrent Recipe:");
        System.out.println("Title: " + currentTitle);
        System.out.println("Ingredients: " + currentIngredients);
        System.out.println("Cooking Time: " + currentCookingTime + " mins");
        System.out.println("Category: " + currentCategory);
        System.out.println("\nEnter new details (press Enter to keep current value):");

        // Get updated values
        System.out.print("New Title [" + currentTitle + "]: ");
        String newTitle = scanner.nextLine().trim();
        if (newTitle.isEmpty()) newTitle = currentTitle;

        System.out.print("New Ingredients (comma separated) [" + currentIngredients + "]: ");
        String ingredientsInput = scanner.nextLine().trim();
        String[] newIngredients = ingredientsInput.isEmpty() 
            ? currentIngredients.replaceAll("[\\[\\]\"]", "").split(",\\s*") 
            : ingredientsInput.split("\\s*,\\s*");

        System.out.print("New Instructions [" + currentInstructions + "]: ");
        String newInstructions = scanner.nextLine().trim();
        if (newInstructions.isEmpty()) newInstructions = currentInstructions;

        int newCookingTime;
        try {
            System.out.print("New Cooking Time (minutes) [" + currentCookingTime + "]: ");
            String cookingTimeInput = scanner.nextLine().trim();
            newCookingTime = cookingTimeInput.isEmpty() 
                ? Integer.parseInt(currentCookingTime) 
                : Integer.parseInt(cookingTimeInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid cooking time. Keeping current value.");
            newCookingTime = Integer.parseInt(currentCookingTime);
        }

        System.out.print("New Category [" + currentCategory + "]: ");
        String newCategory = scanner.nextLine().trim();
        if (newCategory.isEmpty()) newCategory = currentCategory;

        // Create JSON payload
        String jsonInput = String.format(
            "{\"title\":\"%s\",\"ingredients\":[\"%s\"],\"instructions\":\"%s\",\"cookingTime\":%d,\"category\":\"%s\"}",
            newTitle.replace("\"", "\\\""),
            String.join("\",\"", newIngredients).replace("\"", "\\\""),
            newInstructions.replace("\"", "\\\""),
            newCookingTime,
            newCategory.replace("\"", "\\\"")
        );

        // Send update request
        HttpURLConnection conn = createConnection("/" + id, "PUT");
        sendRequest(conn, jsonInput);
        printResponse(conn);
    }

    // Helper method to extract fields from JSON (simplified)
    private static String extractField(String json, String fieldName) {
        try {
            String pattern = "\"" + fieldName + "\":(?:\"([^\"]*)\"|(\\d+)|\\[([^\\]]*)\\])";
            java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = r.matcher(json);
            if (m.find()) {
                if (m.group(1) != null) return m.group(1); // String value
                if (m.group(2) != null) return m.group(2); // Numeric value
                if (m.group(3) != null) return m.group(3); // Array value
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
        return "";
    }
    private static void deleteRecipe() throws IOException {
        System.out.print("Enter Recipe ID to delete: ");
        String id = scanner.nextLine();
        HttpURLConnection conn = createConnection("/" + id, "DELETE");
        printResponse(conn);
    }

    private static HttpURLConnection createConnection(String endpoint, String method) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        
        if (method.equals("POST") || method.equals("PUT")) {
            conn.setDoOutput(true);
        }
        
        return conn;
    }

    private static void sendRequest(HttpURLConnection conn, String jsonInput) throws IOException {
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    private static void printResponse(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        InputStream is = status < 400 ? conn.getInputStream() : conn.getErrorStream();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}