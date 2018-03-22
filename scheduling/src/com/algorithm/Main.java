package com.algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String args[]) {
        Global.readServiceTypes();
        Global.readManuResources();

        Global.readTaskTypes();
        Global.readManuTasks();//ManuTasks要最后读取 //以上四个文件要按照顺序读取
//        测试数据是否正确
//        ManuTask tmpManuTask=Global.allManuTasks.get(26);
//        System.out.println(tmpManuTask.name);
//        for(int i : tmpManuTask.taskType.sequence){
//            System.out.print(i+"    ");
//        }
//        System.out.println();
//        Graph tmpG=tmpManuTask.graph;
//        for(Vertex tmpV : tmpG.allVertexs){
//            System.out.print(tmpV.name+":");
//            for(Edge tmpE : tmpV.allEdges){
//                System.out.print("  "+tmpE.terminus.name);
//            }
//            System.out.println();
//        }
        Global.scheduling();
//        for(ManuTask tmpM : Global.allManuTasks){
//            System.out.print(tmpM.aimResource.name+" "+tmpM.remainTime);
//        }
//        for(ManuTask tmpM : Global.allManuTasks){
//            for(Vertex tmpV : tmpM.graph.allVertexs){
//                if(tmpV.resource!=null) {
//                    System.out.print(tmpV.resource.name + ", ");
//                }
//            }
//            System.out.println();
//        }
////        测试结果
//        ManuTask1 11.517113738811144,ManuTask4 9.534790806956025,ManuTask7 9.938328559996194,ManuTask28 10.1535293663888,
//                ManuTask10 15.75886211673275,ManuTask13 15.178925412452323,ManuTask16 6.488450498364256,ManuTask17 3.4732879623856494,
//                ManuTask19 10.377612348718898,ManuTask21 11.657360547493756,ManuTask23 11.278879886335137,
//                ManuTask2 9.54916701471507,ManuTask5 9.391398790853284,ManuTask8 5.911915727697647,ManuTask18 8.541850641811942,
//                ManuTask11 18.61183305724989,ManuTask14 12.143664594692059,ManuTask24 16.58652332180294,ManuTask26 11.79586631002449,
//                ManuTask12 14.475381034824046,ManuTask15 14.35914267398304,ManuTask25 14.29366463194644,ManuTask27 14.646435615165005,
//                ManuTask3 6.394230553528592,ManuTask6 16.462761610237987,ManuTask9 9.01754112948329,ManuTask29 17.65921909885709,
//                ManuTask20 13.83703488973877,ManuTask22 13.601699421353807,ManuTask30 15.646227385218413,
//                res1, res1, res2, res5, res3, res7, res8,
//                res4, res1, res2, res5, res3, res7, res8,
//                res7, res1, res2, res5, res3, res7, res8,
//                res1, res1, res2, res5, res3, res7, res8,
//                res4, res3, res7, res8, res1, res4,
//                res7, res3, res7, res8, res1, res4,
//                res1, res3, res7, res8, res1, res4,
//                res4, res3, res7, res8, res1, res4,
//                res7, res3, res7, res8, res1, res4,
//                res2, res3, res5, res6, res1, res6, res8,
//                res5, res3, res5, res6, res1, res6, res8,
//                res6, res3, res5, res6, res1, res6, res8,
//                res2, res3, res5, res6, res1, res6, res8,
//                res5, res3, res5, res6, res1, res6, res8,
//                res6, res3, res5, res6, res1, res6, res8,
//                res2, res1, res4, res7, res2, res5, res6, res3, res7, res8,
//                res2, res1, res4, res7, res2, res5, res6, res3, res7, res8,
//                res4, res1, res4, res7, res2, res5, res6, res3, res7, res8,
//                res3, res2, res5, res6, res2, res4, res7, res8, res3, res5, res6,
//                res8, res2, res5, res6, res2, res4, res7, res8, res3, res5, res6,
//                res3, res2, res5, res6, res2, res4, res7, res8, res3, res5, res6,
//                res8, res2, res5, res6, res2, res4, res7, res8, res3, res5, res6,
//                res3, res2, res5, res6, res2, res4, res7, res8, res3, res5, res6,
//                res5, res1, res2, res5, res2, res4, res7, res8, res1, res6, res8, res1, res4,
//                res6, res1, res2, res5, res2, res4, res7, res8, res1, res6, res8, res1, res4,
//                res5, res1, res2, res5, res2, res4, res7, res8, res1, res6, res8, res1, res4,
//                res6, res1, res2, res5, res2, res4, res7, res8, res1, res6, res8, res1, res4,
//                res1, res3, res7, res8, res1, res4,
//                res7, res1, res2, res5, res3, res7, res8,
//                res8, res2, res5, res6, res2, res4, res7, res8, res3, res5, res6,
        //Global.scheduling();

        test();

    }

    public static void test(){
        for(ManuResource tmpM : Global.allManuResources){
            System.out.println(tmpM.name+":");
            for(WorkingTime workingTime : tmpM.workingTimes){
                System.out.print(workingTime.manuTask.name+" "+workingTime.leftTime+"-"+workingTime.rightTime+"    ");
            }
            System.out.println();
        }
    }



}
