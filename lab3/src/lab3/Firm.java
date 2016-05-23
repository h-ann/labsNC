package lab3;

import journal.Importance;
import journal.Journal;
import journal.Record;
import lab1.Sorter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Firm implements Serializable {
    protected String nameOfFirm = "";

    protected ArrayList<Employee> employees;
    protected ArrayList<Customer> customers;
    protected ArrayList<Manager> managers;
    protected ArrayList<Project> projects;

    protected Journal journal;

    public Firm(String name, Journal journal){
        nameOfFirm=name;
        this.journal = journal;
        employees=new ArrayList<>();
        customers=new ArrayList<>();
        managers=new ArrayList<>();
        projects=new ArrayList<>();
    }

    public void writeData() throws IOException, ParserConfigurationException, TransformerException, SQLException, ClassNotFoundException {}
    public Firm readData() throws IOException, ClassNotFoundException, XMLStreamException, ParserConfigurationException, SAXException {return null;}
    public Connection connectDB() throws ClassNotFoundException, SQLException {return null;}
    public  void closeConnection(){}

    public Employee addEmployee(String name){
        Employee employee = new Employee(name);
        if(!(employees.contains(employee))){
            employees.add(employee);
            journal.add(new Record(new Date(), Importance.two, nameOfFirm, "Message: Employee "+name+" is hired to firm"));
            return  employee;
        }else {
            return employees.get(employees.indexOf(employee));
        }
    }

    public Customer addCustomer(String name){
        Customer customer = new Customer(name);
        if(!(customers.contains(customer))) {
            customers.add(customer);
            journal.add(new Record(new Date(), Importance.three, nameOfFirm, "Message: Customer "+name+" has come to firm"));
            return customer;
        }
        return customers.get(customers.indexOf(customer));
    }

    public Project addProject(String name, Customer customer, Employee manager) {
        for (Customer c : customers) {
            if (c.equals(customer)) {

                for (Employee e : employees) {
                    if (e.equals(manager)) {
                        Manager newManager;
                        if (!(e instanceof Manager)) {
                            newManager = e.makeManager();
                            employees.remove(e);
                            employees.add(newManager);
                            managers.add(newManager);
                        } else {
                            newManager = (Manager) e;
                        }
                        Project project = new Project(name, customer, newManager);
                        if(!(projects.contains(project))) {
                            journal.add(new Record(new Date(), Importance.four, nameOfFirm, "Message: Project "+name+" is added to firm"));
                            projects.add(project);
                            return project;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void addExecutorForProject(Project project, Employee executor){
        for (Project p : projects) {
            if (p.equals(project)) {
                for (Employee e : employees) {
                    if (executor.equals(e)) {
                        p.getExecutors().add(e);
                        e.getProjects().add(project);
                        journal.add(new Record(new Date(), Importance.one, nameOfFirm,
                                "Message: Executor "+executor.getName()+" is added to ptoject "+project.getName()));
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<Employee> getExecutorsOfProject(Project project) {
        for(Project p:projects) {
            if(project.equals(p)){
                ArrayList<Employee> result = new ArrayList<>();
                result.addAll(p.getExecutors());
                return result;
            }
        }
        return null;
    }

    public ArrayList<Project> getProjectsOfEmployee(Employee employee) throws SQLException {
        for(Employee e:employees) {
            if(employee.equals(e)){
                ArrayList<Project> result = new ArrayList<>();
                result.addAll(e.getProjects());
                return result;
            }
        }
        return null;
    }

    public ArrayList<Employee> getEmployeeWithoutProject() throws SQLException {
        ArrayList<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getProjects().isEmpty()) {
                result.add(e);
            }
        }
        return result;
    }

    public ArrayList<Employee> getEmployeeManagedBy(Manager manager) throws SQLException {
        for (Manager m : managers) {
            if (manager.equals(m)) {
                ArrayList<Employee> result = new ArrayList<>();
                for (Project p : m.getManagedProjects()) {
                    result.addAll(p.getExecutors());
                }
                result.remove(manager);
                return result;
            }
        }
        return null;
    }

    public ArrayList<Manager> getManagersOfExecutor(Employee employee){
        for(Employee e: employees){
            if(employee.equals(e)){
                ArrayList<Manager> result = new ArrayList<>();
                for(Project p: e.getProjects()){
                    result.add(p.getManager());
                }
                return result;
            }
        }
        return null;
    }

    public ArrayList<Employee> getCollaborators(Employee employee){
        for(Employee e: employees){
            if(employee.equals(e)){
                ArrayList<Employee> result = new ArrayList<>();
                for(Project p:e.getProjects()){
                    result.addAll(p.getExecutors());
                }
                result.remove(e);
                return result;
            }
        }
        return null;
    }

    public ArrayList<Project> getProjectsForCustomer(Customer customer){
        for (Customer c:customers){
            if(customer.equals(c)){
                ArrayList<Project> result = new ArrayList<>();
                result.addAll(c.getProjects());
                return result;
            }
        }
        return null;
    }

    public ArrayList<Employee> getEmployeesForCustomer(Customer customer){
        for(Customer c:customers){
            if(customer.equals(c)){
                ArrayList<Employee> result = new ArrayList<>();
                for(Project p: c.getProjects()){
                    result.addAll(p.getExecutors());
                }
                return result;
            }
        }
        return null;
    }

    public Journal getJournal(){return journal;}

    public void setSomeData(){// helper method for testing
        Employee employee2 = addEmployee("emp2");
        Employee employee = addEmployee("emp1");
        Customer customer = addCustomer("cust1");
        Employee manager = addEmployee("manag1");
        Project project = addProject("pr1",customer,manager);
        addExecutorForProject(project,employee);

    }

    public <T extends Comparable> void sortAscending(ArrayList<T> list, Sorter sorter){
        Comparable[] arr = new Comparable[list.size()];
        for(int i=0; i<list.size(); i++){
            arr[i]=list.get(i);
        }
        sorter.sort(arr);
        for(int i=0; i<list.size(); i++){
            list.set(i, (T) arr[i]);
        }
    }

    public <T extends Comparable> void sortDescending(ArrayList<T> list, Sorter sorter){
        Comparable[] arr = new Comparable[list.size()];
        for(int i=0; i<list.size(); i++){
            arr[i]=list.get(i);
        }
        sorter.sort(arr);
        for(int i=0; i<list.size(); i++){
            list.set(i, (T) arr[list.size()-1-i]);
        }
    }
}
