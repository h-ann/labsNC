package lab3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Employee implements Comparable<Employee>, Serializable  {
    protected String name;
    protected ArrayList<Project> projects;

    Employee(String name){
        this.name=name;
        this.projects=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Project> getProjects(){
        return projects;
    }


    public Manager makeManager() {
        Manager manager = new Manager(this.name);
        manager.getProjects().addAll(projects);
        return manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        return name.equals(((Employee) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public int compareTo(Employee o) {
        return this.name.compareTo(o.name);
    }
}
