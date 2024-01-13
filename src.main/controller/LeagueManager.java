package main.controller;

import main.Exception.*;
import main.database.DatabaseConnectionHandler;
import main.delegates.LoginWindowDelegate;
import main.delegates.TerminalOperationDelegate;
import main.model.*;
import main.ui.LoginWindow;
import main.ui.TerminalWindow;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class LeagueManager  implements LoginWindowDelegate, TerminalOperationDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;

    public LeagueManager() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

//            TerminalTransactions transaction = new TerminalTransactions();
//            transaction.setupDatabase(this);
//            transaction.showMainMenu(this);
            new TerminalWindow(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) {
        LeagueManager leagueManager = new LeagueManager();
        leagueManager.start();
    }

    @Override
    public Contract[] getContractInfo() {
        return dbHandler.getContractInfo();
    }

    @Override
    public Player[] getPlayerInfo() { return dbHandler.getPlayerInfo(); }

    @Override
    public Team[] getTeamInfo() { return dbHandler.getTeamInfo(); }

    /**
     * TerminalOperationDelegate Implementation
     *
     * The TerminalTransaction instance tells us that the user is fine with dropping any existing table
     * called branch and creating a new one for this project to use
     */
    @Override
    public void databaseSetup() throws FileNotFoundException {
//        dbHandler.databaseSetup();
    }

    @Override
    public boolean updateContract(Contract contract, String length, String bonus) throws NullContractException, InvalidBonusException, InvalidLengthException {
        if (contract == null) {
            throw new NullContractException();
        }
        int newBonus = 0;
        int newLength = 0;

        try {
            newLength = Integer.parseInt(length);
            if(newLength <= 0 || newLength > 5) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidLengthException();
        }
        try {
            newBonus = Integer.parseInt(bonus);
            if(newBonus < 0 || newBonus > 75000) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidBonusException();
        }
        return dbHandler.updateContract(contract.getID(), newLength, newBonus);
    }

    @Override
    public TeamStaff[] getTeamStaffInfo(String input) throws InvalidSalaryException {
        try {
            int salary = Integer.parseInt(input);
            if(salary < 0) {
                throw new NumberFormatException();
            }
            return dbHandler.getTeamStaffInfo(salary);
        } catch(NumberFormatException e) {
            throw new InvalidSalaryException();
        }
    }

    public boolean insertPlayer(LocalDateTime debutYear, LocalDateTime dob, int height, String name, int jerseyNum, int pid, String teamName, String cityName) throws NonExistentTeamException {

        boolean teamExists = dbHandler.checkTeamExists(teamName, cityName);

        if (!teamExists) {
            throw new NonExistentTeamException();
        }

        return dbHandler.insertPlayer(debutYear, dob, height, name, jerseyNum, pid, teamName, cityName);
    }

    @Override
    public boolean deletePlayer(int pid) {
        return dbHandler.deletePlayer(pid);
    }

    @Override
    public Team[] selectTeams(String attribute, String comparison, String value) throws NoAttributeSelectedException, NoComparatorSelectedException {
        String sqlAtt = "";
        switch (attribute) {
            case "Team Name":
                sqlAtt = "TName";
                break;
            case "Cap Space":
                sqlAtt = "Cap_Space";
                break;
            case "Arena":
                sqlAtt = "Arena";
                break;
            case "City Name":
                sqlAtt = "City";
                break;
            case "Division Name":
                sqlAtt = "DName";
                break;
            default:
                throw new NoAttributeSelectedException();
        }

        switch (comparison) {
            case ">":
                break;
            case ">=":
                break;
            case "<":
                break;
            case "<=":
                break;
            case "=":
                break;
            case "contains":
                comparison = "LIKE";
                value = "%" + value + "%";
                break;
            case "starts with":
                comparison = "LIKE";
                value = value + "%";
                break;
            case "equals":
                comparison = "=";
                break;
            case "ends with":
                comparison = "LIKE";
                value = "%" + value;
                break;
            default:
                throw new NoComparatorSelectedException();
        }
        return dbHandler.selectTeams(sqlAtt, comparison, value);
    }

    @Override
    public SponsorSponsoredAmount[] getSponsoredAmounts() {
        return dbHandler.getSponsoredAmounts();
    }

    @Override
    public int getHigherThanAvgContractByLength(int length) {
        return dbHandler.getHigherThanAvgContractByLength(length);
    }

    @Override
    public Sponsor[] getSponsors() {
        return dbHandler.getSponsors();
    }

    @Override
    public Team[] getTeamSponsoredByAll() {
        return dbHandler.getTeamSponsoredByAll();
    }

    @Override
    public TeamPlayerHeight[] getPlayerHeights(String team) { return dbHandler.getPlayerHeights(team); }

    @Override
    public String[] getAllTeams() {return dbHandler.getAllTeams();}

    @Override
    public double getAverageHeights(String team) { return dbHandler.getAverageHeights(team);}

    @Override
    public String[] getAllTables() {return dbHandler.getAllTables();}

    @Override
    public String[] getAllAttributes(String table) {return dbHandler.getAllAttributes(table);}

    @Override
    public Object[] addAttributes(String[] attributes, String table) {return dbHandler.addAttributes(attributes, table);}
}
