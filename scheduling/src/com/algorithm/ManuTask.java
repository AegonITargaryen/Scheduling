package com.algorithm;

import java.util.ArrayList;

public class ManuTask {
    public TaskType taskType;
    public String name;
    public Integer tmpSequence;//当前生产进度
    public Graph graph=new Graph();
    public double remainTime;//当前步骤还剩下的生产时间
    public boolean isWaitingInQueue;
    public double onRoadTime;
    public ManuResource aimResource;

    public ManuTask(TaskType taskType,String name){
        this.taskType=taskType;
        this.name=name;
        this.tmpSequence=0;
        this.remainTime=0.0;
        this.onRoadTime=0.0;
        this.isWaitingInQueue=false;
        this.aimResource=null;
        makeGraph(taskType);
    }

    public void makeGraph(TaskType taskType){
        ArrayList<ServiceType> sequence=taskType.sequence;
        ArrayList<Vertex> preVertexs=new ArrayList<Vertex>();
        ArrayList<Vertex> nextVertexs=new ArrayList<Vertex>();
        preVertexs.add(new Vertex("start"));
        for(ServiceType tmpServiceType : sequence){
            ArrayList<ManuResource> nextResources=tmpServiceType.capacityResource;
            for(ManuResource tmp : nextResources){
                nextVertexs.add(new Vertex(tmp));
            }
            for(Vertex tmpPre : preVertexs){
                for(Vertex tmpNext : nextVertexs){
                    tmpPre.allEdges.add(new Edge(tmpNext,0.0));
                }
                this.graph.allVertexs.add(tmpPre);
            }
            preVertexs=nextVertexs;
            nextVertexs=new ArrayList<Vertex>();
        }
        Vertex endVertex=new Vertex("end");
        for(Vertex tmpPre : preVertexs){
            tmpPre.allEdges.add(new Edge(endVertex,0.0));
            this.graph.allVertexs.add(tmpPre);
        }
        this.graph.allVertexs.add(endVertex);
    }

    public ServiceType getTmpServiceType(){
        return taskType.sequence.get(tmpSequence);
    }

    //更新图的权值
    public void refreshGraph(){
        Integer index=tmpSequence;
        ArrayList<Vertex> preV=new ArrayList<Vertex>();
        preV.add(graph.allVertexs.get(0));
        while(taskType.sequence.size()!=index){
            ServiceType tmpServiceType=taskType.sequence.get(index);
            for(Vertex tmpV : preV){
                for(Edge tmpE : tmpV.allEdges){
                    //物流消耗时间+预期等待时间
                    tmpE.value=tmpV.roadCost(tmpE.terminus)+
                            tmpE.terminus.expectWaitingTime(tmpServiceType);
                }
                tmpV.shortestPathLength=Double.MAX_VALUE;
                tmpV.preVertex=null;
            }
            ArrayList<Edge> edges=preV.get(0).allEdges;
            preV.clear();
            for(Edge tmpE : edges){
                preV.add(tmpE.terminus);
            }
            ++index;
        }
        graph.allVertexs.get(0).shortestPathLength=0.0;
    }

    public void searchAimResource(){
        Vertex aimV=graph.searchAimVertex();
        Vertex tmpV=graph.allVertexs.get(0);
        //确定目标生产资源
        aimResource=aimV.resource;
        //如果两者距离等于0，则不需要经过物流时间，直接投入生产即可
        if(tmpV.roadCost(aimV)==0.0){
            onRoadTime=0.0;
            aimResource.putOnProduction(this);
        }
        else{
            onRoadTime=tmpV.roadCost(aimV);
        }
    }


    public boolean isFinished(){
        if(taskType.sequence.size()==tmpSequence){
            return true;
        }
        else{
            return false;
        }
    }

}
