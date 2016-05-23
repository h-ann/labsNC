package lab3;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable, Comparable<Customer>{
    String name;
    ArrayList<Project> projects;

    Customer(String name){
        this.name=name;
        this.projects = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public ArrayList<Project> getProjects(){
        return projects;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Customer)) return false;
        if(o==this) return true;
        return name.equals(((Customer) o).getName());
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public int compareTo(Customer o) {
        return this.name.compareTo(o.name);
    }
}
