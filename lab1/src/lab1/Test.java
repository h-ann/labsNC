package lab1;

import java.util.Arrays;

/**
 * Class for measuring time of sorting the array on specific filler and specific sorter
 * @author Anna Hulita
 * @version 1.0
 */
public class Test   {
    private int n;

     Test(){
        this.n = 1000;
    }

    Test(int n){
        this.n = n;
    }

    /**
     * Method for measuring time on specific filler and specific sorter
     * @param filler Class that is used as filler of array
     * @param sorter Class that is user as sorter of array
     */
    public void measure(Filler filler, Sorter sorter){

        int[] mass = new int[n];

        filler.fill(mass);
        long start = System.nanoTime();
        sorter.sort(mass);
        long end = System.nanoTime();

        System.out.println((end - start)+" "+filler.getClass());
        System.out.println(Arrays.toString(mass));

    }
}
