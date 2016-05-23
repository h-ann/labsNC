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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class ArrayJournal implements Journal, Serializable {
    public Record[] records;
    int numOfRecords;
    int resizeStep = 5;

    public ArrayJournal(ArrayJournal journal){
        this.numOfRecords=journal.numOfRecords;
        this.records= new Record[journal.size()];
        for(int i=0;i<journal.size();i++){
            this.records[i]=journal.get(i);
        }
    }

    public ArrayJournal(){
        records = new Record[5];
        numOfRecords=0;
    }

    private void resize(){
        Record[] recordsnew = new Record[records.length+resizeStep];
        for(int i = 0; i<records.length;i++){
            recordsnew[i]=records[i];
        }
        records=recordsnew;
    }

    @Override
    public void add(Record record) {
        if(numOfRecords==records.length){
            resize();
        }
        records[numOfRecords++]=record;
    }

    @Override
    public void add(Journal j) {
        for(int i= 0;i<j.size();i++){
            if(numOfRecords==records.length){
                resize();
            }
            records[numOfRecords++]=j.get(i);
        }
    }

    @Override
    public void remove(Record r) {
        int position=-1;
        for(int i = 0; i<numOfRecords;i++){
            if(records[i]==r){
                position = i;
            }
        }
        if(position!=-1) {
            Record[] recordsnew = new Record[records.length];
            for (int i = 0; i < position; i++) {
                recordsnew[i] = records[i];
            }
            for (int i = position + 1; i < numOfRecords; i++) {
                recordsnew[i-1] = records[i];
            }
            records = recordsnew;
        }else{
            System.out.println("Can`t found record in journal");
        }
    }

    @Override
    public Record get(int index) {
        return records[index];
    }

    @Override
    public void set(int index, Record record) {
        records[index]=record;
    }

    @Override
    public void insert(int index, Record record) {
        int newsize;
        if(numOfRecords==records.length){newsize=records.length+resizeStep;}
        else {newsize=records.length;}
        Record[] recordsnew = new Record[newsize];
        for(int i = 0; i<index;i++){
            recordsnew[i]=records[i];
        }
        recordsnew[index]=record;
        for(int i = index+1; i<records.length;i++){
            recordsnew[i]=records[i-1];
        }
        records=recordsnew;
    }

    @Override
    public void remove(int index) throws JournalIndexException{
        if(index<numOfRecords) {
            Record[] recordsnew = new Record[this.records.length];
            for(int i=0;i<index;i++){
                recordsnew[i]=records[i];
            }
            for(int i = index+1; i<numOfRecords;i++){
                recordsnew[i-1]=records[i];
            }
            records=recordsnew;
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
        if((toIndex>records.length-1)||(fromIndex<0)){
            throw new JournalIndexException("incorrect index to remove");
        }else{
            Record[] recordsnew = new Record[this.records.length];
            for(int i=0; i<fromIndex;i++){
                recordsnew[i]=records[i];
            }
            for(int i=toIndex+1;i<numOfRecords;i++){
                recordsnew[i-(toIndex-fromIndex)-1]=records[i];
            }
            records=recordsnew;
        }
    }

    @Override
    public void removeAll() {
        for(int i=0; i<numOfRecords; i++){
            records[i]=null;
        }
        numOfRecords=0;
    }

    @Override
    public int size() {
        return records.length;
    }

    @Override
    public ArrayJournal filter(String s) {
        ArrayJournal newJournal = new ArrayJournal();
        int iterator = 0;
        for(int i=0; i<numOfRecords; i++) {
            if (records[i].toString().contains(s)){
                newJournal.records[iterator++]=records[i];
            }
        }
        return newJournal;
    }

    @Override
    public ArrayJournal filter(Date fromDate, Date toDate) {
        ArrayJournal newJournal = new ArrayJournal();
        int iterator = 0;
        for(int i=0; i<numOfRecords; i++) {
            if (records[i].getTime().compareTo(fromDate)==1 &&
                    records[i].getTime().compareTo(toDate)==-1){
                newJournal.records[iterator++]=records[i];
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
        for(int i = 0; i<numOfRecords; i++){
            System.out.println(records[i].toString());
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

        for(int i=0; i<numOfRecords;i++){
            Record r=records[i];

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
        StreamResult result = new StreamResult(new File("journal.xml"));

        transformer.transform(source, result);
        System.out.println("File saved!");

    }

    public Record[] getRecords() {
        return records;
    }

    public void sortBy(Comparator c) {
        Arrays.sort(records, c);
    }

    public static class JournalIndexException extends Exception {
        JournalIndexException() { super(); }
        JournalIndexException(String s) { super(s); }
    }
}
