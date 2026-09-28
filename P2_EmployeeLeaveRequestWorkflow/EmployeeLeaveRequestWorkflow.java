package P2_EmployeeLeaveRequestWorkflow;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

interface LeavePolicy {
    boolean canTakeLeave(long days);
}

class FullTimeLeavePolicy implements LeavePolicy {

    public boolean canTakeLeave(long days) {
        return days <= 30;
    }
}

class PartTimeLeavePolicy implements LeavePolicy {

    public boolean canTakeLeave(long days) {
        return days <= 10;
    }
}

class ContractorLeavePolicy implements LeavePolicy {

    public boolean canTakeLeave(long days) {
        return days <= 5;
    }
}

abstract class Employee {
    private String name;
    private LeavePolicy leavePolicy;

    public Employee(String name, LeavePolicy leavePolicy) {
        this.name = name;
        this.leavePolicy = leavePolicy;
    }

    public String getName() {
        return name;
    }

    public LeavePolicy getLeavePolicy() {
        return leavePolicy;
    }
}

class FullTimeEmployee extends Employee {

    public FullTimeEmployee(String name) {
        super(name, new FullTimeLeavePolicy());
    }
}

class PartTimeEmployee extends Employee {

    public PartTimeEmployee(String name) {
        super(name, new PartTimeLeavePolicy());
    }
}

class Contractor extends Employee {

    public Contractor(String name) {
        super(name, new ContractorLeavePolicy());
    }
}

enum LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED
}

class LeaveRequest {
    private Employee employee;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;

    public LeaveRequest(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                "End date cannot be before start date."
            );
        }

        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = LeaveStatus.PENDING;
    }

    public long getDays() {
        return ChronoUnit.DAYS.between(
            startDate,
            endDate
        ) + 1;
    }

    public void approve() {

        if (status != LeaveStatus.PENDING) {
            System.out.println(
                "Cannot approve a non-pending request."
            );
            return;
        }

        if (!employee.getLeavePolicy().canTakeLeave(getDays())) {
            System.out.println(
                "Leave policy does not allow this request."
            );
            return;
        }

        status = LeaveStatus.APPROVED;

        System.out.println(
            employee.getName() +
            "'s leave request (" +
            startDate + " - " +
            endDate +
            ") approved."
        );

        System.out.println("Status: Approved.");
    }

    public void reject() {

        if (status != LeaveStatus.PENDING) {
            System.out.println(
                "Cannot reject a non-pending request."
            );
            return;
        }

        status = LeaveStatus.REJECTED;

        System.out.println(
            employee.getName() +
            "'s leave request (" +
            startDate + " - " +
            endDate +
            ") rejected."
        );

        System.out.println("Status: Rejected.");
    }

    public void changeToPending() {

        if (status != LeaveStatus.PENDING) {
            System.out.println(
                "Cannot change leave request status from " +
                status +
                " to Pending."
            );
            return;
        }

        System.out.println(
            "Leave request is already Pending."
        );
    }
}

class LeaveManagementSystem {

    public LeaveRequest submitLeave(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate) {

        LeaveRequest request =
            new LeaveRequest(
                employee,
                startDate,
                endDate
            );

        System.out.println(
            "Leave request submitted for " +
            employee.getName() +
            " (" +
            startDate +
            " - " +
            endDate +
            ")."
        );

        System.out.println("Status: Pending.");

        return request;
    }
}

public class EmployeeLeaveRequestWorkflow {

    public static void main(String[] args) {

        Employee john =
            new FullTimeEmployee("John");

        Employee jane =
            new PartTimeEmployee("Jane");

        LeaveManagementSystem system =
            new LeaveManagementSystem();

        LeaveRequest johnRequest =
            system.submitLeave(
                john,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 5)
            );

        System.out.println(
            "Manager Alice reviews John's request."
        );

        johnRequest.approve();

        LeaveRequest janeRequest =
            system.submitLeave(
                jane,
                LocalDate.of(2026, 2, 10),
                LocalDate.of(2026, 2, 11)
            );

        System.out.println(
            "Manager Bob reviews Jane's request."
        );

        janeRequest.reject();

        johnRequest.changeToPending();
    }
}