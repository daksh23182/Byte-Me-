public class Food {
    private String name;
    private double price;
    private String category;
    private boolean isInStock;
    private int stock;


    public Food(String name, double price, String category, boolean isInStock) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.isInStock = isInStock;
        this.stock = isInStock ? 10 : 0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isInStock() {
        return isInStock;
    }

    public void setInStock(boolean inStock) {
        isInStock = inStock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public void decreaseStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
        } else {
            throw new IllegalArgumentException("Not enough stock available");
        }
    }
    public boolean isAvailable() {
        return stock > 0;
    }
    public void setAvailable(boolean available) {
        this.isInStock = available;

        if (!available) {
            this.stock = 0;
        }
    }
    @Override
    public String toString() {
        return "name=" + name + ", price=" + price + ", available=" + (isAvailable() ? "Yes" : "No");
    }

}
