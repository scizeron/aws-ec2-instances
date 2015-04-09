/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfciz.aws.ec2.ui;

import com.stfciz.aws.ec2.data.EC2Instance;
import com.stfciz.aws.ec2.data.EC2InstancesManager;
import javax.swing.JTable;

/**
 *
 * @author Bellevue
 */
public class EC2InstancesTableEventHandler {

    private final EC2InstancesManager ec2InstancesManager;
    
    private final JTable tabInstances;

    /**
     *
     * @param tabInstances
     * @param ec2InstancesManager
     */
    public EC2InstancesTableEventHandler(JTable tabInstances, EC2InstancesManager ec2InstancesManager) {
        this.tabInstances = tabInstances;
        this.ec2InstancesManager = ec2InstancesManager;
    }

    /**
     *
     * @param action
     * @param instance
     */
    void action(String action, EC2Instance instance) {
        if (null != action) switch (action) {
            case "start":
                this.ec2InstancesManager.startInstance(new String[]{instance.getId()});
                break;
            case "stop":
                this.ec2InstancesManager.stopInstance(new String[]{instance.getId()});
                break;
        }
        tabInstances.setModel(new EC2InstancesTableModel(this.ec2InstancesManager.getEC2Instances(null)));
    }
}
