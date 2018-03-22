package com.algorithm;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Vertex> allVertexs;

    public Graph(){
        this.allVertexs=new ArrayList<Vertex>();
    }

    public void addVertex(Vertex newVertex){
        this.allVertexs.add(newVertex);
    }

    private void relax(Vertex origin, Edge tmpEdge){
        if(origin.shortestPathLength+tmpEdge.value<tmpEdge.terminus.shortestPathLength){
            tmpEdge.terminus.preVertex=origin;
            tmpEdge.terminus.shortestPathLength=origin.shortestPathLength+tmpEdge.value;
        }
    }

    //更新图中所有路径的权值
    public void refresh(){
//        for(Vertex tmpV : allVertexs){
//            for(Edge tmpE : tmpV.allEdges){
//                //物流消耗时间+预期等待时间
//                tmpE.value=tmpV.roadCost(tmpE.terminus)+
//                        tmpE.terminus.expectWaitingTime(tmpServiceType);
//            }
//            tmpV.shortestPathLength=Double.MAX_VALUE;
//            tmpV.preVertex=null;
//        }
//        allVertexs.get(0).shortestPathLength=0.0;


    }

    public Vertex searchAimVertex(){
        for(Vertex tmpV : allVertexs){
            for(Edge tmpE : tmpV.allEdges){
                relax(tmpV,tmpE);
            }
        }
        Vertex tmpV=allVertexs.get(allVertexs.size()-1);
        while(tmpV.preVertex!=allVertexs.get(0)){
            tmpV=tmpV.preVertex;
        }
        return tmpV;
    }


}
