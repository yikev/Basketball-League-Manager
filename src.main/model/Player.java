package main.model;

import java.time.LocalDateTime;

public class Player {

    private String TName;
    private String city;
    private String name;
    private int pid;
    private int height;
    private int jerseyNum;
    private LocalDateTime debutYear;
    private LocalDateTime dateofBirth;
    private Contract playerContract;
    public Player(LocalDateTime debutYear, LocalDateTime dateOfBirth,  int height,String name, int jerseyNum
            ,int pid, String TName, String city, Contract playerContract) {
        this.TName = TName;
        this.city = city;
        this.name = name;
        this.pid = pid;
        this.height = height;
        this.jerseyNum = jerseyNum;
        this.debutYear = debutYear;
        this.dateofBirth = dateOfBirth;
        this.playerContract = playerContract;
    }

    public Player(LocalDateTime debutYear, LocalDateTime dateOfBirth,  int height,String name, int jerseyNum
            ,int pid, String TName, String city) {
        this.TName = TName;
        this.city = city;
        this.name = name;
        this.pid = pid;
        this.height = height;
        this.jerseyNum = jerseyNum;
        this.debutYear = debutYear;
        this.dateofBirth = dateOfBirth;
    }

    public String getTName() {
        return TName;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }
    public int getPid() {return pid;}
    public int getHeight() {
        return height;
    }
    public int getJerseyNum() {
        return jerseyNum;
    }
    public LocalDateTime getDebutYear() {
        return debutYear;
    }
    public LocalDateTime getDateofBirth() {
        return dateofBirth;
    }
    public Contract getPlayerContract() { return playerContract;}
    public void setPlayerContract(Contract c) {this.playerContract = c;}
}
