package main.model;

public class Team {
    private String name;
    private String city;
    private String arena;
    private String division;
    private int cap_space;



    public Team(String name, String city, String arena, String division, int cap_space) {
        this.name = name;
        this.city = city;
        this.arena = arena;
        this.division = division;
        this.cap_space = cap_space;
    }
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getArena() {
        return arena;
    }

    public String getDivision() {
        return division;
    }

    public int getCap_space() {
        return cap_space;
    }
}
