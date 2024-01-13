package main.database;

import main.model.*;
import main.util.PrintablePreparedStatement;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");

            databaseSetup();

            System.out.println("\nDatabase set up!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public Contract getPlayerContract(int pid) {


        try {
            String query = "SELECT * FROM signed_contract";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Contract model = new Contract(rs.getInt("bonus"),
                        rs.getInt("pid"),
                        rs.getInt("length"),
                        rs.getInt("value"),
                        rs.getObject("signed_date", LocalDateTime.class),
                        rs.getInt("cid"));

                if (model.getPid() == pid) return model;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        Contract c = new Contract(-1, -1,-1, -1,LocalDateTime.of(2019, 03, 28, 14, 33, 48, 640000) ,-1);
        return c;
    }

    public boolean checkTeamExists(String teamName, String cityName) {
        try {
            String query = "SELECT COUNT(*) FROM Team WHERE TName = ? AND City = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, teamName);
            ps.setString(2, cityName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return false;
    }

    public boolean insertPlayer(LocalDateTime debutYear, LocalDateTime dob, int height, String name, int jerseyNum, int pid, String tName, String City) {

        String query = "INSERT INTO Player_Plays_for_Team VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            connection.setAutoCommit(false);

            if (debutYear == null) {
                ps.setNull(1, java.sql.Types.NULL);
            } else {
                ps.setObject(1, debutYear);
            }

            if (dob == null) {
                ps.setNull(2, java.sql.Types.NULL);
            } else {
                ps.setObject(2, dob);
            }

            ps.setInt(3, height);
            ps.setString(4, name);
            ps.setInt(5, jerseyNum);
            ps.setInt(6, pid);
            ps.setString(7, tName);
            ps.setString(8, City);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("Player added successfully.");
            } else {
                connection.rollback();
                System.out.println("Player could not be added.");
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePlayer(int pid) {
        String query = "DELETE FROM Player_Plays_for_Team WHERE PID = ?";
        String query2 = "DELETE FROM Signed_Contract WHERE PID = ?";
        try {
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            PrintablePreparedStatement ps2 = new PrintablePreparedStatement(connection.prepareStatement(query2), query2, false);

            connection.setAutoCommit(false);

            ps.setInt(1, pid);
            ps2.setInt(1, pid);

            int rowsAffected2 = ps2.executeUpdate();
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0 && rowsAffected2 > 0) {
                connection.commit();
                System.out.println("Player and contract deleted successfully.");
            } else {
                connection.rollback();
                System.out.println("Player not found or couldn't be deleted.");
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
        return false;
    }

    public Player[] getPlayerInfo() {
        ArrayList<Player> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM Player_Plays_for_Team";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime debut = rs.getObject("Debut_Year", LocalDateTime.class);
                LocalDateTime dob = rs.getObject("Date_of_Birth", LocalDateTime.class);
                int height = rs.getInt("Height");
                String name = rs.getString("Name");
                int jersey = rs.getInt("Jersey#");
                int pid = rs.getInt("PID");
                String team = rs.getString("TName");
                String city = rs.getString("City");

//                Player model = new Player(
//                        rs.getObject("Debut_Year", LocalDateTime.class),
//                        rs.getObject("Date_of_Birth", LocalDateTime.class),
//                        rs.getInt("Height"),
//                        rs.getString("Name"),
//                        rs.getInt("Jersey#"),
//                        rs.getInt("PID"),
//                        rs.getString("TName"),
//                        rs.getString("City"));


                Player model = new Player(
                        debut,
                        dob,
                        height,
                        name,
                        jersey,
                        pid,
                        team,
                        city,
                        getPlayerContract(pid));
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            e.printStackTrace();
        }

        return result.toArray(new Player[result.size()]);
    }

    public Team[] getTeamInfo() {
        ArrayList<Team> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM Team";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String teamName = rs.getString("TName");
                int capSpace = rs.getInt("Cap_Space");
                String arenaName = rs.getString("Arena");
                String cityName = rs.getString("City");
                String divisionName = rs.getString("DName");

                Team model = new Team(teamName, cityName, arenaName, divisionName, capSpace);
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            e.printStackTrace();
        }

        return result.toArray(new Team[result.size()]);
    }

    public Contract[] getContractInfo() {
        ArrayList<Contract> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM signed_contract";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Contract model = new Contract(rs.getInt("bonus"),
                        rs.getInt("pid"),
                        rs.getInt("length"),
                        rs.getInt("value"),
                        rs.getObject("signed_date", LocalDateTime.class),
                        rs.getInt("cid"));
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Contract[result.size()]);
    }

    public SponsorSponsoredAmount[] getSponsoredAmounts() {
        ArrayList<SponsorSponsoredAmount> sponsors = new ArrayList<>();
        try {
            String query = "Select S.Name, SUM(T.Sponsored_Amount) AS TOTALSPONSOREDAMT " +
                    "FROM Sponsor S, Sponsor_Sponsors_Team T " +
                    "WHERE S.SID = T.SID " +
                    "GROUP BY S.Name " +
                    "ORDER BY TOTALSPONSOREDAMT DESC";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Name");
                int amt = rs.getInt("TOTALSPONSOREDAMT");

                SponsorSponsoredAmount sponsor = new SponsorSponsoredAmount(name, amt);
                sponsors.add(sponsor);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return sponsors.toArray(new SponsorSponsoredAmount[sponsors.size()]);
    }

    public int getHigherThanAvgContractByLength(int length) {


        try {
            String query =  "SELECT AVG(c1.value) as avg_value " +
                    "FROM signed_contract c1 " +
                    "WHERE c1.value > (Select AVG(c2.value) " +
                    "From signed_contract c2) " +
                    "GROUP BY c1.length " +
                    "HAVING c1.length = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, length);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return rs.getInt("avg_value");
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return 0;
    }

    public boolean updateContract(int id, int newLength, int newBonus) {
        try {
            String query = "UPDATE signed_contract SET bonus = ?, length = ? WHERE cid = ?";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, newBonus);
            ps.setInt(2, newLength);
            ps.setInt(3, id);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Contract " + id + " does not exist!");
                return false;
            }
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            return false;
        }
        return true;
    }

    public Team[] selectTeams(String attribute, String comparison, String value) {
        ArrayList<Team> teams = new ArrayList<>();

        try {
            String query = "SELECT * FROM Team WHERE ";

            if (comparison.equals("LIKE")) {
                query += "UPPER(" + attribute + ") "  + "LIKE" + " UPPER(?)";
            } else if (attribute.equals("Cap_Space")) {
                query += attribute + " " + comparison + " ?";
            } else if (comparison.equals("=")) { // string equals
                query += "UPPER(" + attribute + ") "  + comparison + " UPPER(?)";
            }

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ps.setString(1, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("TName");
                String city = rs.getString("City");
                String arena = rs.getString("Arena");
                String division = rs.getString("DName");
                int cap_space = rs.getInt("Cap_Space");

                Team team = new Team(name, city, arena, division, cap_space);
                teams.add(team);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams.toArray(new Team[teams.size()]);
    }

    public TeamStaff[] getTeamStaffInfo(int salary) {
        ArrayList<TeamStaff> result = new ArrayList<>();

        try {
            String query = "SELECT t.tname, t.city, s.name, s.salary " +
                    "FROM team t, staff s, works_for w " +
                    "WHERE s.salary >= ? AND s.stid = w.stid AND w.tname = t.tname AND w.city = t.city";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, salary);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                TeamStaff model = new TeamStaff(rs.getString("TName"),
                        rs.getString("City"),
                        rs.getString("Name"),
                        rs.getInt("Salary"));
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new TeamStaff[result.size()]);
    }

    public Team[] getTeamSponsoredByAll() {
        ArrayList<Team> result = new ArrayList<>();

        try {
            String query = "SELECT t.tname, t.city, t.arena, t.dname, t.cap_space " +
                    "FROM team t " +
                    "WHERE NOT EXISTS (  SELECT s.sid " +
                    "FROM sponsor s " +
                    "MINUS " +
                    "SELECT sst.sid " +
                    "FROM sponsor_sponsors_team sst " +
                    "WHERE t.tname = sst.tname AND t.city = sst.city) ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Team model = new Team(rs.getString("tname"),
                        rs.getString("city"),
                        rs.getString("arena"),
                        rs.getString("dname"),
                        rs.getInt("cap_space"));
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Team[result.size()]);
    }

    public Sponsor[] getSponsors() {
        ArrayList<Sponsor> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM sponsor";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Sponsor model = new Sponsor(rs.getInt("sid"),
                        rs.getString("name"),
                        rs.getString("slogan"));
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Sponsor[result.size()]);
    }

    public double getAverageHeights(String team) {
        double result = 0;

        try {
            String query1 = "SELECT AVG(Height) AS AvgHeight FROM Player_Plays_For_Team WHERE TName = ?";

            PrintablePreparedStatement ps1 = new PrintablePreparedStatement(connection.prepareStatement(query1), query1, false);

            ps1.setString(1, team);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                result = rs1.getDouble("AvgHeight");
                System.out.println("Average Height for Team " + team + ": " + result);
            }

            rs1.close();
            ps1.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public TeamPlayerHeight[] getPlayerHeights(String team) {
        ArrayList<TeamPlayerHeight> result = new ArrayList<>();

        try {

            String query1 = "SELECT AVG(Height) AS AvgHeight FROM Player_Plays_For_Team WHERE TName = ?";
            String query2 = "SELECT * FROM Player_Plays_For_Team WHERE Tname = ? AND " +
                    "Height > (SELECT AVG(Height) FROM Player_Plays_For_Team WHERE TName = ?)";

            PrintablePreparedStatement ps1 = new PrintablePreparedStatement(connection.prepareStatement(query1), query1, false);
            PrintablePreparedStatement ps2 = new PrintablePreparedStatement(connection.prepareStatement(query2), query2, false);



            ps1.setString(1, team);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                double avgHeight = rs1.getDouble("AvgHeight");
                System.out.println("Average Height for Team " + team + ": " + avgHeight);
            }

            ps2.setString(1, team);
            ps2.setString(2, team);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                TeamPlayerHeight playerHeight = new TeamPlayerHeight(
                        rs2.getString("Name"),
                        rs2.getInt("Height")
                );
                result.add(playerHeight);
            }

            connection.setAutoCommit(false);

            rs1.close();
            rs2.close();
            ps1.close();
            ps2.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new TeamPlayerHeight[result.size()]);
    }

    public String[] getAllTeams() {

        ArrayList<String> result = new ArrayList<>();

        String query = "SELECT DISTINCT TName FROM Team";

        try {

            PrintablePreparedStatement ps1 = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                result.add(rs1.getString("TName"));
            }

            connection.setAutoCommit(false);

            rs1.close();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new String[0]);
    }

    public String[] getAllTables() {
        ArrayList<String> result = new ArrayList<>();

        String query = "SELECT table_name FROM user_tables";

        try {

            PrintablePreparedStatement ps1 = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                result.add(rs1.getString("table_name"));
            }

            connection.setAutoCommit(false);

            rs1.close();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new String[0]);
    }

    public String[] getAllAttributes(String table) {
        ArrayList<String> result = new ArrayList<>();

        String query = "SELECT DISTINCT column_name FROM all_tab_columns WHERE table_name = ?";

        try {

            PrintablePreparedStatement ps1 = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ps1.setString(1, table);

            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                result.add(rs1.getString("column_name"));
            }

            connection.setAutoCommit(false);

            rs1.close();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new String[0]);
    }

    public Object[] addAttributes(String[] attributes, String table) {
        ArrayList<Object> result = new ArrayList<>();

        String columns = String.join(", ", attributes);
        String query = "SELECT " + columns + " FROM " + table;

        try {
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                for (String attribute : attributes) {
                    result.add(rs.getString(attribute));
                }
            }

            connection.setAutoCommit(false);
            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Object[0]);
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void databaseSetup() throws FileNotFoundException {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setStopOnError(false);
        scriptRunner.runScript(new FileReader("./src/main/sql_scripts/databaseSetup.sql"));
    }


}
