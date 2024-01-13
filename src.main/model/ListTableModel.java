package main.model;

import javax.swing.table.DefaultTableModel;

public class ListTableModel extends DefaultTableModel {
    public ListTableModel() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
