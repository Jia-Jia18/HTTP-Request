package com.chen.jiajia;
import java.text.*;
import java.util.*;

public class test {
    public static void main(String[]args) {
        MyResource ac = new MyResource();
        String company="jc1026";
        // System.out.println("********************get department by ID**************************");
        //System.out.println(ac.getDepartment("jc1026",3).getEntity());
        // System.out.println(ac.getDepartment("jc1026",0).getEntity());

        // System.out.println("**************************get all departments*********************");
        //System.out.println(ac.getAllDepartment("jc1026").getEntity());
        // System.out.println(ac.getAllDepartment("jc106").getEntity());

        // System.out.println("**************************update department*********************");
        //  String json = "{\"company\":\""+company+"\",\"dept_id\":1,\"dept_name\":\"IT\","+
        //              "\"dept_no\":\""+company+"-saasweas\",\"location\":\"rochester\"}";  

        // String json1 = "{\"company\":\""+company+"\",\"dept_id\":0,\"dept_name\":\"IT\","+
        //              "\"dept_no\":\""+company+"-xxx\",\"location\":\"rochester\"}"; 

        // String json2 = "{\"company\":\""+company+"\",\"dept_id\":1,\"dept_name\":\"IT\","+
          //           "\"dept_no\":\"xxxxd10\",\"location\":\"rochester\"}"; 

        // System.out.println(ac.getDepartment("jc1026",1).getEntity());
        // System.out.println(ac.updateDepartment(json).getEntity());
        // System.out.println(ac.getDepartment("jc1026",1).getEntity());
        //System.out.println(ac.updateDepartment(json1).getEntity());
        //System.out.println(ac.updateDepartment(json2).getEntity());

        // System.out.println("**************************insert department*********************");
        // System.out.println(ac.insertDepartment("jc1026","test","ttttt","ll").getEntity());
        // System.out.println(ac.getDepartment("jc1026",9).getEntity());

        // System.out.println("**************************delete department*********************");
        //  System.out.println(ac.getDepartment("jc1026",7).getEntity());
        // System.out.println(ac.deleteDepartment("jc1026",7).getEntity());
        // System.out.println(ac.getDepartment("jc1026",7).getEntity());


        // System.out.println("********************get employee by ID**************************");
        //  System.out.println(ac.getEmployee("jc1026",2).getEntity());
        //  System.out.println(ac.getEmployee("jc1026",0).getEntity());

        // System.out.println("********************get all employee*************************");
        //System.out.println(ac.getAllEmployee("jc1026").getEntity());
        // System.out.println(ac.getAllEmployee("jc10261").getEntity());


        //System.out.println("********************insert employee*************************");
        //System.out.println(ac.insertEmployee("jc1026","french","eeee","2021-11-16","prog",5000.0,1,2).getEntity());
        //System.out.println(ac.getEmployee("jc1026",15).getEntity());

        // String dateStr = "2021-11-16";
        // int dept_id = 1;
        // System.out.println("**************************update employee*********************");
        // String json = "{\"company\":\""+company+"\",\"emp_id\":15,\"emp_name\":\"test\","+
        // "\"emp_no\":\""+company+"-bdfvksNew\",\"hire_date\":"+
        // "\""+dateStr+"\",\"job\":\"new job\",\"salary\":60000.0,"+
        // "\"dept_id\":"+dept_id+",\"mng_id\":2}";  


         //System.out.println(ac.getEmployee("jc1026",15).getEntity());
         //System.out.println(ac.updateEmployee(json).getEntity());
         //System.out.println(ac.getEmployee("jc1026",15).getEntity());



         // System.out.println("**************************delete employee*********************");
        //  System.out.println(ac.getEmployee("jc1026",14).getEntity());
        // System.out.println(ac.deleteEmployee("jc1026",14).getEntity());
        // System.out.println(ac.getEmployee("jc1026",14).getEntity());



        // System.out.println("**************************get timecard by id*********************");
        //System.out.println(ac.getTimecard("jc1026",1).getEntity());
        //System.out.println(ac.getAllTimecard("jc1026",1).getEntity());


        System.out.println(ac.insertTimecard("jc1026",1,"2021-10-21 11:20:00","2021-11-21 14:20:00").getEntity());

        // String dateStr = "2021-11-15 11:20:00";
        // String dateStr2 = "2021-11-15 13:20:00";
        // String json = "{\"company\":\""+company+"\",\"timecard_id\":2,\"start_time\":\""+dateStr+
        //   "\",\"end_time\":\""+dateStr2+"\",\"emp_id\":2 }";
        //   System.out.println(ac.getTimecard("jc1026",1).getEntity());
        //   System.out.println();
        //System.out.println(ac.updateTimecard(json).getEntity());
        //   System.out.println();
        //   System.out.println(ac.getTimecard("jc1026",1).getEntity());

        // System.out.println(ac.getTimecard("jc1026",1).getEntity());
        //System.out.println(ac.deleteTimecard("jc1026",7).getEntity());
        // System.out.println(ac.getTimecard("jc1026",1).getEntity());
         
        

        
    }
}
