
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.*;


abstract class Customer {
    protected String name;
    protected ArrayList<Order> orderHistory;

    public Customer(String name) {
        this.name = name;
        this.orderHistory = new ArrayList<>();
    }

    public String getName() { return name; }
    public ArrayList<Order> getOrderHistory() { return orderHistory; }
    public abstract boolean isVIP();

    public Customer upgradeToVIP() {
        return new VIPCustomer(name);
    }
}

class VIPCustomer extends Customer {
    public VIPCustomer(String name) {
        super(name);
    }

    @Override
    public boolean isVIP() {
        return true;
    }
}


class RegularCustomer extends Customer {
    public RegularCustomer(String name) {
        super(name);
    }

    @Override
    public boolean isVIP() {
        return false;
    }
}

class CartItem {
    private Food Food;
    private int quantity;

    public CartItem(Food Food, int quantity) {
        this.Food = Food;
        setQuantity(quantity);
    }

    public Food getFood() { return Food; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return Food.getName() + " x" + quantity;
    }
}


class Order implements Comparable<Order> {
    private static int orderCounter = 1;
    private int orderNumber;
    private Customer customer;
    private List<CartItem> items;
    private String status;
    private String specialRequest;

    public Order(Customer customer, List<CartItem> items, String specialRequest) {
        this.orderNumber = orderCounter++;
        this.customer = customer;
        this.items = items;
        this.specialRequest = specialRequest;
        this.status = "Order Received";
    }

    public int getOrderNumber() { return orderNumber; }
    public Customer getCustomer() { return customer; }
    public List<CartItem> getItems() { return items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Order #").append(orderNumber)
                .append(" - ").append(customer.getName())
                .append(" (").append(customer.isVIP() ? "VIP" : "Regular").append(")\n");
        for (CartItem item : items) {
            orderDetails.append(item.getFood().getName())
                    .append(" x").append(item.getQuantity()).append("\n");
        }
        orderDetails.append("Status: ").append(status);
        return orderDetails.toString();
    }

    @Override
    public int compareTo(Order other) {
        if (this.customer.isVIP() && !other.customer.isVIP()) {
            return -1;
        } else if (!this.customer.isVIP() && other.customer.isVIP()) {
            return 1;
        } else {

            return 0;
        }
    }

}

class Review {
    private String customerName;
    private String comment;

    public Review(String customerName, String comment) {
        this.customerName = customerName;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return customerName + ": " + comment;
    }
}


public class ByteMe {
    static TreeMap<String, Food> menu = new TreeMap<>();
    private static PriorityQueue<Order> orderQueue = new PriorityQueue<>();
    private static ArrayList<Order> orderHistory = new ArrayList<>();
    private static HashMap<String, List<Review>> reviews = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static HashMap<String, Customer> customers = new HashMap<>();

    public static void main(String[] args) {

        loadMenu();
        loadOrders();

        while (true) {
            System.out.println("\nWelcome to Byte Me!");
            System.out.println("1. Admin Interface");
            System.out.println("2. Customer Interface");
            System.out.println("3. Run GUI");
            System.out.println("4. Exit");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    adminInterface();
                    break;
                case 2:
                    customerInterface();
                    break;
                case 3:
                    Application.launch(ByteMeGUI.class); // Launch the JavaFX GUI
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    private static void adminInterface() {
        while (true) {
            System.out.println("\nAdmin Interface");
            System.out.println("1. Manage Menu");
            System.out.println("2. Manage Orders");
            System.out.println("3. Generate Daily Sales Report");
            System.out.println("4. Back to Main Menu");
            int choice = getIntInput("Enter your choice: ");



            switch (choice) {
                case 1:
                    manageMenu();
                    break;
                case 2:
                    manageOrders();
                    break;
                case 3:
                    generateReport();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void manageMenu() {
        while (true) {
            System.out.println("\nMenu Management");
            System.out.println("1. Add Food Item");
            System.out.println("2. Update Food Item");
            System.out.println("3. Remove Food Item");
            System.out.println("4. View Menu");
            System.out.println("5. Back");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addFood();
                    break;
                case 2:
                    updateFood();
                    break;
                case 3:
                    removeFood();
                    break;
                case 4:
                    viewMenu();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void saveCartToFile(List<CartItem> cart) {
        try (FileWriter writer = new FileWriter("cart.txt")) {
            for (CartItem item : cart) {
                writer.write(item.getFood().getName() + "," + item.getQuantity() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving cart: " + e.getMessage());
        }
    }

    private static List<CartItem> loadCartFromFile() {
        List<CartItem> cart = new ArrayList<>();
        File cartFile = new File("cart.txt");

        if (!cartFile.exists()) {
            return cart;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String itemName = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                Food food = menu.get(itemName);

                if (food != null && food.isAvailable()) {
                    cart.add(new CartItem(food, quantity));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading cart: " + e.getMessage());
        }

        return cart;
    }


    private static void clearCartFile() {
        try (FileWriter writer = new FileWriter("cart.txt", false)) {
            // Overwrite file with an empty string
        } catch (IOException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }
    private static void saveMenu() {
        try (PrintWriter writer = new PrintWriter("menu.txt")) {
            for (Food item : menu.values()) {
                writer.println(item.getName() + "," + item.getPrice() + "," + item.getCategory() + "," + item.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error saving menu data: " + e.getMessage());
        }
    }


    private static void loadMenu() {
        try (BufferedReader reader = new BufferedReader(new FileReader("menu.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                String category = parts[2];
                boolean available = Boolean.parseBoolean(parts[3]);
                menu.put(name, new Food(name, price, category, available));
            }
            System.out.println("Menu loaded: " + menu.values()); // Debugging

        } catch (IOException e) {
            System.out.println("Error loading menu, adding default items.");
            menu.put("burger", new Food("burger", 50.0, "snacks", true));
            menu.put("pizza", new Food("pizza", 200.0, "main course", true));
            menu.put("water", new Food("water", 20.0, "beverages", true));
        }
    }



    private static void saveOrders() {
        try (PrintWriter writer = new PrintWriter("orders.txt")) {
            for (Order order : orderQueue) {
                StringBuilder orderData = new StringBuilder();
                orderData.append(order.getOrderNumber()).append(",")
                        .append(order.getCustomer().getName()).append(",")
                        .append(order.getStatus()).append(",");
                for (CartItem item : order.getItems()) {
                    orderData.append(item.getFood().getName()).append(" x").append(item.getQuantity()).append(";");
                }
                writer.println(orderData);
            }
        } catch (IOException e) {
            System.out.println("Error saving order data: " + e.getMessage());
        }
    }


    private static void loadOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int orderNumber = Integer.parseInt(parts[0]);
                String customerName = parts[1];
                String status = parts[2];
                String[] itemsData = parts[3].split(";");
                List<CartItem> items = new ArrayList<>();
                for (String itemData : itemsData) {
                    String[] itemParts = itemData.split(" x");
                    String itemName = itemParts[0];
                    int quantity = Integer.parseInt(itemParts[1]);
                    items.add(new CartItem(menu.get(itemName), quantity));
                }
                Customer customer = customers.getOrDefault(customerName, new RegularCustomer(customerName));
                Order order = new Order(customer, items, "");
                order.setStatus(status);
                orderQueue.add(order);
            }
        } catch (IOException e) {
            System.out.println("Error loading order data: " + e.getMessage());
        }
    }

    private static void addFood() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        double price = getDoubleInput("Enter price: ");
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        boolean available = getBooleanInput("Is it available? (true/false): ");

        menu.put(name, new Food(name, price, category, available));
        saveMenu();
        System.out.println("Food item added to the menu.");
    }

    private static void updateFood() {
        System.out.print("Enter item name to update: ");
        String name = scanner.nextLine();
        if (menu.containsKey(name)) {
            Food item = menu.get(name);
            item.setPrice(getDoubleInput("Enter new price: "));
            item.setAvailable(getBooleanInput("Is it available? (true/false): "));
            saveMenu();
            System.out.println("Food item updated.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void removeFood() {
        System.out.print("Enter item name to remove: ");
        String name = scanner.nextLine();
        if (menu.remove(name) != null) {
            saveMenu();
            System.out.println("Food item removed from the menu.");

            List<Order> updatedOrders = new ArrayList<>();
            while (!orderQueue.isEmpty()) {
                Order order = orderQueue.poll();
                if (orderContainsItem(order, name) && order.getStatus().equals("Order Received")) {
                    order.setStatus("Denied");
                }
                updatedOrders.add(order);
            }


            orderQueue.addAll(updatedOrders);


            for (Order order : orderHistory) {
                if (orderContainsItem(order, name) && order.getStatus().equals("Order Received")) {
                    order.setStatus("Denied");
                }
            }

            System.out.println("All pending orders containing the removed item have been denied.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static boolean orderContainsItem(Order order, String itemName) {
        for (CartItem cartItem : order.getItems()) {
            if (cartItem.getFood().getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    private static void viewMenu() {
        if (menu.isEmpty()) {
            System.out.println("The menu is currently empty.");
            return;
        }

        System.out.println("\nMenu Items:");
        for (Food item : menu.values()) {
            if (item.isAvailable()) {
                System.out.println(item);
            }
        }
    }

    private static void manageOrders() {
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders.");
            return;
        }

        System.out.println("\nPending Orders (VIP orders prioritized):");
        List<Order> pendingOrders = new ArrayList<>(orderQueue);
        pendingOrders.sort(Order::compareTo);

        for (int i = 0; i < pendingOrders.size(); i++) {
            System.out.println((i + 1) + ". " + pendingOrders.get(i));
        }

        int orderIndex = getIntInput("Select an order to process by index (0 to cancel): ") - 1;
        if (orderIndex < 0 || orderIndex >= pendingOrders.size()) {
            System.out.println("Invalid choice. Returning to previous menu.");
            return;
        }

        Order orderToUpdate = pendingOrders.get(orderIndex);

        System.out.println("\nChoose an action for the selected order:");
        System.out.println("1. Mark as Completed");
        System.out.println("2. Update Status (Preparing, Out for Delivery)");
        System.out.println("3. Deny Order");
        System.out.println("4. Process Refund");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                orderQueue.remove(orderToUpdate);
                orderToUpdate.setStatus("Completed");
                updateCustomerOrderHistory(orderToUpdate);
                orderHistory.add(orderToUpdate);
                System.out.println("Order marked as completed.");
                break;
            case 2:
                System.out.print("Enter new status (e.g., Preparing, Out for Delivery): ");
                String newStatus = scanner.nextLine();
                orderToUpdate.setStatus(newStatus);
                updateCustomerOrderHistory(orderToUpdate);
                System.out.println("Order status updated.");
                break;
            case 3:
                orderQueue.remove(orderToUpdate);
                orderToUpdate.setStatus("Denied");
                updateCustomerOrderHistory(orderToUpdate);
                orderHistory.add(orderToUpdate);
                System.out.println("Order denied.");
                break;
            case 4:
                orderQueue.remove(orderToUpdate);
                orderToUpdate.setStatus("Refunded");
                updateCustomerOrderHistory(orderToUpdate);
                orderHistory.add(orderToUpdate);
                System.out.println("Refund processed for the order.");
                break;
            default:
                System.out.println("Invalid choice. Returning to previous menu.");
                break;
        }
    }

    private static void updateCustomerOrderHistory(Order updatedOrder) {
        Customer customer = updatedOrder.getCustomer();
        for (Order order : customer.getOrderHistory()) {
            if (order == updatedOrder) {
                order.setStatus(updatedOrder.getStatus());
                break;
            }
        }
    }

    private static void generateReport() {
        System.out.println("\nDaily Sales Report");
        double totalSales = 0;
        HashMap<String, Integer> itemCounts = new HashMap<>();

        for (Order order : orderHistory) {
            if (order.getStatus().equals("Completed")) {
                for (CartItem item : order.getItems()) {
                    totalSales += item.getFood().getPrice() * item.getQuantity();
                    itemCounts.put(item.getFood().getName(),
                            itemCounts.getOrDefault(item.getFood().getName(), 0) + item.getQuantity());
                }

                if (order.getCustomer().isVIP()) {
                    totalSales += 50;
                }
            }
        }

        System.out.println("Total Sales: Rs." + totalSales);
        System.out.println("Most Popular Items:");
        itemCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " orders"));
    }

    private static void customerInterface() {
        System.out.print("\nEnter your name: ");
        String name = scanner.nextLine();


        Customer customer = customers.getOrDefault(name, new RegularCustomer(name));


        if (!customers.containsKey(name)) {
            customers.put(name, customer);
        }
        ArrayList<CartItem> cart = new ArrayList<>();
        while (true) {
            System.out.println("\nCustomer Interface");
            System.out.println("1. Browse Menu");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Remove from Cart"); // New option for removing items
            System.out.println("5. Checkout");
            System.out.println("6. Cancel Order");
            System.out.println("7. View Order History");
            System.out.println("8. Leave a Review");
            System.out.println("9. View Item Reviews");
            System.out.println("10. Search Menu");
            System.out.println("11. Filter by Category");
            System.out.println("12. Sort by Price in Ascending order");
            System.out.println("13. Sort by Price in Descending order");
            System.out.println("14. Exit");
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    addToCart(cart);
                    break;
                case 3:
                    viewCart(cart);
                    break;
                case 4:
                    removeFromCart(cart);
                    break;
                case 5:
                    checkout(cart, customer);
                    break;
                case 6:
                    cancelOrder(customer);
                    break;
                case 7:
                    viewOrderHistory(customer);
                    break;
                case 8:
                    leaveReview(customer);
                    break;
                case 9:
                    viewItemReviews();
                    break;
                case 10:
                    searchMenu();
                    break;
                case 11:
                    filterMenuByCategory();

                case 12:
                    sortMenuByPrice(true);
                    break;
                case 13:
                    sortMenuByPrice(false);
                    break;
                case 14:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addToCart(List<CartItem> cart) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        if (menu.containsKey(name) && menu.get(name).isAvailable()) {
            int quantity = getIntInput("Enter quantity: ");

            boolean itemFound = false;
            for (CartItem cartItem : cart) {
                if (cartItem.getFood().getName().equalsIgnoreCase(name)) {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                cart.add(new CartItem(menu.get(name), quantity));
            }
            saveCartToFile(cart);
            System.out.println("Item added to cart.");
        } else {
            System.out.println("Item not available.");
        }
    }

    private static void removeFromCart(List<CartItem> cart) {
        System.out.print("Enter the item name to remove: ");
        String itemName = scanner.nextLine();

        boolean itemFound = false;


        Iterator<CartItem> iterator = cart.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getFood().getName().equalsIgnoreCase(itemName)) {
                itemFound = true;


                iterator.remove();
                System.out.println(itemName + " has been removed from your cart.");


                saveCartToFile(cart);
                break;
            }
        }

        if (!itemFound) {
            System.out.println("Item not found in your cart.");
        }
    }

    private static void viewCart(List<CartItem> cart) {
        cart.clear();
        cart.addAll(loadCartFromFile());
        System.out.println("\nYour Cart:");
        double total = 0;
        for (CartItem item : cart) {
            System.out.println(item);
            total += item.getFood().getPrice() * item.getQuantity();
        }
        System.out.println("Total: Rs." + total);
    }

    static void checkout(List<CartItem> cart, Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double total = 0;
        boolean vipUpgraded = false;


        for (CartItem item : cart) {
            total += item.getFood().getPrice() * item.getQuantity();
        }


        if (!customer.isVIP()) {
            System.out.print("Do you want to upgrade to VIP for Rs.50? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                total += 50;

                Customer vipCustomer = customer.upgradeToVIP();
                customer = vipCustomer;
                vipUpgraded = true;
                System.out.println("Congratulations! You are now a VIP customer.");
            }
        }

        System.out.println("\nYour total payment: Rs." + total);
        System.out.print("Confirm payment? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            System.out.print("Any special requests? ");
            String specialRequest = scanner.nextLine();


            Order order = new Order(customer, new ArrayList<>(cart), specialRequest);
            order.setStatus(vipUpgraded ? "VIP Order" : "Order Received");


            customer.getOrderHistory().add(order);
            orderHistory.add(order);
            orderQueue.add(order);

            saveOrders();

            cart.clear();
            clearCartFile();
            System.out.println("Order placed successfully! Thank you for your payment.");
        } else {
            System.out.println("Order not confirmed. Returning to menu.");
        }
    }

    private static void cancelOrder(Customer customer) {
        if (customer.getOrderHistory().isEmpty()) {
            System.out.println("No orders to cancel.");
            return;
        }

        System.out.println("\nYour Order History:");
        for (int i = 0; i < customer.getOrderHistory().size(); i++) {
            System.out.println((i + 1) + ". " + customer.getOrderHistory().get(i) +
                    " | Status: " + customer.getOrderHistory().get(i).getStatus());
        }

        int index = getIntInput("Enter the order index to cancel: ") - 1;

        if (index < 0 || index >= customer.getOrderHistory().size()) {
            System.out.println("Invalid order index.");
            return;
        }

        Order orderToCancel = customer.getOrderHistory().get(index);

        if (orderToCancel.getStatus().equals("Order Received")) {
            orderToCancel.setStatus("Cancelled");
            orderQueue.remove(orderToCancel);
            saveOrders();
            System.out.println("Order cancelled successfully.");
        } else {
            System.out.println("Order cannot be cancelled as it is already being processed.");
        }
    }

    private static void viewOrderHistory(Customer customer) {
        if (customer.getOrderHistory().isEmpty()) {
            System.out.println("No previous orders found.");
        } else {
            System.out.println("\nYour Order History:");
            for (int i = 0; i < customer.getOrderHistory().size(); i++) {
                Order order = customer.getOrderHistory().get(i);
                System.out.println((i + 1) + ". " + order + " | Status: " + order.getStatus());
            }
        }
    }


    private static void leaveReview(Customer customer) {
        System.out.print("Enter item name to review: ");
        String itemName = scanner.nextLine();

        if (!menu.containsKey(itemName)) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter your review: ");
        String comment = scanner.nextLine();

        reviews.computeIfAbsent(itemName, k -> new ArrayList<>()).add(new Review(customer.getName(), comment));
        System.out.println("Review added successfully.");
    }

    private static void viewItemReviews() {
        System.out.print("Enter item name to view reviews: ");
        String itemName = scanner.nextLine();

        if (!reviews.containsKey(itemName)) {
            System.out.println("No reviews found for this item.");
            return;
        }

        System.out.println("\nReviews for " + itemName + ":");
        for (Review review : reviews.get(itemName)) {
            System.out.println(review);
        }
    }

    private static void searchMenu() {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean found = false;

        for (Food item : menu.values()) {
            if (item.getName().toLowerCase().contains(keyword)) {
                System.out.println(item);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No items match your search.");
        }
    }

    private static void filterMenuByCategory() {
        System.out.print("Enter category to filter: ");
        String category = scanner.nextLine().toLowerCase();

        for (Food item : menu.values()) {
            if (item.getCategory().toLowerCase().equals(category) && item.isAvailable()) {
                System.out.println(item);
            }
        }
    }

    private static void sortMenuByPrice(boolean ascending) {
        List<Food> sortedItems = new ArrayList<>(menu.values());
        sortedItems.sort((item1, item2) -> ascending ?
                Double.compare(item1.getPrice(), item2.getPrice()) :
                Double.compare(item2.getPrice(), item1.getPrice()));

        for (Food item : sortedItems) {
            if (item.isAvailable()) {
                System.out.println(item);
            }
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid price.");
            }
        }
    }

    private static boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("true")) return true;
            if (input.equalsIgnoreCase("false")) return false;
            System.out.println("Invalid input. Enter 'true' or 'false'.");
        }
    }


    public static class ByteMeGUI extends Application {

        private Stage primaryStage;

        @Override
        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage;
            showMenuPage();
        }


        private void showMenuPage() {

            System.out.println("Menu Items:");
            for (Food item : ByteMe.menu.values()) {
                System.out.println(item);
            }
            ByteMe.loadMenu();
            Label title = new Label("Canteen Menu");


            TableView<Food> menuTable = new TableView<>();
            TableColumn<Food, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<Food, Double> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn<Food, Boolean> availabilityCol = new TableColumn<>("Available");
            availabilityCol.setCellValueFactory(new PropertyValueFactory<>("available"));

            menuTable.getColumns().addAll(nameCol, priceCol, availabilityCol);


            menuTable.getItems().clear();
            menuTable.getItems().addAll(ByteMe.menu.values());
            menuTable.refresh();
            System.out.println("Data added to TableView: " + menuTable.getItems());


            Button ordersPageButton = new Button("View Orders");
            ordersPageButton.setOnAction(e -> showOrdersPage());

            VBox layout = new VBox(10, title, menuTable, ordersPageButton);
            layout.setAlignment(Pos.CENTER);
            Scene menuScene = new Scene(layout, 600, 400);
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Byte Me - Menu");
            primaryStage.show();
        }


        private void showOrdersPage() {
            Label title = new Label("Pending Orders");


            TableView<Order> ordersTable = new TableView<>();

            TableColumn<Order, String> customerCol = new TableColumn<>("Customer");
            customerCol.setCellValueFactory(order ->
                    new SimpleStringProperty(order.getValue().getCustomer().getName()));

            TableColumn<Order, String> itemsCol = new TableColumn<>("Items");
            itemsCol.setCellValueFactory(order -> {
                StringBuilder items = new StringBuilder();
                for (CartItem item : order.getValue().getItems()) {
                    items.append(item.getFood().getName()).append(" x").append(item.getQuantity()).append("; ");
                }
                return new SimpleStringProperty(items.toString());
            });

            TableColumn<Order, String> statusCol = new TableColumn<>("Status");
            statusCol.setCellValueFactory(order ->
                    new SimpleStringProperty(order.getValue().getStatus()));


            ordersTable.getColumns().addAll(customerCol, itemsCol, statusCol);

            ordersTable.getItems().addAll(ByteMe.orderQueue);

            Button backButton = new Button("Back to Menu");
            backButton.setOnAction(e -> showMenuPage());


            VBox layout = new VBox(10, title, ordersTable, backButton);
            layout.setAlignment(Pos.CENTER);
            Scene ordersScene = new Scene(layout, 600, 400);


            primaryStage.setScene(ordersScene);
            primaryStage.setTitle("Byte Me - Orders");
            primaryStage.show();
        }

    }
}