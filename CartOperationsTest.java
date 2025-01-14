import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartOperationsTest {

    @BeforeEach
    void setUp() {

        ByteMe.menu.clear();
    }

    @Test
    void testAddItemToCart() {

        Food availableFood = new Food("Available Item", 50.0, "snacks", true);
        ByteMe.menu.put(availableFood.getName(), availableFood);

        Customer customer = new RegularCustomer("Test Customer");
        List<CartItem> cart = new ArrayList<>();


        cart.add(new CartItem(availableFood, 1));
        double totalPrice = cart.stream().mapToDouble(item -> item.getFood().getPrice() * item.getQuantity()).sum();


        assertEquals(50.0, totalPrice, "Total price should be correctly updated when an item is added.");
    }

    @Test
    void testModifyItemQuantityInCart() {

        Food availableFood = new Food("Available Item", 50.0, "snacks", true);
        ByteMe.menu.put(availableFood.getName(), availableFood);

        Customer customer = new RegularCustomer("Test Customer");
        List<CartItem> cart = new ArrayList<>();
        CartItem cartItem = new CartItem(availableFood, 1);
        cart.add(cartItem);


        cartItem.setQuantity(3);
        double totalPrice = cart.stream().mapToDouble(item -> item.getFood().getPrice() * item.getQuantity()).sum();


        assertEquals(150.0, totalPrice, "Total price should update when item quantity is modified.");
    }

    @Test
    void testNegativeQuantityInCart() {

        Food availableFood = new Food("Available Item", 50.0, "snacks", true);
        ByteMe.menu.put(availableFood.getName(), availableFood);

        Customer customer = new RegularCustomer("Test Customer");
        List<CartItem> cart = new ArrayList<>();
        CartItem cartItem = new CartItem(availableFood, 1);
        cart.add(cartItem);


        try {
            cartItem.setQuantity(-1);
            fail("Negative quantity should not be allowed in the cart.");
        } catch (IllegalArgumentException e) {

            assertEquals("Quantity cannot be negative.", e.getMessage());
        }
    }
}
