/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfciz.aws.ec2.data;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author Bellevue
 */
public class EC2InstancesManager {

    public final static String CREDENTIALS_FILE = System.getProperty("user.home") +  File.separator + ".ec2" + File.separator + "AwsCredentials.properties";

    private AWSCredentials credentials = null;

    private AmazonEC2Client amazonEC2Client = null;

    private List<String> regions;

    /**
     *
     */
    public EC2InstancesManager() throws IOException {
        loadCredentials();
        final Enumeration<String> keys = ResourceBundle.getBundle("regions").getKeys();
        List<String> regionValues = new ArrayList<>();
        while (keys.hasMoreElements()) {
            regionValues.add(keys.nextElement());
        }
        this.regions = Collections.unmodifiableList(regionValues);
    }

    /**
     * 
     * @param instanceId 
     */
    public void startInstance(String[] instanceIds) {
        StartInstancesRequest request = new StartInstancesRequest();
        request.setInstanceIds(Arrays.asList(instanceIds));
        this.amazonEC2Client.startInstances(request);
    }
    
        /**
     * 
     * @param instanceId 
     */
    public void stopInstance(String[] instanceIds) {
        StopInstancesRequest request = new StopInstancesRequest();
        request.setInstanceIds(Arrays.asList(instanceIds));
        this.amazonEC2Client.stopInstances(request);
    }
    
    /**
     *
     * @return
     */
    public boolean hasCredentials() {
        return this.credentials != null;
    }

    /**
     *
     * @return
     */
    public String getAWSAccessKeyId() {
        return this.credentials != null ? this.credentials.getAWSAccessKeyId() : null;
    }

    /**
     *
     * @return
     */
    public String getAWSSecretKey() {
        return this.credentials != null ? this.credentials.getAWSSecretKey() : null;
    }

    /**
     *
     * @return
     */
    public List<String> getRegions() {
        return this.regions;
    }

    /**
     *
     * @param accessKeyId
     * @param secretKey
     * @throws IOException
     */
    public void save(String accessKeyId, String secretKey) throws IOException {
        Properties properties = new Properties();
        properties.put("accessKey", accessKeyId);
        properties.put("secretKey", secretKey);
        
        File parent = new File(CREDENTIALS_FILE).getParentFile();
        
        if (!parent.exists()) {
          parent.mkdirs();
        }
        
        properties.store(new FileOutputStream(new File(CREDENTIALS_FILE)), null);

        loadCredentials();
    }

    /**
     *
     * @param region
     * @return
     */
    public List<EC2Instance> getEC2Instances(String region) {
        if (this.credentials == null) {
            return null;
        }
        List<EC2Instance> ec2Instances;
        ec2Instances = new ArrayList<>();
        if (region != null) {
            this.amazonEC2Client.setRegion(Region.getRegion(Regions.fromName(region)));
        }
        final DescribeInstancesResult describeInstances = this.amazonEC2Client.describeInstances();
        final List<Reservation> reservations = describeInstances.getReservations();
        for (Reservation reservation : reservations) {
            final List<Instance> instances = reservation.getInstances();
            for (Instance instance : instances) {
                EC2Instance ec2Instance = new EC2Instance();
                ec2Instance.setId(instance.getInstanceId());
                List<Tag> tags = instance.getTags();

                if (tags != null && !tags.isEmpty()) {
                    StringBuilder name = new StringBuilder();
                    for (Tag tag : tags) {
                        if (name.length() > 0) {
                            name.append(", ");
                        }
                        name.append(tag.getValue());
                    }
                    ec2Instance.setName(name.toString());
                }
                ec2Instance.setPublicDnsName(instance.getPublicDnsName());
                final InstanceState state = instance.getState();
                ec2Instance.setStatus(state.getName());
                ec2Instance.setStatusCode(state.getCode());
                ec2Instances.add(ec2Instance);
            }
        }

        return ec2Instances;
    }

    /**
     *
     * @throws IOException
     */
    private void loadCredentials() throws IOException {
        File propertiesCredentialsFile = new File(CREDENTIALS_FILE);
        if (propertiesCredentialsFile.exists()) {
            this.credentials = new PropertiesCredentials(propertiesCredentialsFile);
            this.amazonEC2Client = new AmazonEC2Client(this.credentials);
        }
    }
}
