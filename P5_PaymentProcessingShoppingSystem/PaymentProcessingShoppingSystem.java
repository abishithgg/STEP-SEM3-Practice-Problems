package P5_PaymentProcessingShoppingSystem;

import java.util.*;

interface PaymentMethod {
    boolean processPayment(double amount);
    String getName();
}

class CreditCardPayment implements PaymentMethod {

    public boolean processPayment(double amount) {
        return true;
    }

    public String getName() {
        return "Credit Card";
    }
}

class PayPalPayment implements PaymentMethod {

    public boolean processPayment(double amount) {
        return false;
    }

    public String getName() {
        return "PayPal";
    }
}

class BankTransferPayment implements PaymentMethod {

    public boolean processPayment(double amount) {
        return true;
    }

    public String getName() {
        return "Bank Transfer";
    }
}

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }
}

class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

enum OrderStatus {
    PENDING,
    PAID
}

class Order {
    private Customer customer;
    private List<OrderItem> items;
    private OrderStatus status;

    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addProduct(
            Product product,
            int quantity) {

        if (quantity <= 0) {
            System.out.println(
                "Quantity must be greater than zero."
            );
            return;
        }

        items.add(
            new OrderItem(product, quantity)
        );
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getTotal();
        }

        return total;
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }
}

class PaymentProcessor {

    public void processPayment(
            Order order,
            PaymentMethod paymentMethod) {

        if (order.isEmpty()) {
            System.out.println(
                "Cannot process payment for an empty order."
            );
            return;
        }

        System.out.println(
            "Payment initiated via " +
            paymentMethod.getName() +
            " for Order " +
            order.getCustomerName() +
            "."
        );

        boolean success =
            paymentMethod.processPayment(
                order.getTotal()
            );

        if (success) {

            order.markPaid();

            System.out.println(
                "Payment for Order " +
                order.getCustomerName() +
                " successful."
            );

            System.out.println(
                "Order status: Paid."
            );

        } else {

            System.out.println(
                "Payment for Order " +
                order.getCustomerName() +
                " failed."
            );

            System.out.println(
                "Order status: Pending."
            );
        }
    }
}

public class PaymentProcessingShoppingSystem {

    public static void main(String[] args) {

        Customer customerX =
            new Customer("X");

        Customer customerY =
            new Customer("Y");

        Customer customerZ =
            new Customer("Z");

        Product productA =
            new Product("Product A", 100);

        Product productB =
            new Product("Product B", 200);

        Product productC =
            new Product("Product C", 300);

        Order orderX =
            new Order(customerX);

        orderX.addProduct(productA, 2);
        orderX.addProduct(productB, 1);

        System.out.println(
            "Order created for Customer X."
        );

        PaymentProcessor processor =
            new PaymentProcessor();

        processor.processPayment(
            orderX,
            new CreditCardPayment()
        );

        Order orderY =
            new Order(customerY);

        System.out.println(
            "Order created for Customer Y."
        );

        processor.processPayment(
            orderY,
            new CreditCardPayment()
        );

        Order orderZ =
            new Order(customerZ);

        orderZ.addProduct(productC, 1);

        System.out.println(
            "Order created for Customer Z."
        );

        processor.processPayment(
            orderZ,
            new PayPalPayment()
        );
    }
}
