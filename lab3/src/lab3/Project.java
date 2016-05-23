package lab3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Project implements Serializable, Comparable<Project> {
    private String name;
    private Customer customer;
    private Manager manager;
    private ArrayList<Employee> executors;

    Project(String name, Customer customer, Manager manager){
        this.name= name;
        this.customer=customer;
        this.manager=manager;
        executors = new ArrayList<>();
        executors.add(manager);
        manager.getProjects().add(this);
        manager.getManagedProjects().add(this);
        customer.getProjects().add(this);
    }

    public String getName(){
        return name;
    }

    public Customer getCustomer(){
        return customer;
    }

    public Manager getManager(){
        return manager;
    }

    public ArrayList<Employee> getExecutors(){
        return executors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        boolean res1 = name.equals(((Project) o).name);
        return res1;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public int compareTo(Project o) {
        return this.name.compareTo(o.name);
    }
}
