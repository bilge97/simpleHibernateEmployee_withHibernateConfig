package com.example.demo;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;


public class ManageEmployee {
    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();


        } catch (Throwable ex) {
            System.err.println("Failed" + ex);
            throw new ExceptionInInitializerError(ex);
        }

        ManageEmployee manageEmployee = new ManageEmployee();
        Integer employee1 = manageEmployee.addEmployee("bilge", "krtgl", 10);
        Integer employee2 = manageEmployee.addEmployee("merve", "krtgl", 10);

        manageEmployee.updateEmployee(1,40);
        manageEmployee.listEmployees();
        manageEmployee.totalSalary();


    }

    //ADD EMPLOYEE
    public Integer addEmployee(String fname, String lname, int salary){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;

        try {
            tx = session.beginTransaction();
            Employee employee = new Employee(fname, lname, salary);
            employeeID = (Integer) session.save(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }

    //DELETE EMPLOYEE
    public Integer deleteEmployee(Integer EmployeeID){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Employee employee = (Employee)session.get(Employee.class, EmployeeID);
            session.delete(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return EmployeeID;
    }

    //LIST EMPLOYEE
    public void listEmployees( ) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Employee.class);
            // Add restriction.
            cr.add(Restrictions.gt("salary", 2000));
            List employees = cr.list();

            for (Iterator iterator = employees.iterator(); iterator.hasNext();){
                Employee employee = (Employee) iterator.next();
                System.out.print("First Name: " + employee.getFirstName());
                System.out.print("  Last Name: " + employee.getLastName());
                System.out.println("  Salary: " + employee.getSalary());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    //UPDATE EMPLOYEE
    public void updateEmployee(Integer EmployeeID, int salary ){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Employee employee = (Employee)session.get(Employee.class, EmployeeID);
            employee.setSalary( salary );
            session.update(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    //TOTAL SALARY
    public void totalSalary(){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Employee.class);

            // To get total salary.
            cr.setProjection(Projections.sum("salary"));
            List totalSalary = cr.list();

            System.out.println("Total Salary: " + totalSalary.get(0) );
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    //COUNT EMPLOYEE
    public void countEmployee(){
        Session session = factory.openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Employee.class);
            //To count employees
            criteria.setProjection(Projections.rowCount());
            List rowCount = criteria.list();
            System.out.println("Total employees : " + rowCount.get(0));
            tx.commit();
        }

        catch(HibernateException e){
            if(tx!=null) tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }


    }
}
