package com.algorithm;

import java.util.ArrayList;

//Directed graph vertex
public class Vertex {
    public String name;
    public ArrayList<Edge> allEdges;
    public Double shortestPathLength;
    public Vertex preVertex;
    public ManuResource resource;


    public Vertex(ManuResource resource){
        this.resource=resource;
        this.name=resource.name;
        this.allEdges=new ArrayList<Edge>();
        this.shortestPathLength=Double.MAX_VALUE;
        this.preVertex=null;

    }


    public Vertex(String name){
        this.name=name;
        this.allEdges=new ArrayList<Edge>();
        this.shortestPathLength=Double.MAX_VALUE;
        this.preVertex=null;
        this.resource=null;
    }

    public void addEdge(Edge newEdge){
        this.allEdges.add(newEdge);
    }


    //在该节点接受服务需要消耗的总时间
    public Double expectWaitingTime(ServiceType tmpServiceType){
        if(resource!=null){
            return resource.expectWaitingTime(tmpServiceType);
        }
        else
            return 0.0;
    }

    //该节点与otherVertex的物流时间
    public Double roadCost(Vertex otherVertex){
        if(this.name=="start"||otherVertex.name=="end"){
            return 0.0;
        }
        else {
            return resource.roadCost(otherVertex.resource);
        }
    }


}
