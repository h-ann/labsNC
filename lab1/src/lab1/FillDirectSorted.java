package lab1;

import java.util.Random;

/**
 * This class contains method for filling array with random numbers in direct sorted order
 * @author Anna Hulita
 * @version 1.0
 * @see  FillReverseSorted
 * @see FillDirectSortedWithRandom
 * @see FillRandom
 */
@FillClass
public class FillDirectSorted extends Filler {

    /**
     * Fill array with random numbers in direct sorted order
     * @param mass array, which will be filled
     */
    @Override
    public void fill(int[] mass) {
        Random r = new Random();
        mass[0]=r.nextInt(10);
        for(int i = 1; i<mass.length; i++){
            mass[i] = mass[i-1]+r.nextInt(10);
        }
    }

}

