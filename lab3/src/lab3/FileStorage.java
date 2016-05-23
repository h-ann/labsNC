package lab3;

import journal.Journal;

import java.io.*;
import java.util.LinkedList;

public class FileStorage extends Firm {

    FileStorage(String name, Journal journal){
        super(name,journal);
    }

    @Override
    public void writeData() throws IOException {
        FileOutputStream fos = new FileOutputStream("database.txt");
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
        for(Employee e: employees){
            if(!(e instanceof Manager)) {
                for (Project p : e.projects) {
                    br.write(e.toString() + p);
                    br.newLine();
                }
            }
            if(e.getProjects().size()==0) {
                br.write(e.toString());
                br.newLine();
            }
        }
        br.close();
        fos.close();
    }

    @Override
    public Firm readData() throws IOException {
        FileInputStream fis = new FileInputStream("firm.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        LinkedList rows = new LinkedList();
        while ((line = br.readLine()) != null) {
            rows.add(line);
        }
        br.close();
        fis.close();

        for (int i = 0; i < rows.size(); i++) {
            line = (String) rows.get(i);
            String[] arr = line.split("\\s+");
            if(arr.length==1){
                Employee employee = addEmployee(arr[0]);
            }else {
                Employee employee = addEmployee(arr[0]);
                Customer customer = addCustomer(arr[1]);
                Employee manager = addEmployee(arr[2]);
                Project project = addProject(arr[3], customer, manager);
                addExecutorForProject(project, employee);
            }
        }
        return this;
    }
}
