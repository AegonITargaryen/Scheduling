package com.algorithm;

import java.util.ArrayList;

public class TaskType {
    public String taskTypeName;
    public ArrayList<ServiceType> sequence;   //该类型任务的生产序列

    public TaskType(String name, ArrayList<ServiceType> sequence){
        this.taskTypeName=name;
        this.sequence=sequence;
    }
}
