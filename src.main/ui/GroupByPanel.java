package main.ui;

import main.delegates.TerminalOperationDelegate;
import main.model.ListTableModel;
import main.model.SponsorSponsoredAmount;

import javax.swing.*;
import java.awt.*;

public class GroupByPanel extends JPanel{
    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;
    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;
    private ListTableModel tableModel;
    private JTable sponsorsTable;
    public GroupByPanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setForeground(Color.WHITE);
        this.setBackground(new Color(22, 30, 51));
        this.delegate = delegate;
        this.cl = cl;
        this.leagueManager = leagueManager;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tableModel = new ListTableModel();
        sponsorsTable = new JTable(tableModel);
        makeOperationPanel();

        setElements();
    }

    private void setElements() {
        removeAll();
        makeBackMenuButton();
        makeOperationPanel();
        revalidate();
        repaint();
    }

    public void makeBackMenuButton() {
        JButton back = new JButton("Back to the Menu");
        back.addActionListener(e -> cl.show(leagueManager, "menu"));
        back.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(back);
    }

    private void makeOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 30));
        operationPanel.setBackground(new Color(22, 30, 51));
        JLabel label1 = new JLabel("Find Total Sponsored Amount by Sponsor");
        label1.setForeground(Color.WHITE);
        JButton groupByButton = new JButton("Calculate Sponsored Amounts");
        groupByButton.addActionListener(e -> groupBySponsor());

        operationPanel.add(label1);
        operationPanel.add(groupByButton);
        add(operationPanel);

        JScrollPane scrollPane = new JScrollPane(sponsorsTable);
        add(scrollPane);
        sponsorsTable.setEnabled(false);
        sponsorsTable.setFocusable(false);
        sponsorsTable.setVisible(false);
    }

    public void createTable(SponsorSponsoredAmount[] sponsors) {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.addColumn("Sponsor");
        tableModel.addColumn("Sponsor Total");
        for (SponsorSponsoredAmount sponsor : sponsors) {
            Object[] rowData = {
                    sponsor.getSponsor(),
                    sponsor.getSponsorAmt()
            };
            tableModel.addRow(rowData);
        }
        sponsorsTable.setVisible(true);
    }

    public void groupBySponsor() {
        SponsorSponsoredAmount[] sponsors = delegate.getSponsoredAmounts();
        createTable(sponsors);
    }

}
