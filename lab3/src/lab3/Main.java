package lab3;

import journal.ArrayJournal;
import journal.CollectionJournal;
import lab1.SortQuick;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, XMLStreamException, ParserConfigurationException, SAXException, TransformerException, SQLException {

        Firm firm = new FileStorage("Firm",new CollectionJournal());
        firm.readData();

        firm.getJournal().printRecords();
        firm.getJournal().saveToXMLFile();

        firm.sortAscending(firm.employees,new SortQuick<>());
        for(Employee e:firm.employees){
            System.out.println(e.toString());
        }

        Firm firm1= new XMLStorage("Firm1", new ArrayJournal());
        firm1.setSomeData();
        firm1.writeData();

        Firm firm2 = new BinaryStorage("Firm2", new CollectionJournal());
        //firm2.setSomeData();
        //firm2.writeData();
        firm2 = firm2.readData();
        System.out.println("\nemployees of firm2:");
        for(Employee e:firm2.employees){
            System.out.println(e.toString());
        }

        /*Firm firm3 = new JDBCStorageDAO("Firm3", new CollectionJournal());
        firm3.connectDB();
        Employee employee = firm3.addEmployee("emp1");
        ArrayList<Project> pr = firm3.getProjectsOfEmployee(employee);
        firm3.closeConnection();
        System.out.println("projects with emp1 from JDBC: "+pr);*/
    }
}
