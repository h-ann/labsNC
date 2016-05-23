package journal;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Date;

public interface Journal  {
    void add(Record r);
    void add(Journal j);
    void remove(Record r);
    Record get(int index);
    void set(int index, Record record);
    void insert(int index, Record record);
    void remove(int index) throws ArrayJournal.JournalIndexException, CollectionJournal.JournalIndexException;
    void remove(int fromIndex, int toIndex) throws ArrayJournal.JournalIndexException, CollectionJournal.JournalIndexException;
    void removeAll();
    int size();
    Journal filter(String s);
    Journal filter(Date fromDate, Date toDate);
    void sortByDate();
    void sortByImportanceDate();
    void sortByImportanceSourceDate();
    void sortBySourceDate();
    void printRecords();
    void saveToTextFile() throws IOException ;
    void saveToBinaryFile() throws IOException;
    void saveToXMLFile() throws ParserConfigurationException, TransformerException;
}
