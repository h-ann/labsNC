package lab1;

import java.util.Random;

/**
 * This class contains method for filling array with random numbers in reverse sorted order
 * @author Anna Hulita
 * @version 1.0
 * @see FillReverseSorted
 * @see FillDirectSorted
 * @see FillDirectSortedWithRandom
 */
@FillClass
public class FillReverseSorted extends Filler {

    /**
     * Fill array with random numbers in reverse sorted order
     * @param mass array, which will be filled
     */
    @Override
    public void fill(int[] mass) {
        Random r = new Random();
        mass[0]=mass.length * 5;
        for(int i = 1; i<mass.length; i++){
            mass[i] = mass[i-1]-r.nextInt(10);
        }
    }
}
