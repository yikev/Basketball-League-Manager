package main.ui;

import main.Exception.InvalidNumException;
import main.Exception.NoAttributeSelectedException;
import main.Exception.NoComparatorSelectedException;
import main.delegates.TerminalOperationDelegate;
import main.model.ListTableModel;
import main.model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SelectPanel extends JPanel {

    private ListTableModel tableModel;

    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;
    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;
    private JComboBox<String> attributeComboBox;
    private JComboBox<String> comparisonComboBox;
    private JTextField textFieldValue;

    JPanel centerPanel;

    public SelectPanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(22, 30, 51));

        this.delegate = delegate;
        this.cl = cl;
        this.leagueManager = leagueManager;
        comparisonComboBox = new JComboBox<>();

        setLayout(new BorderLayout());

        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);

        tableModel = new ListTableModel();

        setElements();

        centerPanel = new JPanel();
        centerPanel.setBackground(new Color(22, 30, 51));
        makeTeamPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setElements() {
        removeAll();
        makeBackMenuButton();
        revalidate();
        repaint();
    }

    public void makeBackMenuButton() {
        JButton back = new JButton("Back to the Menu");
        back.addActionListener(e -> cl.show(leagueManager, "menu"));
        back.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JPanel northPanel = new JPanel();
        northPanel.setBackground(getBackground());
        northPanel.add(back);
        add(northPanel, BorderLayout.NORTH);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();

        String[] attributes = {"Team Name", "Cap Space", "Arena", "City Name", "Division Name"};
        attributeComboBox = new JComboBox<>(attributes);

        String[][] comparisonOptions = {
                {">=", ">", "=", "<", "<="}, // for capSpace
                {"contains", "starts with", "equals", "ends with"}  // for any of the strings attributes
        };

        textFieldValue = new JTextField(10);

        JButton selectButton = new JButton("Select Teams");
        selectButton.addActionListener(this::selectTeams);

        comparisonComboBox = new JComboBox<>(comparisonOptions[1]); // set comparison to strings options bc teamName is first attribute

        attributeComboBox.addActionListener(e -> {
            int selectedIndex = attributeComboBox.getSelectedIndex();
            if (selectedIndex == 1) {
                comparisonComboBox.setModel(new DefaultComboBoxModel<>(comparisonOptions[0]));
            } else {
                comparisonComboBox.setModel(new DefaultComboBoxModel<>(comparisonOptions[1]));
            }
        });

        filterPanel.add(attributeComboBox);
        filterPanel.add(comparisonComboBox);
        filterPanel.add(textFieldValue);
        filterPanel.add(selectButton, BorderLayout.SOUTH);

        return filterPanel;
    }

    public void makeTeamPanel(JPanel centerPanel) {
        centerPanel.removeAll();

        JPanel filterPanel = createFilterPanel();

        Team[] teamsArray = delegate.getTeamInfo();

        tableModel.addColumn("City");
        tableModel.addColumn("Name");
        tableModel.addColumn("Arena");
        tableModel.addColumn("Division");
        tableModel.addColumn("Cap Space");

        for (Team team : teamsArray) {
            Object[] rowData = {
                    team.getCity(),
                    team.getName(),
                    team.getArena(),
                    team.getDivision(),
                    team.getCap_space()
            };
            tableModel.addRow(rowData);
        }

        JTable teamTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(teamTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JPanel teamSelectPanel = new JPanel();
        teamSelectPanel.setBackground(new Color(22, 30, 51));
        teamSelectPanel.setPreferredSize(new Dimension(600, 445));
        teamSelectPanel.setLayout(new BoxLayout(teamSelectPanel, BoxLayout.Y_AXIS));

        teamSelectPanel.add(filterPanel);
        teamSelectPanel.add(scrollPane);

        centerPanel.add(teamSelectPanel, BorderLayout.CENTER);
    }

    private void selectTeams(ActionEvent e) {
        String attribute = (String) attributeComboBox.getSelectedItem();
        String comparison = (String) comparisonComboBox.getSelectedItem();
        String value = textFieldValue.getText();

        int selectedIndex = attributeComboBox.getSelectedIndex();

        if (selectedIndex == 1) {
            try {
                Integer.parseInt(textFieldValue.getText());
            } catch (NumberFormatException ex) {
                try {
                    throw new InvalidNumException("Cap Space Value");
                } catch (InvalidNumException nex) {
                    JOptionPane.showMessageDialog(this, nex.getMessage());
                    return;
                }
            }
        }

        try {
            Team[] teams = delegate.selectTeams(attribute, comparison, value);

            updateTable(teams);

        } catch (NoAttributeSelectedException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (NoComparatorSelectedException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateTable(Team[] teams) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.addColumn("Team Name");
        tableModel.addColumn("City");
        tableModel.addColumn("Arena");
        tableModel.addColumn("Division");
        tableModel.addColumn("Cap Space");

        for (Team team : teams) {
            Object[] rowData = {
                    team.getName(),
                    team.getCity(),
                    team.getArena(),
                    team.getDivision(),
                    team.getCap_space()
            };
            tableModel.addRow(rowData);
        }
    }


}
