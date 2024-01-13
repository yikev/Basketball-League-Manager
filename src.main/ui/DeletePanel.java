package main.ui;

import main.delegates.TerminalOperationDelegate;
import main.model.Contract;
import main.model.ListTableModel;
import main.model.Player;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DeletePanel extends JPanel{

    private ListTableModel tableModel;
    private ListTableModel tableModel2;
    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;

    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;

    private JPanel operationPanel;

    public DeletePanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(22, 30, 51));

        this.delegate = delegate;
        this.cl = cl;
        this.leagueManager = leagueManager;

        tableModel = new ListTableModel();
        tableModel2 = new ListTableModel();

        setLayout(new BorderLayout());
        setElements();

    }

    private void setElements() {
        removeAll();
        makeBackMenuButton();
        makeCenterPanel();
        makeDeleteButton();
    }

    public void makeBackMenuButton() {
        JButton back = new JButton("Back to the Menu");
        back.addActionListener(e -> cl.show(leagueManager, "menu"));
        add(back, BorderLayout.NORTH);
    }

    public void makeDeleteButton() {
        return;
    }



    public void makePlayerPanel(JPanel centerPanel) {
        Player[] playersArray = delegate.getPlayerInfo();



        tableModel.addColumn("Debut Year");
        tableModel.addColumn("Date of Birth");
        tableModel.addColumn("Height");
        tableModel.addColumn("Name");
        tableModel.addColumn("Jersey#");
        tableModel.addColumn("PID");
        tableModel.addColumn("Team");
        tableModel.addColumn("City");

        tableModel2.addColumn("ID");
        tableModel2.addColumn("Bonus");
        tableModel2.addColumn("Player ID");
        tableModel2.addColumn("Length");
        tableModel2.addColumn("Value");
        tableModel2.addColumn("Signed Date");

        for(Player player : playersArray) {
            Object[] rowData = {player.getDebutYear(), player.getDateofBirth(),
                    player.getHeight(), player.getName(), player.getJerseyNum(),
                    player.getPid(), player.getTName(), player.getCity()};
            tableModel.addRow(rowData);
        }

        JTable playerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JTable contractTable = new JTable(tableModel2);
        JScrollPane scrollPane2 = new JScrollPane(contractTable);
        scrollPane2.setPreferredSize(new Dimension(600, 300));

        JButton delete = new JButton("Delete Player");
        delete.addActionListener((ActionEvent e) -> {
            int selectedRow = playerTable.getSelectedRow();
            if (selectedRow != -1) {
                Player selectedPlayer = playersArray[selectedRow];
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this player?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    delegate.deletePlayer(selectedPlayer.getPid());

                    tableModel.removeRow(selectedRow);

                    tableModel.fireTableDataChanged();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No player selected.");
            }
        });


        playerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    tableModel2.fireTableDataChanged();
                    int selectedRow = playerTable.getSelectedRow();
                    if (selectedRow != -1) {

                        Player selectedPlayer = playersArray[selectedRow];
                        Contract contract = selectedPlayer.getPlayerContract();

                        tableModel2.setRowCount(0);
                        Object[] rowData = {contract.getID(), contract.getBonus(), contract.getPid(),
                                contract.getLength(), contract.getValue(), contract.getSignedDate()};
                        tableModel2.addRow(rowData);
                    }
                }
            }
        });

        JPanel playerSelectPanel = new JPanel();
        JPanel contractPanel = new JPanel();
        playerSelectPanel.setBackground(new Color(22, 30, 51));
        contractPanel.setBackground(new Color(22, 30, 51));

        playerSelectPanel.setPreferredSize(new Dimension(600, 445));
        contractPanel.setPreferredSize(new Dimension(600, 100));

        playerSelectPanel.setLayout(new BoxLayout(playerSelectPanel, BoxLayout.Y_AXIS));
        contractPanel.setLayout(new BoxLayout(contractPanel, BoxLayout.Y_AXIS));

        playerSelectPanel.add(scrollPane);
        contractPanel.add(scrollPane2);

        playerSelectPanel.add(delete);

        centerPanel.add(playerSelectPanel);
        centerPanel.add(contractPanel);
    }

    public void makeCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(22, 30, 51));
        makePlayerPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
    }


    public void updateContent() {
        tableModel.setRowCount(0);
        tableModel2.setRowCount(0);

        Player[] playersArray = delegate.getPlayerInfo();
        for (Player player : playersArray) {
            Object[] rowData = {player.getDebutYear(), player.getDateofBirth(),
                    player.getHeight(), player.getName(), player.getJerseyNum(),
                    player.getPid(), player.getTName(), player.getCity()};
            tableModel.addRow(rowData);
        }

//        Contract[] contractsArray = delegate.getContractInfo();
//        for (Contract contract : contractsArray) {
//            Object[] rowData = {contract.getID(), contract.getBonus(), contract.getPid(), contract.getLength(), contract.getValue(), contract.getSignedDate()};
//            tableModel2.addRow(rowData);
//        }

//        tableModel2.fireTableDataChanged();

        if (playersArray.length > 0) {
            Player selectedPlayer = playersArray[0];
            Contract contract = selectedPlayer.getPlayerContract();
            Object[] rowData = {contract.getID(), contract.getBonus(), contract.getPid(),
                    contract.getLength(), contract.getValue(), contract.getSignedDate()};
            tableModel2.addRow(rowData);
        }

        tableModel.fireTableDataChanged();
        tableModel2.fireTableDataChanged();
    }


}
