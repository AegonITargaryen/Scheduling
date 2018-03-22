package com.algorithm;

import java.io.*;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Random;

public class Global {
    static ArrayList<ServiceType> allServiceTypes=new ArrayList<ServiceType>();
    static ArrayList<TaskType> allTaskTypes= new ArrayList<TaskType>();
    static ArrayList<ManuResource> allManuResources=new ArrayList<ManuResource>();
    static ArrayList<ManuTask> allManuTasks=new ArrayList<ManuTask>();
    static ArrayList<ManuTask> finishedManuTasks=new ArrayList<ManuTask>();
    //每一轮迭代的时候需要处理的ManuTask
    static ArrayList<ManuTask> willBeDisposedManuTasks=new ArrayList<ManuTask>();
    static Random rand=new Random(42);
    static Double time=0.0;

    static String allServiceTypesPath="src\\test1\\allServiceTypes.txt";
    static String allTaskTypesPath="src\\test1\\allTaskTypes.txt";
    static String manuResMessagesPath="src\\test1\\manuResMessages.txt";
    static String allManuTasksPath="src\\test1\\allManuTasks.txt";

    //读取data文件夹中的allServiceTypes文件，包含调度系统可提供服务的类型
    static void readServiceTypes() {
        try {
            File file = new File(allServiceTypesPath);
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String str="";
            while((str=reader.readLine())!=null){
                String[] tmpServiceTypes = str.split("\\s+");
                for(String ss : tmpServiceTypes){
                    allServiceTypes.add(new ServiceType(ss));
                }
            }
        }catch (FileNotFoundException e){System.out.println("Can't find allServiceTypes.txt");}
        catch (IOException e){}
    }

    //读取data文件夹中allTaskTypes文件，包含调度系统可生产的任务类型
    static void readTaskTypes(){
        try {
            File file = new File(allTaskTypesPath);
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String str="";
            while((str=reader.readLine())!=null){
                String[] tmpTaskTypes = str.split("\\s+");
                String taskTypeName=tmpTaskTypes[0];
                ArrayList<ServiceType> taskTypeSequence=new ArrayList<ServiceType>();
                for(int i=1;i!=tmpTaskTypes.length;++i){
                    Integer index=Integer.parseInt(tmpTaskTypes[i]);
                    taskTypeSequence.add(Global.allServiceTypes.get(index-1));
                }
                allTaskTypes.add(new TaskType(taskTypeName,taskTypeSequence));
            }
        }catch (FileNotFoundException e){System.out.println("Can't find allTaskTypes.txt");}
        catch (IOException e){}
    }

    //读取data文件夹中的manuResMessages文件，包含调度系统的所有生产资源，该文件每一行各项分别为：
    //资源名称、x坐标、y坐标、该资源可提供的服务类型、最小和最大服务时间
    static void readManuResources(){
        try {
            File file = new File(manuResMessagesPath);
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String str="";
            while((str=reader.readLine())!=null){
                String[] tmpLine = str.split("\\s+");
                int tmpManuRes=0;
                for(tmpManuRes=0;tmpManuRes!=allManuResources.size();++tmpManuRes){
                    ManuResource tmpManuResource=allManuResources.get(tmpManuRes);
                    if(tmpManuResource.name.equals(tmpLine[0])&&
                           tmpManuResource.xAxis==Double.parseDouble(tmpLine[1])&&
                            tmpManuResource.yAxis==Double.parseDouble(tmpLine[2])){
                        break;
                    }
                }
                if(tmpManuRes==allManuResources.size()){
                    allManuResources.add(new ManuResource(tmpLine[0],
                            Double.parseDouble(tmpLine[1]),Double.parseDouble(tmpLine[2])));
                }
                ManuResource tmpManuResource=allManuResources.get(tmpManuRes);
                tmpManuResource.capacityTypes.add(allServiceTypes.get(Integer.parseInt(tmpLine[3])-1));

                tmpManuResource.minServiceTime.add(
                        Math.min(Double.parseDouble(tmpLine[4]),Double.parseDouble(tmpLine[5])));
                tmpManuResource.maxServiceTime.add(
                        Math.max(Double.parseDouble(tmpLine[4]),Double.parseDouble(tmpLine[5])));

            }
            pretreatment();

        }catch (FileNotFoundException e){System.out.println("Can't find manuResMessages.txt");}
        catch (IOException e){}
    }

    //读取data文件夹中的allManuTasks文件，包含调度系统所需要进行调度的所有生产任务
    static void readManuTasks(){
        try {
            File file = new File(allManuTasksPath);
            BufferedReader reader=new BufferedReader(new FileReader(file));
            String str="";
            while((str=reader.readLine())!=null){
                String[] tmpManuTasks = str.split("\\s+");
                String tmpTaskTypeName=tmpManuTasks[1];
                String tmpTaskName=tmpManuTasks[0];
                for(TaskType tmpTaskType : allTaskTypes){
                    if(tmpTaskType.taskTypeName.equals(tmpTaskTypeName)){
                        allManuTasks.add(new ManuTask(tmpTaskType,tmpTaskName));
                        break;
                    }
                }
            }
        }catch (FileNotFoundException e){System.out.println("Can't find allManuTasks.txt");}
        catch (IOException e){}
    }

    //数据预处理,将生产资源根据其可提供的每一个服务类型进行分类，并存入相应的服务类型中。
    // 用于readManuResources()
    static private void pretreatment(){
        for(ManuResource tmpManuResource : Global.allManuResources){
            for(ServiceType tmpCapacityType : tmpManuResource.capacityTypes){
                tmpCapacityType.addResource(tmpManuResource);
            }
        }
    }



    //调度全过程
    static void scheduling(){
        ArrayList<ManuTask> taskOnRoad=new ArrayList<ManuTask>();
        while(allManuTasks.size()!=0){
            //生产任务有三种状态 在物流中，在生产队列等待生产中，正在生产中。需要排除在生产队列中等待的任务，在剩下
            //的任务中寻找最先需要进行处理的任务
            findNextManuTask();
            //刷新时间
            refreshTime();
            //判断该任务是在物流中的任务还是在生产中的任务
            for(ManuTask nextManuTask : willBeDisposedManuTasks) {
                if (nextManuTask.onRoadTime != 0.0) {
                    manuOnRoad(nextManuTask);
                } else {
                    //当不是物流中的任务，则是生产完毕的任务，那么需要为下一道工序选择生产资源
                    chooseManuResource(nextManuTask);
                }
            }
        }
//        ArrayList<ManuTask> taskOnRoad=new ArrayList<ManuTask>();
//        for(int x=1;x!=31;++x){
//            //生产任务有三种状态 在物流中，在生产队列等待生产中，正在生产中。需要排除在生产队列中等待的任务，在剩下
//            //的任务中寻找最先需要进行处理的任务
//            ManuTask nextManuTask=findNextManuTask();
//            //判断该任务是在物流中的任务还是在生产中的任务
//            if(nextManuTask.onRoadTime!=0.0){
//                manuOnRoad(nextManuTask);
//            }
//            else {
//
//            }
//        }
    }
    //可能有多个生产任务需要同时进行处理          ！！！！！！！//
    //寻找下一个要处理的生产任务
    static  private ArrayList<ManuTask> findNextManuTask(){
        //生产任务有三种状态 在物流中，在生产队列等待生产中， 正在生产中
        willBeDisposedManuTasks.clear();
        for(ManuTask tmpManuTask : allManuTasks){
            if(tmpManuTask.isWaitingInQueue)
                continue;
            if(willBeDisposedManuTasks.size()==0) {
                willBeDisposedManuTasks.add(tmpManuTask);
                continue;
            }
            ManuTask nextManuTask=willBeDisposedManuTasks.get(0);
            Double timeN=(nextManuTask.onRoadTime==0.0)? nextManuTask.remainTime:nextManuTask.onRoadTime;
            Double timeT=(tmpManuTask.onRoadTime==0.0)? tmpManuTask.remainTime:tmpManuTask.onRoadTime;
            if(timeN>timeT){
                willBeDisposedManuTasks.clear();
                willBeDisposedManuTasks.add(tmpManuTask);
            }
            else if(timeN.equals(timeT)){
                willBeDisposedManuTasks.add(tmpManuTask);
            }
        }
        return willBeDisposedManuTasks;
    }

    static private void refreshTime(){
        Double addTime=willBeDisposedManuTasks.get(0).onRoadTime==0?
                willBeDisposedManuTasks.get(0).remainTime:willBeDisposedManuTasks.get(0).onRoadTime;
        time+=addTime;
        for(ManuTask tmpManuTask : allManuTasks){
            if((!tmpManuTask.isWaitingInQueue)&&(willBeDisposedManuTasks.indexOf(tmpManuTask)==-1)){
                if(tmpManuTask.onRoadTime!=0.0){
                    tmpManuTask.onRoadTime-=addTime;
                }
                else{
                    tmpManuTask.remainTime-=addTime;
                }
            }
        }
    }

    static private void manuOnRoad(ManuTask nextManuTask){

        //处理当前的生产任务
        nextManuTask.onRoadTime=0.0;
        nextManuTask.aimResource.putOnProduction(nextManuTask);
    }

    static private void chooseManuResource(ManuTask nextManuTask){

        //处理当前任务，该任务需要从当前的资源中离开
        if(nextManuTask.aimResource!=null){
            nextManuTask.aimResource.outOfProduction(nextManuTask);
        }
        //如果该资源生产完毕，则删除该资源
        if(nextManuTask.isFinished()){
            Global.allManuTasks.remove(nextManuTask);
            finishedManuTasks.add(nextManuTask);
            return;
        }

        //更新路径图以便于寻找最优资源
        nextManuTask.refreshGraph();
        //搜索最优的生产资源
        nextManuTask.searchAimResource();
    }


}
