package lab2;

import java.util.*;

/**
 * Created by anna on 13.11.2015.
 */
public class CollectionJournal implements Journal {

    private ArrayList<Record> records;

    CollectionJournal(ArrayJournal journal){
        this.records = new ArrayList();
        Collections.addAll(records, journal.records);
    }

    CollectionJournal(){
        this.records = new ArrayList();
    }

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
    public void sortBy(Comparator c) {
        Collections.sort(records, c);
    }

    public static class JournalIndexException extends Exception {
        JournalIndexException() { super(); }
        JournalIndexException(String s) { super(s); }
    }
}