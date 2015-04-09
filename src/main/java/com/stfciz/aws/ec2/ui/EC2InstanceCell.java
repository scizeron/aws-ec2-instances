/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfciz.aws.ec2.ui;

import com.stfciz.aws.ec2.data.EC2Instance;
import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Bellevue
 */
public class EC2InstanceCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private final EC2InstanceCellComponent eC2InstanceCellComponent;

    /**
     * 
     * @param ec2InstancesTableEventHandler 
     */
    public EC2InstanceCell(EC2InstancesTableEventHandler ec2InstancesTableEventHandler) {
        this.eC2InstanceCellComponent = new EC2InstanceCellComponent(ec2InstancesTableEventHandler);
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
   
    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object value, boolean isSelected, int row, int column) {
        return updateComponent(jtable, value, isSelected, row, column);
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         return updateComponent(jtable, value, isSelected || hasFocus, row, column);
    }
    
    /**
     * 
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return 
     */
    private Component updateComponent(JTable jtable, Object value, boolean isSelected, int row, int column) {
        final EC2Instance instance = (EC2Instance)value;
        this.eC2InstanceCellComponent.setEC2Instance(instance);
        this.eC2InstanceCellComponent.setBackground(isSelected ? Color.CYAN : Color.WHITE);
        return this.eC2InstanceCellComponent;
    }

}
