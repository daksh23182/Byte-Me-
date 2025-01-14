import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutOfStockTest {

    @BeforeEach
    void setUp() {

        ByteMe.menu.clear();
    }

    @Test
    void testOrderingOutOfStockItem() {

        Food unavailableFood = new Food("Out of Stock Item", 100.0, "snacks", false);
        ByteMe.menu.put(unavailableFood.getName(), unavailableFood);

        Customer customer = new RegularCustomer("Test Customer");
        List<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(unavailableFood, 1));


        boolean isOrderPlaced = false;
        try {
            ByteMe.checkout(cart, customer);
            isOrderPlaced = true;
        } catch (Exception e) {
            isOrderPlaced = false;
        }


        assertFalse(isOrderPlaced, "Order should not be placed for out-of-stock items.");
    }


}
