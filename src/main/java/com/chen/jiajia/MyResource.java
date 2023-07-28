package com.chen.jiajia;

import java.util.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.*;

import com.chen.jiajia.business.BusinessLayer;

import companydata.*;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


/**
 * Root resource (exposed at "CompanyServices" path)
 */
@Path("CompanyServices")
public class MyResource {
    @Context
    UriInfo uriInfo;
    DataLayer dl = null;
    BusinessLayer bl = new BusinessLayer();


    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @Path("/company")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCompany(
        @QueryParam("company") String company
    ){
        try{
            dl = new DataLayer(company);
            int row = dl.deleteCompany(company);
            if(row <= 0 ){
                return Response.ok("{\"error\":\""+  "delete company failed!" + "\"}").build();
            }
            return Response.ok("{\"success\":\""+  company + "'s information deleted\"}").build();
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }

    }//end delete


    /** *******************Department get, update, insert, delete ********** */ 
    //get department by id
    @Path("/department")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartment(
        @QueryParam("company") String company,
        @QueryParam("dept_id") int dept_id 

    ) {
        //use our Bussiness layer
        dl = new DataLayer(company);
        try{
            if(bl.checkIDExist(dept_id) == false){
                return Response.ok("{\"error\":\""+  "Dept_id does not exists!" + "\"}").build();
            }
            else{
                Department d = dl.getDepartment(company,dept_id);
                String list = "{\"dept_id\":"+d.getId()+",\"company\":\"" + d.getCompany()+"\",\"dept_name\":\"" + d.getDeptName() +"\","+
                "\"dept_no\":\""+d.getDeptNo()+"\",\"location\":\"" + d.getLocation() +"\"}"; 
                return Response.ok(list).build();
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }


    // get all department
    @Path("/departments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDepartment(
        @QueryParam("company") String company

    ) {
        //use our Bussiness layer
        dl = new DataLayer(company);
        try{
           
            List<Department> departments = dl.getAllDepartment(company);
            String list = "";
            if(bl.checkCompany(company) == false){
                return Response.ok("{\"error\":\""+  "company does not exists!" + "\"}").build();
            } else{
                for(Department d : departments ){    
                    list += "{\"dept_id\":"+d.getId()+",\"company\":\"" + d.getCompany()+"\",\"dept_name\":\"" + d.getDeptName() +"\","+
                "\"dept_no\":\""+d.getDeptNo()+"\",\"location\":\"" + d.getLocation() +"\"}"; 
                }
            }
            return Response.ok("["+list+"]").build();
            
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "}").build();
        }
        finally {
            dl.close();
        }
    }// end of getAllDepartment




    //update department
    @Path("/department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(
        String dept
    ){
        //body comes in as a JSON string
        //parse and do some validation before
        //updating the DB
        JsonReader rdr = Json.createReader(new StringReader(dept));
        String list = "";

        try (rdr) {
            JsonObject obj = rdr.readObject();
            String company = obj.get("company").toString();
            int dept_id = Integer.valueOf(obj.get("dept_id").toString());
            String dept_name = obj.get("dept_name").toString();
            String dept_no = obj.get("dept_no").toString();
            String location = obj.get("location").toString();
            company = company.replace("\"", "");
            dept_no = dept_no.replace("\"", "");
            dept_name = dept_name.replace("\"", "");
            location = location.replace("\"", "");
            dl = new DataLayer(company);

            //Additional Validation
            //i. dept_no must be unique among all companies, Suggestion: include company name as part of id.
            if(bl.checkDeptNoUnique(dept_no) == false){
                return Response.ok("{\"error\":\""+  "Dept_no is not unique among all companies" + "\"}").build();
            }
            //ii. dept_id must be an existing record number for a department
            else if(bl.checkIDExist(dept_id) == false){
                return Response.ok("{\"error\":\""+  "Dept_id does not exists!" + "\"}").build();
            }
            else {
                Department d = dl.getDepartment(company,dept_id);
                d.setDeptName(dept_name);
                d.setDeptNo(dept_no);
                d.setLocation(location);
                Department update = dl.updateDepartment(d);
                list = "{\"dept_id\":"+update.getId()+",\"company\":\"" + update.getCompany()+"\",\"dept_name\":\"" + update.getDeptName() +"\","+
                "\"dept_no\":\""+update.getDeptNo()+"\",\"location\":\"" + update.getLocation() +"\"}"; 
            }

            return Response.ok("{\"Success\":"+list+"}").build();
         }//try
         catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }

    //insert department
    @Path("/department")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertDepartment(
        @FormParam("company") String company,
        @FormParam("dept_name") String dept_name,
        @FormParam("dept_no") String dept_no,
        @FormParam("location") String location
    ){
        dl = new DataLayer(company);
        try{
            //Additional Validation
            //i. dept_no must be unique among all companies, Suggestion: include company name as part of id.
            if(bl.checkDeptNoUnique(dept_no) == false){
                return Response.ok("{\"error\":\""+  "Dept_no is not unique among all companies" + "\"}").build();
            }
            else{
                Department d = new Department(company,dept_name,dept_no,location);
                d = dl.insertDepartment(d);

                String list = "{\"dept_id\":"+d.getId()+",\"company\":\"" + d.getCompany()+"\",\"dept_name\":\"" + d.getDeptName() +"\","+
                "\"dept_no\":\""+d.getDeptNo()+"\",\"location\":\"" + d.getLocation() +"\"}"; 
                return Response.status(200).entity("{\"Success\":"+list+"}").build(); 
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
         }
    }


    @Path("/department")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(
        @QueryParam("company") String company,
        @QueryParam("dept_id") int dept_id 
    ){
        //use our Bussiness layer
        dl = new DataLayer(company);
        try{
            if(bl.checkIDExist(dept_id) == false){
                return Response.ok("{\"error\":\""+  "Dept_id does not exists!" + "\"}").build();
            }
            else{
                int delete = dl.deleteDepartment(company,dept_id);
                if(delete <= 0 ){
                    return Response.ok("{\"error\":\""+  "Department not deleted" + "\"}").build();
                }
                return Response.ok("{\"success\":\""+ "Department "+ dept_id + " from " + company+" is deleted." +"\"}").build();

            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }





        /** *******************Employee get, update, insert, delete ********** */ 
    //get employee by id
    @Path("/employee")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(
        @QueryParam("company") String company,
        @QueryParam("emp_id") int emp_id 

    ) {
        dl = new DataLayer(company);
        try{
            if(bl.checkEmpExist(emp_id) == false){
                return Response.ok("{\"error\":\""+  "Emp_id does not exists!" + "\"}").build();
            }
            else{
                Employee employee = dl.getEmployee(emp_id); 

                String list = "{\"emp_id\":"+employee.getId()+",\"emp_name\":\"" + employee.getEmpName()+"\",\"emp_no\":\"" + employee.getEmpNo() +"\","+
                "\"hire_date\":\"" + employee.getHireDate()+ "\",\"job\":\"" + employee.getJob()+"\",\"salary\":"+ employee.getSalary()+",\"dept_id\":" +employee.getDeptId()+",\"mng_id\":"+ employee.getMngId() +"}"; 
 
                return Response.ok(list).build();
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }




        // get all employee
    @Path("/employees")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmployee(
        @QueryParam("company") String company

    ) {
        dl = new DataLayer(company);
        try{
           
            List<Employee> employees = dl.getAllEmployee(company);
            String list = "";

            if(bl.checkCompany(company) == false){
                return Response.ok("{\"error\":\""+ "company does not exists!" + "\"}").build();
            } else{
                for(Employee employee : employees ){    
                    list += "{\"emp_id\":"+employee.getId()+",\"emp_name\":\"" + employee.getEmpName()+"\",\"emp_no\":\"" + employee.getEmpNo() +"\","+
                "\"hire_date\":\"" + employee.getHireDate()+ "\",\"job\":\"" + employee.getJob()+"\",\"salary\":"+ employee.getSalary()+",\"dept_id\":" +employee.getDeptId()+",\"mng_id\":"+ employee.getMngId() +"}"; 
                }
            }
            return Response.ok("["+list+"]").build();
            
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }

    }


    //insert employee
    @Path("/employee")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEmployee(
        @FormParam("company") String company,
        @FormParam("emp_name") String emp_name,
        @FormParam("emp_no") String emp_no,
        @FormParam("hire_date") String hire_date,
        @FormParam("job") String job,
        @FormParam("salary") double salary,
        @FormParam("dept_id") int dept_id,
        @FormParam("mng_id") int mng_id  
    ){
        dl = new DataLayer(company);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try{
            date = df.parse(hire_date);
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }


        try{
            //Additional Validation
            //i.	company – must be your RIT username
            if(bl.checkCompany(company) == false){
                return Response.ok("{\"error\":\""+  "company does not exists!" + "\"}").build();
            }
            else if(bl.checkIDExist(dept_id) == false){
                 //ii.	dept_id must exist as a Department in your company
                 return Response.ok("{\"error\":\""+  "Dept_id does not exists!" + "\"}").build();
            }
            //iii.	mng_id must be the record id of an existing Employee in your company. Use 0 if the first employee or any other employee that doesn’t have a manager.
            else if(bl.checkMngIdExist(mng_id) == false){
                mng_id = 0;
                return Response.ok("{\"error\":\""+ "mng_id must be the record id of an existing Employee in the company!" + "\"}").build();
            }
            //iv.	hire_date must be a valid date equal to the current date or earlier (e.g. current date or in the past)
            else if(bl.checkDate(date) == false){
                return Response.ok("{\"error\":\""+  "hire_date must be a valid date equal to the current date or earlier (e.g.current date or in the past)!" + "\"}").build();
            }
            //v.	hire_date must be a Monday, Tuesday, Wednesday, Thursday or a Friday. It cannot be Saturday or Sunday.
            else if (bl.checkWeekDay(date) == false){
                return Response.ok("{\"error\":\""+  "hire_date must be a Monday, Tuesday, Wednesday, Thursday or a Friday. It cannot be Saturday or Sunday!" + "\"}").build();
            }
            //vi.	emp_no must be unique amongst all employees in the database, including those of other companies.
            else if(bl.checkEmpNoUnique(emp_no) == false){
                return Response.ok("{\"error\":\""+  "emp_no must be unique amongst all employees in the database!" + "\"}").build();
            }
            else{
                Employee emp = new Employee(emp_name,emp_no, new java.sql.Date(date.getTime()),job,salary,dept_id,mng_id);
                emp = dl.insertEmployee(emp);

                String list = "{\"emp_id\":"+emp.getId()+",\"emp_name\":\"" + emp.getEmpName()+"\",\"emp_no\":\"" + emp.getEmpNo() +"\","+
                "\"hire_date\":\"" + emp.getHireDate()+ "\",\"job\":\"" + emp.getJob()+"\",\"salary\":"+ emp.getSalary()+",\"dept_id\":" +emp.getDeptId()+",\"mng_id\":"+ emp.getMngId() +"}"; 

                return Response.status(200).entity("{\"Success\":"+list+"}").build(); 
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
         }
    }



    @Path("/employee")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(
        String employee
    ){
        JsonReader rdr = Json.createReader(new StringReader(employee));

        try (rdr) {
            JsonObject obj = rdr.readObject();
            String company = obj.get("company").toString();
            int emp_id = Integer.valueOf(obj.get("emp_id").toString());
            String emp_name = obj.get("emp_name").toString();
            String emp_no = obj.get("emp_no").toString();
            String hire_date = obj.get("hire_date").toString();
            String job = obj.get("job").toString();
            double salary = Double.valueOf(obj.get("salary").toString());
            int dept_id = Integer.valueOf(obj.get("dept_id").toString());
            int mng_id = Integer.valueOf(obj.get("mng_id").toString());
            
        
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            hire_date = hire_date.replace("\"", "");
            company = company.replace("\"", "");
            emp_name = emp_name.replace("\"", "");
            emp_no = emp_no.replace("\"", "");
            job = job.replace("\"", "");
            date = format.parse(hire_date);
            dl = new DataLayer(company);
        
            //Additional Validation
            //i.	company – must be your RIT username
            if(bl.checkCompany(company) == false){
                return Response.ok("{\"error\":\""+  "company does not exists!" + "\"}").build();
            }
            else if(bl.checkIDExist(dept_id) == false){
                 //ii.	dept_id must exist as a Department in your company
                 return Response.ok("{\"error\":\""+  "Dept_id does not exists!" + "\"}").build();
            }
            //iii.	mng_id must be the record id of an existing Employee in your company. Use 0 if the first employee or any other employee that doesn’t have a manager.
            else if(bl.checkMngIdExist(mng_id) == false){
                mng_id = 0;
                return Response.ok("{\"error\":\""+ "mng_id must be the record id of an existing Employee in the company!" + "\"}").build();
            }
            //iv.	hire_date must be a valid date equal to the current date or earlier (e.g. current date or in the past)
            else if(bl.checkDate(date) == false){
                return Response.ok("{\"error\":\""+  "hire_date must be a valid date equal to the current date or earlier (e.g.current date or in the past)!" + "\"}").build();
            }
            //v.	hire_date must be a Monday, Tuesday, Wednesday, Thursday or a Friday. It cannot be Saturday or Sunday.
            else if (bl.checkWeekDay(date) == false){
                return Response.ok("{\"error\":\""+  "hire_date must be a Monday, Tuesday, Wednesday, Thursday or a Friday. It cannot be Saturday or Sunday!" + "\"}").build();
            }
            //vi.	emp_no must be unique amongst all employees in the database, including those of other companies.
            else if(bl.checkEmpNoUnique(emp_no) == false){
                return Response.ok("{\"error\":\""+  "emp_no must be unique amongst all employees in the database!" + "\"}").build();
            }
            //emp_id must be a valid record id in the database.
            else if(bl.checkEmpExist(emp_id) == false){
                return Response.ok("{\"error\":\""+  "emp_id must be a valid record id in the database!" + "\"}").build();
            }
            else{
                Employee e = dl.getEmployee(emp_id);
                e.setEmpName(emp_name);
                e.setEmpNo(emp_no);
                e.setHireDate( new java.sql.Date(date.getTime()));
                e.setJob(job);
                e.setSalary(salary);
                e.setDeptId(dept_id);
                e.setMngId(mng_id);

                Employee emp = dl.updateEmployee(e);
                String list = "{\"emp_id\":"+emp.getId()+",\"emp_name\":\"" + emp.getEmpName()+"\",\"emp_no\":\"" + emp.getEmpNo() +"\","+
                "\"hire_date\":\"" + emp.getHireDate()+ "\",\"job\":\"" + emp.getJob()+"\",\"salary\":"+ emp.getSalary()+",\"dept_id\":" +emp.getDeptId()+",\"mng_id\":"+ emp.getMngId() +"}"; 

                return Response.ok("{\"Success\":"+list+"}").build(); 
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }

    @Path("/employee")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(
        @QueryParam("company") String company,
        @QueryParam("emp_id") int emp_id 
    ){
        //use our Bussiness layer
        dl = new DataLayer(company);
        try{
            if(bl.checkEmpExist(emp_id) == false){
                return Response.ok("{\"error\":\""+  "Emp_id does not exists!" + "\"}").build();
            }
            else{
                int delete = dl.deleteEmployee(emp_id);
                if(delete <= 0 ){
                    return Response.ok("{\"error\":\""+  "Employee not deleted" + "\"}").build();
                }
                return Response.ok("{\"success\":\""+ "Employee "+ emp_id + " deleted." +"\"}").build();

            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }





    /** *******************timecard get, update, insert, delete ********** */ 
    @Path("/timecard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimecard( 
        @QueryParam("company") String company,
        @QueryParam("timecard_id") int timecard_id 
    ) {
        try{
            dl = new DataLayer(company);
            //check if timecard id exist
            if(bl.checkTimecardId(timecard_id) == false){
                return Response.ok("{\"error\":\""+  "timecard_id does not exists!" + "\"}").build();
            }
            else{
                Timecard tc = dl.getTimecard(timecard_id); 

                String list = "{\"timecard_id\":"+tc.getId()+",\"start_time\":\"" + tc.getStartTime()+"\",\"end_time\":\"" + tc.getEndTime() +"\","+
                "\"emp_id\":" + tc.getEmpId()+ "}"; 

                return Response.ok("{\"timecard\":"+list+"}").build(); 
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }



    @Path("/timecards")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTimecard( 
        @QueryParam("company") String company,
        @QueryParam("emp_id") int emp_id 
    ) {
        try{
            dl = new DataLayer(company);
            String list = "";
            if(bl.checkEmpExist(emp_id) == false){
                return Response.ok("{\"error\":\""+  "emp_id does not exists!" + "\"}").build();
            }
            else{
                List<Timecard> timecards = dl.getAllTimecard(emp_id);   
                for(Timecard tc : timecards  ){ 
                    list += "{\"timecard_id\":"+tc.getId()+",\"start_time\":\"" + tc.getStartTime()+"\",\"end_time\":\"" + tc.getEndTime() +"\","+
                    "\"emp_id\":" + tc.getEmpId()+ "}"; 
                }

                return Response.ok("["+list+"]").build(); 
            }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
        }
    }


    @Path("/timecard")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertTimecard(
        @FormParam("company") String company,
        @FormParam("emp_id") int emp_id,
        @FormParam("start_time") String start_time,
        @FormParam("end_time") String end_time     
    ){
        dl = new DataLayer(company);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date st = null;
        Date et = null;
        try{
            st = format.parse(start_time);
            et = format.parse(end_time);
        
        //i.	company must be your RIT id
        if(bl.checkCompany(company) == false){
            return Response.ok("{\"error\":\""+  "company must be your RIT id" + "\"}").build();
        }
        //emp_id must exist as the record id of an Employee in the company.
        else if(bl.checkEmpExist(emp_id) == false){
            return Response.ok("{\"error\":\""+  "emp_id does not exists!" + "\"}").build();
        }
        //start time equal to the current date or back to the Monday prior to the current date if the current date is not a Monday
        else if(bl.checkDate(st) == false){
            return Response.ok("{\"error\":\""+  "Start time must equal to the current date or back to the Monday prior to the current date if the current date is not a Monday!" + "\"}").build();
        }
        //end_time must be at least 1 hour greater than the start_time and be on the same day as the start_time.
        else if(bl.checkEndTime(st,et) == false){
            return Response.ok("{\"error\":\""+  "end_time must be at least 1 hour greater than the start_time and be on the same day as the start_time!" + "\"}").build();
        }
        //start_time and end_time must be a Monday, Tuesday, Wednesday,Thursday or a Friday. They cannot be Saturday or Sunday.
        else if(bl.checkWeekDay(st) == false || bl.checkWeekDay(et) == false ){
            return Response.ok("{\"error\":\""+  "start_time and end_time must be a Monday, Tuesday, Wednesday, Thursday or a Friday. They cannot be Saturday or Sunday!" + "\"}").build();
        }
        //start_time and end_time must be between the hours (in 24 hour format) of 08:00:00 and 18:00:00 inclusive.
        else if(bl.TimeRange(st) == false || bl.TimeRange(et) == false ){
            return Response.ok("{\"error\":\""+  "Time must be between the hours (in 24 hour format) of 08:00:00 and 18:00:00 inclusive!" + "\"}").build();
        }
        //start_time must not be on the same day as any other start_time for that employee.
        else if(bl.checkTimeFrame(emp_id,st) == false){
            return Response.ok("{\"error\":\""+ " start time must not be on the same day as any other start_time for that employee!" + "\"}").build();
        }
        else{
            Timecard timecard = new Timecard(new Timestamp(st.getTime()),new Timestamp(et.getTime()),emp_id);
            timecard = dl.insertTimecard(timecard);

            String list = "{\"timecard_id\":"+timecard.getId()+",\"start_time\":\"" + timecard.getStartTime()+"\",\"end_time\":\"" + timecard.getEndTime() +"\","+
            "\"emp_id\":" + timecard.getEmpId()+ "}"; 

           
            return Response.status(200).entity("{\"success\": "+  list + "}").build(); 
        }
    }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
         }

    }



    @Path("/timecard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTimecard(
       String time
    ){
        JsonReader rdr = Json.createReader(new StringReader(time));
        try(rdr) {
                JsonObject obj = rdr.readObject();
                String company = obj.get("company").toString();
                int timecard_id = Integer.valueOf(obj.get("timecard_id").toString());
                String start_time = obj.get("start_time").toString();
                String end_time = obj.get("end_time").toString();
                int emp_id = Integer.valueOf(obj.get("emp_id").toString());
            
                company = company.replace("\"", "");
                start_time = start_time.replace("\"", "");
                end_time = end_time.replace("\"", "");
                dl = new DataLayer(company);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date st = format.parse(start_time);
                Date et = format.parse(end_time);
            
                    //i.	company must be your RIT id
                if(bl.checkCompany(company) == false){
                    return Response.ok("{\"error\":\""+  "company must be your RIT id" + "\"}").build();
                }
                //emp_id must exist as the record id of an Employee in the company.
                else if(bl.checkEmpExist(emp_id) == false){
                    return Response.ok("{\"error\":\""+  "emp_id does not exists!" + "\"}").build();
                }
                //start time equal to the current date or back to the Monday prior to the current date if the current date is not a Monday
                else if(bl.checkDate(st) == false){
                    return Response.ok("{\"error\":\""+  "Start time must equal to the current date or back to the Monday prior to the current date if the current date is not a Monday!" + "\"}").build();
                }
                //end_time must be at least 1 hour greater than the start_time and be on the same day as the start_time.
                else if(bl.checkEndTime(st,et) == false){
                    return Response.ok("{\"error\":\""+  "end_time must be at least 1 hour greater than the start_time and be on the same day as the start_time!" + "\"}").build();
                }
                //start_time and end_time must be a Monday, Tuesday, Wednesday,Thursday or a Friday. They cannot be Saturday or Sunday.
                else if(bl.checkWeekDay(st) == false || bl.checkWeekDay(et) == false ){
                    return Response.ok("{\"error\":\""+  "start_time and end_time must be a Monday, Tuesday, Wednesday, Thursday or a Friday. They cannot be Saturday or Sunday!" + "\"}").build();
                }
                //start_time and end_time must be between the hours (in 24 hour format) of 08:00:00 and 18:00:00 inclusive.
                else if(bl.TimeRange(st) == false || bl.TimeRange(et) == false ){
                    return Response.ok("{\"error\":\""+  "Time must be between the hours (in 24 hour format) of 08:00:00 and 18:00:00 inclusive!" + "\"}").build();
                }
                //start_time must not be on the same day as any other start_time for that employee.
                else if(bl.checkTimeFrame(emp_id,st) == false){
                    return Response.ok("{\"error\":\""+ " start time must not be on the same day as any other start_time for that employee!" + "\"}").build();
                }
                //check if timecard id exist
                else if(bl.checkTimecardId(timecard_id) == false){
                    return Response.ok("{\"error\":\""+  "timecard_id does not exists!" + "\"}").build();
                }
                else{
                    Timecard tc = dl.getTimecard(timecard_id);
                    tc.setStartTime(new Timestamp(st.getTime()));
                    tc.setEndTime(new Timestamp(et.getTime()));
                    tc.setEmpId(emp_id);
                    dl.updateTimecard(tc);
                    String list = "{\"timecard_id\":"+tc.getId()+",\"start_time\":\"" + tc.getStartTime()+"\",\"end_time\":\"" + tc.getEndTime() +"\","+
                    "\"emp_id\":" + tc.getEmpId()+ "}"; 

                    return Response.ok("{\"success\": "+  list +"}").build();
                }
        }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  e.getMessage() + "\"}").build();
        }
        finally {
            dl.close();
         }

    }


    @Path("/timecard")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTimecard(
        @QueryParam("company") String company,
        @QueryParam("timecard_id") int timecard_id 
    ){
        dl = new DataLayer(company);
        try{
            if(bl.checkTimecardId(timecard_id) == false){
                return Response.ok("{\"error\":\""+  "timecard_id does not exists!" + "\"}").build();
            }
            else{
               
                int delete = dl.deleteTimecard(timecard_id);
                if(delete <= 0 ){
                    return Response.ok("{\"error\":\""+  "TimeCard not deleted!" + "\"}").build();
                }
                return Response.ok("{\"success\": \"Timecard "+timecard_id+" deleted.\"}").build();
            }
            }
        catch (Exception e) {
            return Response.ok("{\"error\":\""+  "Delete process failed!"  + "\"}").build();
        }
        finally {
            dl.close();
            }
    }//end delete



    


}

