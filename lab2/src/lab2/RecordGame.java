package lab2;

import java.util.Date;

/**
 * Created by anna on 13.11.2015.
 */
public class RecordGame extends Record {
    int x;
    int y;

    RecordGame(String str,int x,int y) {
        super(str);
        this.x=x;
        this.y=y;
    }

    RecordGame(Date time, Importance importance, String resourceOfMessage, String message,int x, int y) {
        super(time, importance, resourceOfMessage, message);
        this.x=x;
        this.y=y;
    }

    public String toString(){
        String result = ""+dateFormat.format(getTime()) +" "+getImportance()+" "+getSource()+" "+getMessage()+" "+x+" "+y;
        return result;
    }

}
