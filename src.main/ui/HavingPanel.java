package main.ui;

import main.delegates.TerminalOperationDelegate;
import main.model.ListTableModel;
import main.model.TeamPlayerHeight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HavingPanel extends JPanel {

    private ListTableModel tableModel;
    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;

    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;

    public HavingPanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(22, 30, 51));

        this.delegate = delegate;
        this.cl = cl;
        this.leagueManager = leagueManager;

        tableModel = new ListTableModel();

        setLayout(new BorderLayout());
        setElements();

    }

    private void setElements() {
        removeAll();
        makeBackMenuButton();
        makeCenterPanel();
    }

    public void makeBackMenuButton() {
        JButton back = new JButton("Back to the Menu");
        back.addActionListener(e -> cl.show(leagueManager, "menu"));
        add(back, BorderLayout.NORTH);
    }

    public void makeCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(22, 30, 51));
        makeHavingPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void makeHavingPanel(JPanel centerPanel) {
        String[] teams = delegate.getAllTeams();

        JComboBox<String> comboBox = new JComboBox<>(teams);

        JLabel avgHeight = new JLabel("Average Height: ");
        avgHeight.setForeground(Color.WHITE);

        JButton aggregate = new JButton("Find Average Heights");

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Player");
        tableModel.addColumn("Height (cm)");

        JTable playerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JPanel playerSelectPanel = new JPanel();
        playerSelectPanel.setBackground(new Color(22, 30, 51));
        playerSelectPanel.setPreferredSize(new Dimension(600, 445));
        playerSelectPanel.setLayout(new BoxLayout(playerSelectPanel, BoxLayout.Y_AXIS));

        playerSelectPanel.add(comboBox);
        playerSelectPanel.add(avgHeight);
        playerSelectPanel.add(aggregate);
        playerSelectPanel.add(scrollPane);

        aggregate.addActionListener((ActionEvent e) -> {
            String selectedTeam = (String) comboBox.getSelectedItem();

            if (selectedTeam != null) {
                String formattedHeight = String.format("%.2f", delegate.getAverageHeights(selectedTeam));
                avgHeight.setText("Average Height: " + formattedHeight + " cm");
                TeamPlayerHeight[] heightArray = delegate.getPlayerHeights(selectedTeam);

                tableModel.setRowCount(0);

                for (TeamPlayerHeight height : heightArray) {
                    Object[] rowData = {height.getPlayer(), height.getHeight()};
                    tableModel.addRow(rowData);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No team selected.");
            }
        });

        centerPanel.add(playerSelectPanel);
    }


}
