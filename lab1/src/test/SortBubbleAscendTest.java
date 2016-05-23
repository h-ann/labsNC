package test;

import lab1.SortBubbleAscend;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by anna on 12.11.2015.
 */
public class SortBubbleAscendTest {
    int[] mass = {1,2,3,4,5,6,7,8,9};
    SortBubbleAscend sba = new SortBubbleAscend();
    private boolean consistsElem(int elem, int[] mass){
        for(int i=0;i<mass.length;i++){
            if (elem==mass[i]) {return true; }
        }
        return false;
    }

    @Test
    public void testSortOrder() throws Exception {
        sba.sort(mass);
        for(int i=0;i<mass.length-1;i++){
            assertTrue(mass[i]<mass[i+1]);
        }
    }

    @Test
    public void testSortSameValues() throws Exception {
        int[] massInitial = {1,2,3,4,5,6,7,8,9};
        sba.sort(mass);
        for(int i=0;i<massInitial.length;i++) {
            assertTrue(consistsElem(massInitial[i], mass));
        }
    }
    @Test
    public void testSortSize() throws Exception {
        sba.sort(mass);
        assertTrue(mass.length==9);
    }
    @Test
    public void testSortTypes() throws Exception {
        Double[] massDouble = {1.0, 2.2, 3.8};
        int[] massEmpty = {};
        SortBubbleAscend sba = new SortBubbleAscend();
       // sba.sort(massDouble);
        sba.sort(massEmpty);
    }

    @Test(timeout = 1)
    public void testDuration() throws Exception {
        sba.sort(mass);
    }

}