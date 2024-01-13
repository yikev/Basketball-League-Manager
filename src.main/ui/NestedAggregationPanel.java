package main.ui;

import main.delegates.TerminalOperationDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class NestedAggregationPanel extends JPanel{
    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;
    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;
    private JLabel value;
    public NestedAggregationPanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setForeground(Color.WHITE);
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

    private void setElements() {
        removeAll();
        makeBackMenuButton();
        makeOperationPanel();
        makeDisplayPanel();
        updateValue(1);
        revalidate();
        repaint();
    }

    private void makeOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 30));
        operationPanel.setBackground(getBackground());
        JLabel label1 = new JLabel("Display the average contract value of players with higher-than-average contract and with ");
        label1.setForeground(Color.WHITE);
        Integer[] items = {1, 2, 3, 4, 5};
        JComboBox<Integer> lengths = new JComboBox<>(items);
        lengths.setPreferredSize(new Dimension(200, 30));
        lengths.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedLength = (int) lengths.getSelectedItem();
                System.out.println("Selected Item: " + selectedLength);
                updateValue(selectedLength);
            }
        });

        JLabel label2 = new JLabel(" year(s) remain on their contract");
        label2.setForeground(Color.WHITE);
        operationPanel.add(label1);
        operationPanel.add(lengths);
        operationPanel.add(label2);
        add(operationPanel);
    }

    public void makeDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setBackground(getBackground());
        displayPanel.setPreferredSize(new Dimension(200, 100));
        value = new JLabel();
        value.setForeground(Color.WHITE);
        value.setFont(new Font("Serif", Font.PLAIN, 50));
        displayPanel.add(value);
        displayPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(displayPanel);
    }

    public void updateValue(int selectedLength) {
        int avgValue = delegate.getHigherThanAvgContractByLength(selectedLength);
        System.out.println("The average value is " + avgValue);
        value.setText("$" + String.valueOf(avgValue));
        revalidate();
        repaint();
    }

    public void makeBackMenuButton() {
        JButton back = new JButton("Back to the Menu");
        back.addActionListener(e -> cl.show(leagueManager, "menu"));
        back.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(back);
    }

}
