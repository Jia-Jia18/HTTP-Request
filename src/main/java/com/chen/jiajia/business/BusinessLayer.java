package com.chen.jiajia.business;

import java.util.*;
import companydata.*;

//validation 
public class BusinessLayer {
    DataLayer dl = new DataLayer("jc1026");
    String company = "jc1026";

   public boolean checkIDExist (int dept_id) {
    
        //check if dept_id exists
        List<Department> departments = dl.getAllDepartment(company);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(Department d : departments ){    	
            list.add(d.getId());
        }
        if(!list.contains(dept_id))
            return false;
        return true;
    }

    public boolean checkEmpExist (int emp_id) {
    
        //check if emp_id exists
        List<Employee> employee = dl.getAllEmployee(company);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(Employee e : employee ){    	
            list.add(e.getId());
        }
        if(!list.contains(emp_id))
            return false;
        return true;
    }

    public boolean checkCompany(String companyName ){
        if(companyName.equals("jc1026")){
            return true;
        } 
        return false;
    }

    public boolean checkDeptNoUnique(String dept_no) {
        List<Department> departments = dl.getAllDepartment(company);
        ArrayList<String> list = new ArrayList<String>();
        for(Department d : departments ){  
            list.add(d.getDeptNo());
            if(list.contains(dept_no)){
                return false;
            }
        }
        return true; 
      }


    public boolean checkMngIdExist(int mng_id) {
        List<Employee> employee = dl.getAllEmployee(company);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(Employee e : employee ){    	
            list.add(e.getMngId());
        }
        if(!list.contains(mng_id)){
            return false;
        } 
        return true;
      }

      public boolean checkDate(Date date) {
        Date today = new Date();
        if(today.compareTo(date) > 0 || today.compareTo(date) == 0) {
            return true;
        }
        return false; 
      }

      public boolean checkWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)  {
            return false;
        }
        return true;   
    }


    public boolean checkEmpNoUnique(String emp_no) {
        List<Employee> employees = dl.getAllEmployee(company);
        ArrayList<String> list = new ArrayList<String>();
        for(Employee e : employees ){  
            list.add(e.getEmpNo());
        }
        if(list.contains(emp_no)){
            return false;
        }
        return true;
      }


      public boolean checkTimecardId(int timecard_id){
        Timecard timecards = dl.getTimecard(timecard_id); 
        if(timecards == null){
            return false;
        }
        return true;
    }


    public boolean checkEndTime(Date st, Date et){
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal.setTime(st);
        cal1.setTime(et);
        cal.add(Calendar.HOUR, 1);
        Date end = cal.getTime();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String date = String.valueOf(cal.get(Calendar.DAY_OF_YEAR));
        String year1 = String.valueOf(cal1.get(Calendar.YEAR));
        String date1 = String.valueOf(cal1.get(Calendar.DAY_OF_YEAR));
        if(et.after(end) && year.equals(year1) && date.equals(date1)){
            return true;
        }
        return false;
    }


    public boolean TimeRange(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if(cal.get(Calendar.HOUR_OF_DAY) >= 8 && cal.get(Calendar.HOUR_OF_DAY) < 18 ){
            return true;
        }
        else if(cal.get(Calendar.HOUR_OF_DAY) == 18 && cal.get(Calendar.MINUTE) == 0){
            return true;
        }
        return false;
    }

    public boolean checkTimeFrame(int emp_id, Date st){
        Calendar cal = Calendar.getInstance();
        cal.setTime(st);

        String year = String.valueOf(cal.get(Calendar.YEAR));
        String date = String.valueOf(cal.get(Calendar.DAY_OF_YEAR));

        List<Timecard> timecards = dl.getAllTimecard(emp_id);
        for(Timecard t : timecards ){  
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(t.getStartTime());
            String date1 = String.valueOf(cal1.get(Calendar.DAY_OF_YEAR));
            String year1 = String.valueOf(cal1.get(Calendar.YEAR));
            
            if(year.equals(year1) && date.equals(date1)){
                return false;
            } 
        }
        return true;
    }




}
