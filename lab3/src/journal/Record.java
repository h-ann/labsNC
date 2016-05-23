package journal;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.comparators.ComparatorChain;

public class Record implements Serializable {
    final private Date time;
    final private Importance importance;
    final private String resourceOfMessage;
    final private String message;

    public Record(Date time, Importance importance,
           String resourceOfMessage, String message ){
        if (resourceOfMessage.contains(" "))
            throw new IllegalSourceException("Bad source: " + resourceOfMessage);
        if (message.contains("\n"))
            throw new IllegalMessageException("Bad message: " + message);
        this.time=time;
        this.importance=importance;
        this.resourceOfMessage=resourceOfMessage;
        this.message=message;
    }

    Record(String str)   {
        Pattern pattern = Pattern.compile
                ("(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+(\\.|!+)\\s+(\\w+)\\s+(.*)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()){
            try {
                this.time=dateFormat.parse(matcher.group(1));
            } catch (ParseException exeption) {
                throw new IllegalArgumentException("can`t parse date");
            }
            String imp = matcher.group(2);
            if(imp.equals(".")){
                this.importance=Importance.one;
            }else if(imp.equals("!")){
                this.importance=Importance.two;
            }else if(imp.equals("!!!")){
                this.importance=Importance.three;
            }else if(imp.equals("!!!!!")){
                this.importance=Importance.four;
            }else{
                System.out.println("Importance " + imp + " is bad. Importance . is set instead");
                this.importance=Importance.one;
            }
            if (matcher.group(3).contains(" "))
                throw new IllegalSourceException("Bad source: " + matcher.group(3));
            if (matcher.group(4).contains("\n"))
                throw new IllegalMessageException("Bad message: " + matcher.group(4));
            this.resourceOfMessage=matcher.group(3);
            this.message=matcher.group(4);
        }else{
            throw new IllegalArgumentException("not match pattern");
        }
    }

    public static DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String toString(){
        String importanceString = String.format("%-5s",importance.getString());
        String result=""+dateFormat.format(time)+" "+importanceString+
                " "+resourceOfMessage+" "+message;
        return result;
    }

    public Date getTime(){
        return time;
    }

    public int getImportance(){
        return importance.getValue();
    }
    public String getImportanceString(){
        return importance.getString();
    }

    public String getSource(){
        return resourceOfMessage;
    }

    public String getMessage(){
        return message;
    }

    public static class IllegalSourceException extends
            IllegalArgumentException {
        IllegalSourceException(String s) { super(s); }
    }

    public static class IllegalMessageException extends
            IllegalArgumentException {
        IllegalMessageException(String s) { super(s); }
    }

    static class DateComparator implements Comparator<Record> {
        public int compare(Record r1, Record r2) {
            return r1.getTime().compareTo(r2.getTime());
        }
    }
    static class ImportanceComparator implements Comparator<Record> {
        public int compare(Record r1, Record r2) {
            return r1.getImportance() - r2.getImportance();
        }
    }
    static class SourceComparator implements Comparator<Record> {
        public int compare(Record r1, Record r2) {
            return r1.getSource().compareTo(r2.getSource());
        }
    }
    static ComparatorChain ImportanceDateComparator = new ComparatorChain();
    static ComparatorChain ImportanceSourceDateComparator = new ComparatorChain();
    static ComparatorChain SourceDateComparator = new ComparatorChain();
    static {
        ImportanceDateComparator.addComparator(new ImportanceComparator());
        ImportanceDateComparator.addComparator(new DateComparator());

        ImportanceSourceDateComparator.addComparator(new ImportanceComparator());
        ImportanceSourceDateComparator.addComparator(new SourceComparator());
        ImportanceSourceDateComparator.addComparator(new DateComparator());

        SourceDateComparator.addComparator(new SourceComparator());
        SourceDateComparator.addComparator(new DateComparator());
    }
}
