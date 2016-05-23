package journal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class CollectionJournal implements Journal,Serializable {

    public ArrayList<Record> records;

    CollectionJournal(ArrayJournal journal){
        this.records = new ArrayList();
        Collections.addAll(records, journal.records);
    }

    public CollectionJournal(){
        this.records = new ArrayList();
    }

    public ArrayList<Record> getRecords(){return records;}

    @Override
    public void add(Record r) {
        records.add(r);
    }

    @Override
    public void add(Journal j) {
        for(int i=0;i<j.size();i++){
            this.records.add(j.get(i));
        }
    }

    @Override
    public void remove(Record r) {
        records.remove(r);
    }

    @Override
    public Record get(int index) {
        return records.get(index);
    }

    @Override
    public void set(int index, Record record) {
        records.set(index,record);
    }

    @Override
    public void insert(int index, Record record) {
        records.add(index,record);
    }

    @Override
    public void remove(int index) throws JournalIndexException {
        if(index<size()) {
            records.remove(index);
        }else{
            throw new JournalIndexException("incorrect index to remove");
        }
    }

    @Override
    public void remove(int fromIndex, int toIndex) throws JournalIndexException {
        if(fromIndex>toIndex){
            int temp = fromIndex;
            fromIndex =toIndex;
            toIndex = temp;
        }
        if((toIndex>size()-1)||(fromIndex<0)){
            throw new JournalIndexException("incorrect index to remove");
        }else {
            for (int i = fromIndex; i <= toIndex; i++) {
                records.remove(i);
            }
        }
    }

    @Override
    public void removeAll() {
        records.clear();
    }

    @Override
    public int size() {
        return records.size();
    }

    @Override
    public Journal filter(String s) {
        CollectionJournal newJournal = new CollectionJournal();
        int iterator = 0;
        for(int i=0; i<size(); i++) {
            if (get(i).toString().contains(s)){
                newJournal.add(get(i));
            }
        }
        return newJournal;
    }

    @Override
    public Journal filter(Date fromDate, Date toDate) {
        CollectionJournal newJournal = new CollectionJournal();
        int iterator = 0;
        for(int i=0; i<size(); i++) {
            if (get(i).getTime().compareTo(fromDate)==1 &&
                    get(i).getTime().compareTo(toDate)==-1){
                newJournal.add(get(i));
            }
        }
        return newJournal;
    }

    @Override
    public void sortByDate() {
        sortBy(new Record.DateComparator());
    }

    @Override
    public void sortByImportanceDate() {
        sortBy(Record.ImportanceDateComparator);
    }

    @Override
    public void sortByImportanceSourceDate() {
        sortBy(Record.ImportanceSourceDateComparator);
    }

    @Override
    public void sortBySourceDate() {
        sortBy(Record.SourceDateComparator);
    }

    @Override
    public void printRecords() {
        for(int i = 0; i<size(); i++){
            System.out.println(get(i).toString());
        }
    }

    @Override
    public void saveToTextFile() throws IOException {
        FileOutputStream fos = new FileOutputStream("journal.txt");
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
        for(Record r: records){
            br.write(r.toString());
            br.newLine();
        }
        br.close();
        fos.close();
    }

    @Override
    public void saveToBinaryFile() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("journal.bin"));
        out.writeObject(this);
        out.close();
    }

    @Override
    public void saveToXMLFile() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("journal");
        doc.appendChild(rootElement);

        for(Record r:records){

            Element record = doc.createElement("record");
            rootElement.appendChild(record);

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode(Record.dateFormat.format(r.getTime())));
            record.appendChild(time);

            Element importance = doc.createElement("importance");
            importance.appendChild(doc.createTextNode(r.getImportanceString()));
            record.appendChild(importance);

            Element resourceOfMessage = doc.createElement("resourceOfMessage");
            resourceOfMessage.appendChild(doc.createTextNode(r.getSource()));
            record.appendChild(resourceOfMessage);

            Element message = doc.createElement("message");
            message.appendChild(doc.createTextNode(r.getMessage()));
            record.appendChild(message);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("CollectionJournal.xml"));

        transformer.transform(source, result);
        System.out.println("File saved!");

    }


    public void sortBy(Comparator c) {
        Collections.sort(records, c);
    }

    public static class JournalIndexException extends Exception {
        JournalIndexException() { super(); }
        JournalIndexException(String s) { super(s); }
    }

}