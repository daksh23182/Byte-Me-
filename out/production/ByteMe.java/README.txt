Overview:
The Byte Me Canteen System is a software solution that allows both admins and customers to interact with the canteen's menu and manage orders. The system supports both a Graphical User Interface (GUI) using JavaFX and a Command Line Interface (CLI) for interacting with the canteen system.

Core Features:
Admin Features:

Manage Menu Items: Admins can add, update, and remove menu items, with the changes reflected in the menu.txt file.
View and Manage Orders: Admins can view pending orders, update their statuses (e.g., preparing, completed, canceled), and process refunds for canceled orders.
Process VIP Requests: Admins can approve or reject VIP membership requests for customers.
View Total Sales: Admins can view total sales accumulated from completed orders.
Customer Features:

View Menu: Customers can browse the menu, including details like item name, price, and availability.
Place Orders: Customers can place orders by selecting items from the menu (through CLI or GUI).
Order History: Customers can view their previous orders saved in a user-specific file.
File Handling:

Menu Data: Menu items are saved and loaded from the menu.txt file.
Order Data: Pending orders are saved and loaded from the orders.txt file.
Order History: Each customer’s order history is stored in a file named username_orders.txt.
Cart Storage: During a session, the cart data is saved temporarily for the user.


Running the Program:
There are two ways to interact with the system: GUI (Graphical User Interface) and CLI (Command Line Interface).

Option 1: Running the GUI:
Launch the program by running the ByteMe.java file.
The GUI will show up with an initial screen that allows you to choose to view the Menu or exit the program.
As an Admin, you can:
View and manage the menu (add/remove/update items).
View and update order statuses.
View and approve VIP requests.
As a Customer, you can:
View the menu and place orders (through CLI or GUI).
View the order status (in the CLI).
Option 2: Running the CLI:
After running the program, you will be prompted to choose between GUI or CLI by entering the respective option number.
If you select CLI:
You can log in as an Admin or Customer.
Admins can manage the menu and orders through a series of CLI commands.
Customers can place orders, view their order history, and check the menu.
File Structure:
The system uses several text files to store and load data:

menu.txt: Contains the list of menu items, including their name, price, and availability.
orders.txt: Contains the list of pending orders. Each order includes the order number, items ordered, and the status.
username_orders.txt: Stores the order history of each customer. Each file is specific to the user and named using the user's username.
Example of menu.txt:
Copy code
Burger,5.99,Fast Food,Available
Pizza,8.99,Fast Food,Unavailable
Pasta,7.49,Italian,Available
Soda,1.99,Beverage,Available
Example of orders.txt:
Copy code
1,Burger,Pizza,Pending
2,Pasta,Soda,Preparing
Detailed Features & Instructions:
Admin Features:
Login:

Admin logs in using a predefined username and password.
Admin can choose to:
View and manage the Menu (add/update/remove items).
View and manage Orders (update order status, process refunds).
View VIP Requests (approve or reject VIP status requests).
View Total Sales.
Menu Management:

Add Items: Admin can add new items to the menu by providing the name, price, and category of the item.
Update Items: Admin can update the price and category of existing menu items.
Remove Items: Admin can remove items from the menu.
Order Management:

Admin can view pending orders, mark them as completed or canceled, and process refunds for canceled orders.
VIP Processing: Admin can approve or reject VIP requests from customers.
Customer Features:
Login:

Customers log in using their roll number (7 digits) and password.
Customers can:
Browse the Menu: View the list of items, their prices, and availability.
Place Orders: Choose items from the menu to place an order.
Order History:

Customers can view their previous orders by checking the username_orders.txt file.
How to Use the System:
1. Admin Login:
The admin can log in using the credentials:
Admin ID: admin
Admin Password: admin123
2. Customer Login:
The customer logs in using their roll number and password. The password is assumed to be the last 3 digits of the roll number.
3. Adding Menu Items:
Admins can add a new item to the menu by entering the item’s name, price, and category. The updated menu is then saved to menu.txt.
4. Order Management:
Admin can view pending orders, update their status, and process refunds for canceled orders.
File Handling:
Menu File (menu.txt): Stores all menu items. Changes made by the admin (add, remove, update) are reflected in this file.
Order File (orders.txt): Stores the orders. Admins can update the order status.
Order History: Each customer's order history is saved to their specific file (e.g., john_doe_orders.txt).