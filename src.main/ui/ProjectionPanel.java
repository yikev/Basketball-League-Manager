package main.ui;

import main.delegates.TerminalOperationDelegate;
import main.model.ListTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ProjectionPanel extends JPanel {

    private ListTableModel tableModel;

    JPanel playerSelectPanel;

    JScrollPane scrollPane;
    JTable playerTable;

    ArrayList<JCheckBox> checkBoxList;

    JButton project;

    JButton select;

    JComboBox<String> comboBox;

    JPanel checkBoxContainer;

    String[] tables;
    private static final int PANEL_WIDTH = 1080;
    private static final int PANEL_HEIGHT = 720;

    private TerminalOperationDelegate delegate;
    private CardLayout cl;
    private JPanel leagueManager;

    public ProjectionPanel(CardLayout cl, JPanel leagueManager, TerminalOperationDelegate delegate) {
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
        makeProjectionPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void repaint(JPanel centerPanel) {
        tables = delegate.getAllTables();

        comboBox = new JComboBox<>(tables);

        checkBoxContainer = new JPanel();

        checkBoxContainer.setLayout(new GridLayout(0, 1));

        checkBoxList = new ArrayList<>();

        select = new JButton("Select");

        select.addActionListener((ActionEvent e) -> {
            System.out.println("HIIIIIII");
            String selectedTable = (String) comboBox.getSelectedItem();

            if (selectedTable != null) {
                String[] attributes = delegate.getAllAttributes(selectedTable);

                JPanel checkBoxPanel = new JPanel();
                checkBoxPanel.setLayout(new GridLayout(0, 1));

                for (String attribute : attributes) {
                    JCheckBox checkBox = new JCheckBox(attribute);
                    checkBoxList.add(checkBox);
                    checkBoxPanel.add(checkBox);
                }

                centerPanel.add(checkBoxPanel);
                centerPanel.revalidate();
                centerPanel.repaint();

                centerPanel.add(checkBoxContainer);
                centerPanel.revalidate();
                centerPanel.repaint();

                comboBox.setVisible(false);
                select.setVisible(false);


            } else {
                JOptionPane.showMessageDialog(this, "No table selected.");
            }
        });

        project = new JButton("Project");

        project.addActionListener((ActionEvent e) -> {
            Component[] components = checkBoxContainer.getComponents();
            ArrayList<String> selectedAttributes = new ArrayList<>();

            for (JCheckBox checkBox : checkBoxList) {
                if (checkBox.isSelected()) {
                    selectedAttributes.add(checkBox.getText());
                }
            }

            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        selectedAttributes.add(checkBox.getText());
                    }
                }
            }

            centerPanel.removeAll();
            centerPanel.validate();
            centerPanel.repaint();



            String[] selectedAttributesArray = selectedAttributes.toArray(new String[0]);

            String selectedTable = (String) comboBox.getSelectedItem();

            Object[] attributes = delegate.addAttributes(selectedAttributesArray, selectedTable);



            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            System.out.println(selectedAttributesArray.length);

            for (int i = 0; i < selectedAttributesArray.length; i++) {
                tableModel.addColumn(selectedAttributesArray[i]);
            }

            for(int i = 0; i < attributes.length; i++) {
                Object[] rowData = new Object[selectedAttributesArray.length];

                for (int j = 0; j < selectedAttributesArray.length; j++) {
                    rowData[j] = attributes[i];
                    i++;
                }

                i --;
                tableModel.addRow(rowData);
            }


            checkBoxContainer.removeAll();
            checkBoxContainer.revalidate();
            checkBoxContainer.repaint();

            repaint(centerPanel);

            centerPanel.remove(checkBoxContainer);
            centerPanel.revalidate();
            centerPanel.repaint();

            comboBox.setVisible(true);
            select.setVisible(true);
        });

        playerTable = new JTable(tableModel);

        scrollPane = new JScrollPane(playerTable);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        playerSelectPanel = new JPanel();
        playerSelectPanel.setBackground(new Color(22, 30, 51));

        playerSelectPanel.setPreferredSize(new Dimension(900, 445));

        playerSelectPanel.setLayout(new BoxLayout(playerSelectPanel, BoxLayout.Y_AXIS));

        playerSelectPanel.add(comboBox);
        playerSelectPanel.add(select);
        playerSelectPanel.add(project);
        playerSelectPanel.add(scrollPane);

        centerPanel.add(playerSelectPanel);



    }

    public void makeProjectionPanel(JPanel centerPanel) {



        tables = delegate.getAllTables();

        comboBox = new JComboBox<>(tables);

        checkBoxContainer = new JPanel();

        checkBoxContainer.setLayout(new GridLayout(0, 1));

        checkBoxList = new ArrayList<>();

        select = new JButton("Select");
        select.addActionListener((ActionEvent e) -> {
            System.out.println("HIIIIIII");
            String selectedTable = (String) comboBox.getSelectedItem();

            if (selectedTable != null) {
                String[] attributes = delegate.getAllAttributes(selectedTable);

                JPanel checkBoxPanel = new JPanel();
                checkBoxPanel.setLayout(new GridLayout(0, 1));

                for (String attribute : attributes) {
                    JCheckBox checkBox = new JCheckBox(attribute);
                    checkBoxList.add(checkBox);
                    checkBoxPanel.add(checkBox);
                }

                centerPanel.add(checkBoxPanel);
                centerPanel.revalidate();
                centerPanel.repaint();

                centerPanel.add(checkBoxContainer);
                centerPanel.revalidate();
                centerPanel.repaint();

                comboBox.setVisible(false);
                select.setVisible(false);


            } else {
                JOptionPane.showMessageDialog(this, "No table selected.");
            }
        });

        project = new JButton("Project");

        project.addActionListener((ActionEvent e) -> {
            Component[] components = checkBoxContainer.getComponents();
            ArrayList<String> selectedAttributes = new ArrayList<>();

            for (JCheckBox checkBox : checkBoxList) {
                if (checkBox.isSelected()) {
                    selectedAttributes.add(checkBox.getText());
                }
            }

            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        selectedAttributes.add(checkBox.getText());
                    }
                }
            }

            centerPanel.removeAll();
            centerPanel.validate();
            centerPanel.repaint();



            String[] selectedAttributesArray = selectedAttributes.toArray(new String[0]);

            String selectedTable = (String) comboBox.getSelectedItem();

            Object[] attributes = delegate.addAttributes(selectedAttributesArray, selectedTable);



            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            System.out.println(selectedAttributesArray.length);

            for (int i = 0; i < selectedAttributesArray.length; i++) {
                tableModel.addColumn(selectedAttributesArray[i]);
            }

            for(int i = 0; i < attributes.length; i++) {
                Object[] rowData = new Object[selectedAttributesArray.length];

                for (int j = 0; j < selectedAttributesArray.length; j++) {
                    rowData[j] = attributes[i];
                    i++;
                }

                i --;
                tableModel.addRow(rowData);
            }


            checkBoxContainer.removeAll();
            checkBoxContainer.revalidate();
            checkBoxContainer.repaint();

            repaint(centerPanel);

            centerPanel.remove(checkBoxContainer);
            centerPanel.revalidate();
            centerPanel.repaint();

            comboBox.setVisible(true);
            select.setVisible(true);
        });

        playerTable = new JTable(tableModel);
        scrollPane = new JScrollPane(playerTable);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        playerSelectPanel = new JPanel();
        playerSelectPanel.setBackground(new Color(22, 30, 51));

        playerSelectPanel.setPreferredSize(new Dimension(900, 445));

        playerSelectPanel.setLayout(new BoxLayout(playerSelectPanel, BoxLayout.Y_AXIS));

        playerSelectPanel.add(comboBox);
        playerSelectPanel.add(select);
        playerSelectPanel.add(project);
        playerSelectPanel.add(scrollPane);

        centerPanel.add(playerSelectPanel);
    }

}
