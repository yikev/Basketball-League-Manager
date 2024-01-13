package main.model;

public class TeamPlayerHeight {
    private String player;

    private int height;

    public TeamPlayerHeight(String player, int height) {
        this.player = player;
        this.height = height;
    }

    public String getPlayer() {
        return this.player;
    }

    public int getHeight() {
        return this.height;
    }

}
