package main.model;

public class TeamStaff {
    private final String TName;
    private final String city;
    private final String staffName;
    private final int salary;
    public TeamStaff(String TName, String city, String staffName, int salary) {
        this.TName = TName;
        this.city = city;
        this.staffName = staffName;
        this.salary = salary;
    }

    public String getTName() {
        return TName;
    }

    public String getCity() {
        return city;
    }

    public String getStaffName() {
        return staffName;
    }

    public int getSalary() {
        return salary;
    }
}
