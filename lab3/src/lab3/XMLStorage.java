package lab3;

import journal.Journal;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLStorage extends Firm {

    XMLStorage(String name, Journal journal){
        super(name, journal);
    }

    @Override
    public void writeData() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("firm");
        doc.appendChild(rootElement);

        for(Employee e:employees) {
            if(!(e instanceof Manager)) {
                Element employee = doc.createElement("employee");
                rootElement.appendChild(employee);
                employee.setAttribute("employeename", e.getName());

                if(e.projects.size()!=0) {
                    for(Project p:e.projects) {
                        Element project = doc.createElement("project");
                        employee.appendChild(project);

                        Element prname = doc.createElement("prname");
                        prname.appendChild(doc.createTextNode(p.getName()));
                        project.appendChild(prname);

                        Element customer = doc.createElement("customer");
                        customer.appendChild(doc.createTextNode(p.getCustomer().toString()));
                        project.appendChild(customer);

                        Element manager = doc.createElement("manager");
                        manager.appendChild(doc.createTextNode(p.getManager().toString()));
                        project.appendChild(manager);
                    }
                }
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("firm1.xml"));

        transformer.transform(source, result);
        System.out.println("File saved!");
    }

    @Override
    public Firm readData() throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File("firm.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("employee");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;

                Employee employee = addEmployee(eElement.getAttribute("employeename"));
                Customer customer = addCustomer(eElement.getElementsByTagName("customer").item(0).getTextContent());
                Employee manager = addEmployee(eElement.getElementsByTagName("manager").item(0).getTextContent());
                Project project = addProject(eElement.getElementsByTagName("prname").item(0).getTextContent(),
                        customer, manager);
                addExecutorForProject(project, employee);
            }
        }
        System.out.println(employees.get(0).getName()+"   numOfProjects "+employees.get(0).projects.size());
        return this;
    }
}
