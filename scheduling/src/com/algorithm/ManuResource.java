package com.algorithm;

import java.util.ArrayList;

public class ManuResource {

    public String name;
    public double xAxis;
    public double yAxis;

    public ArrayList<ServiceType> capacityTypes;
    public ArrayList<Double> minServiceTime;
    public ArrayList<Double> maxServiceTime;

    //该制造资源的全部工作时间段
    public ArrayList<WorkingTime> workingTimes;

    public ArrayList<ManuTask> manuQueue;

    public ManuResource(String name,double xAxis,double yAxis){
        this.name=name;
        this.xAxis=xAxis;
        this.yAxis=yAxis;
        this.capacityTypes=new ArrayList<ServiceType>();
        this.minServiceTime=new ArrayList<Double>();
        this.maxServiceTime=new ArrayList<Double>();
        this.manuQueue=new ArrayList<ManuTask>();
        this.workingTimes=new ArrayList<WorkingTime>();
    }


//当任务到达生产资源时，需要修改任务的参数和当前生产资源的参数
    public void putOnProduction(ManuTask tmpManuTask){
        tmpManuTask.onRoadTime=0.0;
        //删除多余的节点
        Graph tmpGraph=tmpManuTask.graph;
        Vertex preV=tmpGraph.allVertexs.get(0);
        Integer count=preV.allEdges.size();
        tmpGraph.allVertexs.remove(0);
        for(int x=0;x<count-1;++x){
            if(tmpGraph.allVertexs.get(0).resource==this){
                tmpGraph.allVertexs.remove(1);
            }
            else{
                tmpGraph.allVertexs.remove(0);
            }
        }
        //设置remainTime，表示该任务此工序需要花费的时间
        Integer index=capacityTypes.indexOf(tmpManuTask.getTmpServiceType());
        Double timeMin=minServiceTime.get(index);
        Double timeMax=maxServiceTime.get(index);
        tmpManuTask.remainTime=(Global.rand.nextDouble())*(timeMax-timeMin)+timeMin;
        //设置isWaitingInQueue，如果生产队列为空，那么该任务能够立即投入生产，则isWaitingInQueue为false,否则需要在
        //队列中等待
        if(manuQueue.size()==0){
            tmpManuTask.isWaitingInQueue=false;
            //当该任务投入生产时，记录生产的开始时间
            workingTimes.add(new WorkingTime(Global.time,Global.time,tmpManuTask));
        }
        else{
            tmpManuTask.isWaitingInQueue=true;
        }


        //添加进生产队列
        manuQueue.add(tmpManuTask);

    }

//当任务生产完成，离开该生产资源时，需要修改任务的参数和当前生产资源的参数
    //此时需要移除的任务为生产队列中第一个任务，即tmpManuTask==manuQueue.get(0)
    //同时设置后面等待的任务的参数
    public ManuTask outOfProduction(ManuTask tmpManuTask){
        //当前的任务的当前生产步骤完成，进入下一步骤
        tmpManuTask.tmpSequence++;
        //已经到达目的地，删除目的地
        tmpManuTask.aimResource=null;
        tmpManuTask.remainTime=0.0;
        //移除该任务
        manuQueue.remove(0);
        //该任务离开时，做时间记录
        workingTimes.get(workingTimes.size()-1).rightTime=Global.time;
        //生产队列中等待的任务投入生产
        if(manuQueue.size()!=0) {
            ManuTask newManuTask = manuQueue.get(0);
            newManuTask.isWaitingInQueue = false;
            //当该任务投入生产时，记录生产的开始时间
            workingTimes.add(new WorkingTime(Global.time,Global.time,newManuTask));
        }
        return tmpManuTask;
    }

//当增加新任务，需要完成tmpServiceType服务时，大概需要的时间，包括需要等待的时间和服务时间
    public Double expectWaitingTime(ServiceType tmpServiceType){
        Integer index=capacityTypes.indexOf(tmpServiceType);
        Double result=(minServiceTime.get(index)+maxServiceTime.get(index))/2;
        for(ManuTask tmpManuTask : manuQueue){
            ServiceType ser=tmpManuTask.getTmpServiceType();
            index=capacityTypes.indexOf(ser);
            result+=(minServiceTime.get(index)+maxServiceTime.get(index))/2;
        }
        return result;
    }
    //计算该生产资源与otherResource的物流时间
    public Double roadCost(ManuResource otherResource){
        return Math.sqrt((xAxis-otherResource.xAxis)*(xAxis-otherResource.xAxis)+
                (yAxis-otherResource.yAxis)*(yAxis-otherResource.yAxis));
    }

}
