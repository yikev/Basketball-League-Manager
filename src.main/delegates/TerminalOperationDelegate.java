package main.delegates;

import main.Exception.*;
import main.model.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public interface TerminalOperationDelegate {
    public Contract[] getContractInfo();

    public Player[] getPlayerInfo();
    public Team[] getTeamInfo();
    public boolean insertPlayer(LocalDateTime debutYear, LocalDateTime dob, int height, String name, int jerseyNum, int pid, String teamName, String cityName) throws NonExistentTeamException;
    public boolean deletePlayer(int pid);
    public void databaseSetup() throws FileNotFoundException;
    public boolean updateContract(Contract contract, String Length,String bonus) throws NullContractException, InvalidBonusException, InvalidLengthException;
    public Team[] selectTeams(String attribute, String comparison, String value) throws NoAttributeSelectedException, NoComparatorSelectedException;
    public TeamStaff[] getTeamStaffInfo(String input) throws InvalidSalaryException;
    public SponsorSponsoredAmount[] getSponsoredAmounts();
    public int getHigherThanAvgContractByLength(int length);
    public Sponsor[] getSponsors();
    public Team[] getTeamSponsoredByAll();
    public TeamPlayerHeight[] getPlayerHeights(String team);
    public double getAverageHeights(String team);
    public String[] getAllTeams();
    public String[] getAllTables();
    public String[] getAllAttributes(String table);
    public Object[] addAttributes(String[] attributes, String table);
}
