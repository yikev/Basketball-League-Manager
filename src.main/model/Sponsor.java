package main.model;

public class Sponsor {
    private int id;
    private String name;
    private String slogan;
    public Sponsor(int id, String name, String slogan) {
        this.id =id;
        this.name = name;
        this.slogan = slogan;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlogan() {
        return slogan;
    }
}
