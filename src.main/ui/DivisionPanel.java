package main.ui;

import main.delegates.TerminalOperationDelegate;
import main.model.ListTableModel;
import main.model.Sponsor;
import main.model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DivisionPanel extends JPanel {
    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;

    private JPanel sponsorPanel;
    private JPanel teamPanel;

    public DivisionPanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.delegate = delegate;
        this.cl = cl;
        this.leagueManager = leagueManager;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                setElements();
            }
        });
    }

    public void setElements() {
        removeAll();
        makeBackMenuButton();
        makeSponsorPanel();
        makeMiddlePanel();
        makeTeamPanel();
        revalidate();
        repaint();
    }

    public void makeBackMenuButton() {
        JButton back = new JButton("Back to the Menu");
        back.addActionListener(e -> cl.show(leagueManager, "menu"));
        back.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(back);
    }

    public void makeSponsorPanel() {
        Sponsor[] sponsorArray = delegate.getSponsors();

        ListTableModel tableModel = new ListTableModel();

        tableModel.addColumn("Sponsor ID");
        tableModel.addColumn("Sponsor Name");
        tableModel.addColumn("Company Slogan");
        for (Sponsor sponsor : sponsorArray) {
            Object[] rowData = {sponsor.getId(), sponsor.getName(), sponsor.getSlogan()};
            tableModel.addRow(rowData);
        }
        JTable staffTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBackground(getBackground());
        if(sponsorPanel != null) {
            remove(sponsorPanel);
        }
        sponsorPanel = new JPanel();
        sponsorPanel.setBackground(getBackground());
        sponsorPanel.setPreferredSize(new Dimension(600, 200));
        sponsorPanel.add(scrollPane);
        add(sponsorPanel);
    }

    public void makeMiddlePanel() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        JLabel text1 = new JLabel("^^^^^    All sponsors in the system    ^^^^^");
        JLabel text2 = new JLabel("vvvvv    All teams that are sponsored by all the sponsors    vvvvv");
        text1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        text2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        middlePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        text1.setBackground(getBackground());
        text1.setForeground(Color.WHITE);
        text2.setBackground(getBackground());
        text2.setForeground(Color.WHITE);
        middlePanel.setBackground(getBackground());

        middlePanel.add(text1);
        middlePanel.add(text2);
        add(middlePanel);
    }

    public void makeTeamPanel() {
        Team[] teamArray = delegate.getTeamSponsoredByAll();

        ListTableModel tableModel = new ListTableModel();
        tableModel.addColumn("Home City");
        tableModel.addColumn("Team Name");
        tableModel.addColumn("Arena");
        tableModel.addColumn("Division");
        tableModel.addColumn("Cap Space");
        for (Team team : teamArray) {
            Object[] rowData = {team.getCity(), team.getName(), team.getArena(), team.getDivision(), team.getCap_space()};
            tableModel.addRow(rowData);
        }
        JTable staffTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBackground(getBackground());
        if(teamPanel != null) {
            remove(teamPanel);
        }
        teamPanel = new JPanel();
        teamPanel.setBackground(getBackground());
        teamPanel.setPreferredSize(new Dimension(600, 400));
        teamPanel.add(scrollPane);
        add(teamPanel);
    }
}
