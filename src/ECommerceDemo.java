

import java.util.*;

class ECommercePlatform {
    static class Product implements Comparable<Product> {
        private Integer id;
        private String name;
        private double price;
        private int stock;

        public Product(Integer id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        @Override
        public int compareTo(Product other) {
            return Double.compare(this.price, other.price);
        }

        @Override
        public String toString() {
            return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + '}';
        }
    }

    static class User {
        private Integer id;
        private String username;
        private Map<Product, Integer> cart;

        public User(Integer id, String username) {
            this.id = id;
            this.username = username;
            this.cart = new HashMap<>();
        }

        public Integer getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public Map<Product, Integer> getCart() {
            return cart;
        }

        public void addToCart(Product product, int quantity) {
            cart.put(product, cart.getOrDefault(product, 0) + quantity);
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", username='" + username + "', cart=" + cart + '}';
        }
    }

    static class Order {
        private Integer id;
        private Integer userId;
        private Map<Product, Integer> orderDetails;
        private double totalPrice;

        public Order(Integer id, Integer userId, Map<Product, Integer> orderDetails) {
            this.id = id;
            this.userId = userId;
            this.orderDetails = orderDetails;
            this.totalPrice = calculateTotalPrice();
        }

        public Integer getId() {
            return id;
        }

        public Integer getUserId() {
            return userId;
        }

        public Map<Product, Integer> getOrderDetails() {
            return orderDetails;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        private double calculateTotalPrice() {
            return orderDetails.entrySet().stream()
                    .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                    .sum();
        }

        @Override
        public String toString() {
            return "Order{id=" + id + ", userId=" + userId + ", orderDetails=" + orderDetails + ", totalPrice=" + totalPrice + '}';
        }
    }

    private Map<Integer, User> users;
    private Map<Integer, Product> products;
    private Map<Integer, Order> orders;

    public ECommercePlatform() {
        this.users = new HashMap<>();
        this.products = new HashMap<>();
        this.orders = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void createOrder(Integer userId, Map<Product, Integer> orderDetails) {
        Order order = new Order(orders.size() + 1, userId, orderDetails);
        orders.put(order.getId(), order);
        updateStock(orderDetails);
    }

    public List<Product> listAvailableProducts() {
        return new ArrayList<>(products.values());
    }

    public List<User> listUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Order> listOrders() {
        return new ArrayList<>(orders.values());
    }

    public void updateStock(Map<Product, Integer> orderDetails) {
        for (Map.Entry<Product, Integer> entry : orderDetails.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int currentStock = product.getStock();
            product.setStock(currentStock - quantity);
        }
    }

    public static void main(String[] args) {
        ECommercePlatform platform = new ECommercePlatform();

        // Adding users
        User user1 = new User(1, "User1");
        User user2 = new User(2, "User2");
        platform.addUser(user1);
        platform.addUser(user2);

        // Adding products
        Product product1 = new Product(1, "Product1", 10.0, 20);
        Product product2 = new Product(2, "Product2", 15.0, 15);
        platform.addProduct(product1);
        platform.addProduct(product2);

        // Simulating user interactions
        user1.addToCart(product1, 2);
        user2.addToCart(product2, 1);
        user2.addToCart(product1, 3);

        // Creating orders
        platform.createOrder(user1.getId(), user1.getCart());
        platform.createOrder(user2.getId(), user2.getCart());

        // Displaying the state of the platform
        System.out.println("Users: " + platform.listUsers());
        System.out.println("Products: " + platform.listAvailableProducts());
        System.out.println("Orders: " + platform.listOrders());
    }
}

