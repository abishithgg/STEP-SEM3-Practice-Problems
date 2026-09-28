package P4_HotelBookingSystem;

import java.time.LocalDate;
import java.util.*;

abstract class Room {
    private String roomNumber;

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public abstract double calculatePrice(long nights);
}

class StandardRoom extends Room {

    public StandardRoom(String roomNumber) {
        super(roomNumber);
    }

    @Override
    public double calculatePrice(long nights) {
        return nights * 100;
    }
}

class DeluxeRoom extends Room {

    public DeluxeRoom(String roomNumber) {
        super(roomNumber);
    }

    @Override
    public double calculatePrice(long nights) {
        return nights * 180;
    }
}

class Suite extends Room {

    public Suite(String roomNumber) {
        super(roomNumber);
    }

    @Override
    public double calculatePrice(long nights) {
        return nights * 300;
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

class Reservation {
    private Customer customer;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate cancellationDeadline;
    private boolean cancelled;

    public Reservation(
            Customer customer,
            Room room,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate cancellationDeadline) {

        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cancellationDeadline = cancellationDeadline;
        this.cancelled = false;
    }

    public boolean overlaps(LocalDate start, LocalDate end) {

        return !cancelled
                && start.isBefore(endDate)
                && end.isAfter(startDate);
    }

    public double calculatePrice() {

        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                startDate,
                endDate
        );

        return room.calculatePrice(nights);
    }

    public void cancel(LocalDate currentDate) {

        if (cancelled) {
            System.out.println(
                    "Reservation is already cancelled."
            );
            return;
        }

        if (currentDate.isAfter(cancellationDeadline)) {
            System.out.println(
                    "Cancellation deadline has passed."
            );
            return;
        }

        cancelled = true;

        System.out.println(
                "Reservation for "
                        + customer.getName()
                        + ", "
                        + room.getClass().getSimpleName()
                        + " "
                        + room.getRoomNumber()
                        + " ("
                        + startDate
                        + " - "
                        + endDate
                        + ") cancelled successfully."
        );
    }

    public Room getRoom() {
        return room;
    }
}

class BookingManager {
    private List<Reservation> reservations;

    public BookingManager() {
        reservations = new ArrayList<>();
    }

    public boolean isAvailable(
            Room room,
            LocalDate startDate,
            LocalDate endDate) {

        for (Reservation reservation : reservations) {

            if (reservation.getRoom() == room
                    && reservation.overlaps(startDate, endDate)) {

                return false;
            }
        }

        return true;
    }

    public Reservation reserveRoom(
            Customer customer,
            Room room,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate cancellationDeadline) {

        if (!isAvailable(room, startDate, endDate)) {

            System.out.println(
                    room.getClass().getSimpleName()
                            + " "
                            + room.getRoomNumber()
                            + " is not available from "
                            + startDate
                            + " to "
                            + endDate
                            + "."
            );

            return null;
        }

        Reservation reservation = new Reservation(
                customer,
                room,
                startDate,
                endDate,
                cancellationDeadline
        );

        reservations.add(reservation);

        System.out.println(
                "Reservation confirmed for "
                        + customer.getName()
                        + ", "
                        + room.getClass().getSimpleName()
                        + " "
                        + room.getRoomNumber()
                        + " ("
                        + startDate
                        + " - "
                        + endDate
                        + ")."
        );

        System.out.printf(
                "Price: $%.2f%n",
                reservation.calculatePrice()
        );

        return reservation;
    }
}

public class HotelBookingSystem {

    public static void main(String[] args) {

        BookingManager system = new BookingManager();

        Customer customerA = new Customer("Customer A");
        Customer customerB = new Customer("Customer B");
        Customer customerC = new Customer("Customer C");

        Room standard101 = new StandardRoom("101");
        Room deluxe201 = new DeluxeRoom("201");

        LocalDate startA = LocalDate.of(2026, 1, 1);
        LocalDate endA = LocalDate.of(2026, 1, 5);

        System.out.println(
                "Standard Room 101 is available from "
                        + startA
                        + " to "
                        + endA
                        + "."
        );

        Reservation reservationA = system.reserveRoom(
                customerA,
                standard101,
                startA,
                endA,
                LocalDate.of(2025, 12, 30)
        );

        system.reserveRoom(
                customerB,
                standard101,
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 7),
                LocalDate.of(2026, 1, 1)
        );

        reservationA.cancel(
                LocalDate.of(2025, 12, 29)
        );

        system.reserveRoom(
                customerC,
                deluxe201,
                LocalDate.of(2026, 2, 10),
                LocalDate.of(2026, 2, 12),
                LocalDate.of(2026, 2, 5)
        );
    }
}