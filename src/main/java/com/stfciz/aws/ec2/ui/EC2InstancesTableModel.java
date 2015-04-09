/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfciz.aws.ec2.ui;

import com.stfciz.aws.ec2.data.EC2Instance;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Bellevue
 */
public class EC2InstancesTableModel extends AbstractTableModel {

    private final List<EC2Instance> instances;

    /**
     * 
     * @param instances 
     */
    public EC2InstancesTableModel(List<EC2Instance> instances) {
        this.instances = instances;
    }

    @Override
    public int getRowCount() {
        return this.instances != null ? this.instances.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(final int i) {
        return null;//"Instance";
    }

    @Override
    public Class<?> getColumnClass(final int col) {
        return EC2Instance.class;
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        return true;
    }

    @Override
    public Object getValueAt(final int row, final int col) {
        if (this.instances == null) {
            return null;
        }
        return this.instances.get(row);
    }
}
