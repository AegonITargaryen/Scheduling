package com.algorithm;

import java.util.ArrayList;

public class ServiceType {
    public String serviceTypeName;
    public ArrayList<ManuResource> capacityResource;

    public ServiceType(String name){
        this.serviceTypeName=name;
        this.capacityResource=new ArrayList<ManuResource>();
    }

    public void addResource(ManuResource newResource){
        this.capacityResource.add(newResource);
    }
}
