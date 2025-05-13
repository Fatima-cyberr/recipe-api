package Client;

import java.io.*;


import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ConsoleClient{

    @SuppressWarnings("unused")//for base-url error handling 
	private static final String BASE_URL = "http://localhost:8080/api/recipes";
    private static Scanner scanner = new Scanner(System.in);
static String usertype="user";
    public static void main(String[] args) {
        System.out.println("Are you a Client (C)or an Admin(A)");
        String ans=scanner.nextLine();
        if (ans.equalsIgnoreCase("A"))
        		{System.out.println("Enter the secret code please(A.....)");
        		String code=scanner.nextLine();
        		if (!code.equalsIgnoreCase("A001"))
        		{
        			System.out.println("Code entered is wrong.");
        			System.out.println("Exiting the system...");
        			return;
        		}
        		usertype="admin";
        		}
     
        while (true) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                handleUserChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
    }

    private static void printMenu() {
        
    	System.out.println("1. List All Recipes");
        System.out.println("2. Get Recipe by ID");
        System.out.println("3. Get Recipe by category");
        System.out.println("4. Get Recipe by ingredient");
        if (usertype.equals("admin")) {
            System.out.println("5. Create Recipe");
            System.out.println("6. Update Recipe");
            System.out.println("7. Delete Recipe");
        }
        
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleUserChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                displayRecipes();
                break;
            case 2:
                getRecipeById();
                break;
            case 3: getRecipebycat();break;
            case 4: getrecipebyingre();break;
            case 5:
                if (usertype.equals("admin")) {
                    createRecipe();
                } else {
                    System.out.println("Admin only can access.");
                }
                break;
            case 6:
                if (usertype.equals("admin")) {
                    updateRecipe();
                } else {
                    System.out.println("Admin only can access..");
                }
                break;
            case 7:
                if (usertype.equals("admin")) {
                    deleteRecipe();
                } else {
                    System.out.println("Admin only can access..");
                }
                break;
            case 0:
                System.out.println("exiting the system..");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }
    
    private static void getrecipebyingre() throws UnsupportedEncodingException {
    	System.out.println("Enter an ingredient:");
		String ing=scanner.nextLine();
		String encodedIng = URLEncoder.encode(ing, StandardCharsets.UTF_8.toString());
		System.out.println("Searching for ingredient: '" + ing + "'");

		 try {
			 
	            @SuppressWarnings("deprecation")
	            URL url = new URL("http://localhost:8080/recipes/ingredients/"+encodedIng);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            

	            if (connection.getResponseCode() == 200) {
	             
	            	StringBuilder response = new StringBuilder();
	                try (BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()))) {
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        response.append(line);
	                    }
	                }
	            } else {
	                System.err.println("Error: " + connection.getResponseCode());
	            }
	        } catch (Exception e) {
	            System.err.println("Failed to fetch recipes:");
	            e.printStackTrace();
	        }
	}

	private static void getRecipebycat() {
		System.out.println("Enter a category:");
		String cat=scanner.nextLine().trim();
		 try {
			 
	            @SuppressWarnings("deprecation")
	            URL url = new URL("http://localhost:8080/recipes/category/"+cat);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            if (connection.getResponseCode() == 200) {
	             
	                
	                try (BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()))) {
	                    
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        System.out.println(line);
	                    }
	                }
	            } else {
	                System.err.println("Error: " + connection.getResponseCode());
	            }
	        } catch (Exception e) {
	            System.err.println("Failed to fetch recipes:");
	            e.printStackTrace();
	        }
	}
		
	

	private static void deleteRecipe() {
	    System.out.println("Enter recipe ID to delete:");
	    String id = scanner.nextLine();

	    try {
	        @SuppressWarnings("deprecation")
			URL url = new URL("http://localhost:8080/recipes/" + id);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("DELETE");
	        connection.setRequestProperty("Accept", "application/json");

	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()))) {
	                System.out.println("Server response: " + br.readLine());
	            }
	        } else {
	        	if (responseCode==500)
	        	{
	        		System.out.println("Recipe is successfully deleted");
	        		
	        	}
	        	
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(connection.getErrorStream()))) {
	                br.lines().forEach(System.err::println);
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Failed to delete recipe:");
	        e.printStackTrace();
	    }
	}

	private static void updateRecipe() {
	    try {
	        // 1. Get recipe ID to update
	        System.out.println("Enter recipe ID to update:");
	        String id = scanner.nextLine();

	        // 2. Fetch existing recipe (optional: show current values)
	        System.out.println("Fetching current recipe details...");
	        URL getUrl = new URL("http://localhost:8080/recipes/" + id);
	        HttpURLConnection getConnection = (HttpURLConnection) getUrl.openConnection();
	        getConnection.setRequestMethod("GET");
	        
	        if (getConnection.getResponseCode() != 200) {
	            System.err.println("Recipe not found or error fetching details");
	            return;
	        }

	        // 3. Collect updated fields from user
	        System.out.println("Leave blank to keep current value");
	        
	        System.out.println("Enter new title:");
	        String title = scanner.nextLine();
	        
	        System.out.println("Enter new ingredients (comma separated):");
	        String ingredientsInput = scanner.nextLine();
	        
	        System.out.println("Enter new instructions:");
	        String instructions = scanner.nextLine();
	        
	        System.out.println("Enter new category:");
	        String category = scanner.nextLine();
	        
	        System.out.println("Enter new cooking time (minutes):");
	        String cookingTimeInput = scanner.nextLine();
	        Integer cookingTime = cookingTimeInput.isEmpty() ? null : Integer.parseInt(cookingTimeInput);

	        // 4. Build JSON string manually
	        StringBuilder jsonBuilder = new StringBuilder("{");
	        boolean firstField = true;

	        if (!title.isEmpty()) {
	            jsonBuilder.append("\"title\":\"").append(title).append("\"");
	            firstField = false;
	        }

	        if (!ingredientsInput.isEmpty()) {
	            if (!firstField) jsonBuilder.append(",");
	            jsonBuilder.append("\"ingredients\":[");
	            String[] ingredients = ingredientsInput.split("\\s*,\\s*");
	            for (int i = 0; i < ingredients.length; i++) {
	                if (i > 0) jsonBuilder.append(",");
	                jsonBuilder.append("\"").append(ingredients[i]).append("\"");
	            }
	            jsonBuilder.append("]");
	            firstField = false;
	        }

	        if (!instructions.isEmpty()) {
	            if (!firstField) jsonBuilder.append(",");
	            jsonBuilder.append("\"instructions\":\"").append(instructions).append("\"");
	            firstField = false;
	        }

	        if (!category.isEmpty()) {
	            if (!firstField) jsonBuilder.append(",");
	            jsonBuilder.append("\"category\":\"").append(category).append("\"");
	            firstField = false;
	        }

	        if (cookingTime != null) {
	            if (!firstField) jsonBuilder.append(",");
	            jsonBuilder.append("\"cookingTime\":").append(cookingTime);
	        }

	        jsonBuilder.append("}");
	        String jsonPayload = jsonBuilder.toString();

	        // 5. Send PATCH request
	        URL url = new URL("http://localhost:8080/recipes/" + id);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("PATCH");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoOutput(true);

	        try (OutputStream os = connection.getOutputStream();
	             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
	            osw.write(jsonPayload);
	        }

	        // 6. Handle response
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()))) {
	                System.out.println("Recipe updated successfully:");
	                br.lines().forEach(System.out::println);
	            }
	        } else {
	            if (responseCode==500)
	            	System.out.println("Recipe is deleted successfully");
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(connection.getErrorStream()))) {
	                br.lines().forEach(System.err::println);
	            }
	        }
	    } catch (NumberFormatException e) {
	        System.err.println("Invalid cooking time format. Please enter a number.");
	    } catch (Exception e) {
	        System.err.println("Failed to update recipe:");
	        e.printStackTrace();
	    }
	}
	private static void getRecipeById() {
		System.out.println("Enter recipe ID:");
		String idd=scanner.nextLine();
		 try {
			 
	            @SuppressWarnings("deprecation")
	            URL url = new URL("http://localhost:8080/recipes/"+idd);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            
	            if (connection.getResponseCode() == 200) {
	                System.out.println("Recipes available in mongodb sorted");
	                
	                try (BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()))) {
	                    
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        System.out.println(line);
	                    }
	                }
	            } else {
	                System.err.println("Error: " + connection.getResponseCode());
	            }
	        } catch (Exception e) {
	            System.err.println("Failed to fetch recipes:");
	            e.printStackTrace();
	        }
	    }
	public static void displayRecipes() {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("http://localhost:8080/recipes/sorted");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            if (connection.getResponseCode() == 200) {
                System.out.println("Recipes available in mongodb sorted");
                
                try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } else {
                System.err.println("Error: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch recipes:");
            e.printStackTrace();
        }
    }
	
	private static void createRecipe() {
	    try {
	        // 1. Collect recipe data from user
	        System.out.println("Enter recipe title:");
	        String title = scanner.nextLine();
	        
	        System.out.println("Enter ingredients (comma separated):");
	        String ingredientsInput = scanner.nextLine();
	        List<String> ingredients = Arrays.asList(ingredientsInput.split("\\s*,\\s*"));
	        
	        System.out.println("Enter instructions:");
	        String instructions = scanner.nextLine();
	        
	        System.out.println("Enter category:");
	        String category = scanner.nextLine();
	        
	        System.out.println("Enter cooking time (minutes):");
	        int cookingTime = Integer.parseInt(scanner.nextLine());

	        // 2. Manually build JSON string
	        String jsonPayload = String.format(
	            "{\"title\":\"%s\",\"ingredients\":[\"%s\"],\"instructions\":\"%s\",\"category\":\"%s\",\"cookingTime\":%d}",
	            title.replace("\"", "\\\""), // Escape quotes
	            String.join("\",\"", ingredients).replace("\"", "\\\""), // Escape quotes
	            instructions.replace("\"", "\\\""), // Escape quotes
	            category.replace("\"", "\\\""), // Escape quotes
	            cookingTime
	        );

	        // 3. Configure HTTP connection
	        @SuppressWarnings("deprecation")
			URL url = new URL("http://localhost:8080/recipes");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoOutput(true);

	        try (OutputStream os = connection.getOutputStream()) {
	            os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
	        }

	        // 5. Handle response
	        int status = connection.getResponseCode();
	        if (status == HttpURLConnection.HTTP_CREATED) {
	            try (BufferedReader br = new BufferedReader(
	                new InputStreamReader(connection.getInputStream()))) {
	                System.out.println("Success! Created recipe:");
	                br.lines().forEach(System.out::println);
	            }
	        } else {
	            if (status==500 && status==200)
	            {
	            	System.out.println("Recipe is successfully saved to mongoDB");
	            }
	            else
	        	System.err.println("Error: " + status);
	        }
	    } catch (Exception e) {
	        System.err.println("Failed to create recipe:");
	        e.printStackTrace();
	    }
	}}