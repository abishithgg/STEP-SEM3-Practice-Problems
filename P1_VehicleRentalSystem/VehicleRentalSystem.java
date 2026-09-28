package P1_VehicleRentalSystem;

interface Vehicle {
    String getName();
    double calculateCharge(int days);
}

class Sedan implements Vehicle {
    private String name;

    public Sedan(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double calculateCharge(int days) {
        return days * 50;
    }
}

class SUV implements Vehicle {
    private String name;

    public SUV(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double calculateCharge(int days) {
        return days * 80;
    }
}

class Truck implements Vehicle {
    private String name;

    public Truck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double calculateCharge(int days) {
        return days * 100;
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

class Rental {
    private Customer customer;
    private Vehicle vehicle;
    private int days;

    public Rental(Customer customer, Vehicle vehicle, int days) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
    }

    public double getCharge() {
        return vehicle.calculateCharge(days);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}

class RentalSystem {
    public Rental rentVehicle(
            Customer customer,
            Vehicle vehicle,
            int days) {

        if (vehicle instanceof RentalVehicle) {
            RentalVehicle rentalVehicle = (RentalVehicle) vehicle;

            if (!rentalVehicle.isAvailable()) {
                System.out.println(
                    vehicle.getName() + " is currently unavailable."
                );
                return null;
            }

            rentalVehicle.setAvailable(false);
        }

        Rental rental = new Rental(customer, vehicle, days);

        System.out.println(
            vehicle.getName() +
            " rented successfully by " +
            customer.getName() + "."
        );

        System.out.printf(
            "Rental charge: $%.2f%n",
            rental.getCharge()
        );

        return rental;
    }

    public void returnVehicle(Rental rental) {

        Vehicle vehicle = rental.getVehicle();

        if (vehicle instanceof RentalVehicle) {
            RentalVehicle rentalVehicle =
                (RentalVehicle) vehicle;

            rentalVehicle.setAvailable(true);
        }

        System.out.println(
            vehicle.getName() +
            " returned by " +
            rental.getCustomer().getName() +
            "."
        );
    }
}

interface RentalVehicle {
    boolean isAvailable();
    void setAvailable(boolean available);
}

class RentalSedan implements Vehicle, RentalVehicle {
    private String name;
    private boolean available = true;

    public RentalSedan(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double calculateCharge(int days) {
        return days * 50;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

class RentalSUV implements Vehicle, RentalVehicle {
    private String name;
    private boolean available = true;

    public RentalSUV(String name) {
        this.name = name;
    }

    public double calculateCharge(int days) {
        return days * 80;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }
}

public class VehicleRentalSystem {

    public static void main(String[] args) {

        Customer customer1 = new Customer("Customer 1");
        Customer customer2 = new Customer("Customer 2");
        Customer customer3 = new Customer("Customer 3");

        RentalVehicle sedanA = new RentalSedan("Sedan A");
        RentalVehicle suvB = new RentalSUV("SUV B");

        RentalSystem system = new RentalSystem();

        Rental rental1 = system.rentVehicle(
            customer1,
            (Vehicle) sedanA,
            3
        );

        system.rentVehicle(
            customer2,
            (Vehicle) sedanA,
            2
        );

        system.returnVehicle(rental1);

        system.rentVehicle(
            customer3,
            (Vehicle) suvB,
            5
        );
    }
}