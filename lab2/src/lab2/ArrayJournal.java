package lab2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by anna on 13.11.2015.
 */
public class ArrayJournal implements Journal {
    public Record[] records;
    int numOfRecords;
    int resizeStep = 5;

    ArrayJournal(ArrayJournal journal){
        this.numOfRecords=journal.numOfRecords;
        this.records= new Record[journal.size()];
        for(int i=0;i<journal.size();i++){
            this.records[i]=journal.get(i);
        }
    }

    ArrayJournal(){
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

    public void sortBy(Comparator c) {
        Arrays.sort(records, c);
    }

    public static class JournalIndexException extends Exception {
        JournalIndexException() { super(); }
        JournalIndexException(String s) { super(s); }
    }
}
