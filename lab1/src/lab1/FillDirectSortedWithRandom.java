package lab1;

import java.util.Random;

/**
 * This class contains method for filling array with random numbers in direct sorted order and random last element
 * @author Anna Hulita
 * @version 1.0
 * @see FillReverseSorted
 * @see FillDirectSorted
 * @see FillRandom
 */
@FillClass
public class FillDirectSortedWithRandom extends Filler {
    /**
     * Fill array with random numbers in direct sorted order and random last element
     * @param mass array, which will be filled
     */
   @Override
    public void fill(int[] mass) {
        Random r = new Random();
        mass[0]=r.nextInt(10);
        for(int i = 1; i<mass.length-1; i++){
            mass[i] = mass[i-1]+r.nextInt(10);
        }
        mass[mass.length-1] = r.nextInt(mass[mass.length-2]);
    }
}
