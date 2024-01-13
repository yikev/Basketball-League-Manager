package main.ui;

import main.Exception.InvalidBonusException;
import main.Exception.InvalidLengthException;
import main.Exception.NullContractException;
import main.delegates.TerminalOperationDelegate;
import main.model.Contract;
import main.model.ListTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class UpdatePanel extends JPanel {
    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;

    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;

    private JTextField contractLength;
    private JPanel operationPanel;
    private JTextField contractBonus;
    private Contract selectedContract;


    public UpdatePanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.delegate = delegate;
        this.cl = cl;
        this.leagueManager = leagueManager;

        setLayout(new BorderLayout());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                setElements();
            }
        });
    }


    private void setElements() {
        removeAll();
        makeBackMenuButton();
        makeCenterPanel();
        makeOperationPane(0, 0);
        makeUpdateButton();
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

    public void makeUpdateButton() {
        JButton updateButton = new JButton("Update Contract");
        updateButton.addActionListener((ActionEvent e) -> {
            updateContract();
        });
        add(updateButton, BorderLayout.SOUTH);
    }

    private void updateContract() {
        String length = contractLength.getText();
        String bonus = contractBonus.getText();
        try {
            boolean updated = delegate.updateContract(selectedContract, length, bonus);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Contract " + selectedContract.getID() + " updated.");
                setElements();
            } else {
                JOptionPane.showMessageDialog(this, "Unable to update contract" + selectedContract.getID() );
            }
        } catch (InvalidBonusException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (NullContractException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (InvalidLengthException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public void makeCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(getBackground());
        makeContractPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void makeContractPanel(JPanel centerPanel) {
        Contract[] contractsArray = delegate.getContractInfo();
        ListTableModel tableModel = new ListTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Bonus");
        tableModel.addColumn("Player ID");
        tableModel.addColumn("Length");
        tableModel.addColumn("Value");
        tableModel.addColumn("Signed Date");

        for (Contract contract : contractsArray) {
            Object[] rowData = {contract.getID(), contract.getBonus(), contract.getPid(), contract.getLength(), contract.getValue(), contract.getSignedDate()};
            tableModel.addRow(rowData);
        }

        JTable contractTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contractTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        ListSelectionModel selectionModel = contractTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a selection listener to handle single selection
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = contractTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedContract = getContractFromSelectedRow(contractTable, selectedRow);
                        if (operationPanel != null) {
                            centerPanel.remove(operationPanel);
                        }
                        makeOperationPane(selectedContract.getBonus(), selectedContract.getLength());
                        centerPanel.add(operationPanel);
                        revalidate();
                        repaint();
                    }
                }
            }
        });

        JPanel contractSelectPanel = new JPanel();
        contractSelectPanel.setPreferredSize(new Dimension(600, 450));
        contractSelectPanel.setLayout(new BoxLayout(contractSelectPanel, BoxLayout.Y_AXIS));
        contractSelectPanel.add(scrollPane);
        centerPanel.add(contractSelectPanel);
    }

    private Contract getContractFromSelectedRow(JTable table, int selectedRow) {
        int id = (int) table.getValueAt(selectedRow, 0);

        int bonus = (int) table.getValueAt(selectedRow, 1);
        int length = (int) table.getValueAt(selectedRow, 3);
        return new Contract(id, bonus, length);
    }

    public void makeOperationPane(int curr_bonus, int curr_length) {
        operationPanel = new JPanel();
        operationPanel.setBackground(getBackground());
        operationPanel.setLayout(new GridLayout(2, 2));
        operationPanel.setPreferredSize(new Dimension(200, 60));

        JLabel lengthLabel = new JLabel("Contract length");
        JLabel bonusLabel = new JLabel("Bonus");
        lengthLabel.setForeground(Color.WHITE);
        bonusLabel.setForeground(Color.WHITE);

        contractLength = new JTextField(Integer.toString(curr_length));
        contractLength.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
        contractLength.setPreferredSize(new Dimension(100, 30));
        contractBonus = new JTextField(Integer.toString(curr_bonus));
        contractBonus.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
        contractBonus.setPreferredSize(new Dimension(100, 30));
        operationPanel.add(lengthLabel);
        operationPanel.add(bonusLabel);
        operationPanel.add(contractLength);
        operationPanel.add(contractBonus);
    }
}