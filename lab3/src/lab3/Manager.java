package lab3;

import java.io.Serializable;
import java.util.ArrayList;

public class Manager extends Employee implements Serializable {
    private ArrayList<Project> projectsManagedBy;

    Manager(String name){
        super(name);
        this.projectsManagedBy = new ArrayList<>();
    }

    public ArrayList<Project> getManagedProjects() {
        return projectsManagedBy;
    }

}
