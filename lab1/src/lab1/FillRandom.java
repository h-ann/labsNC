package lab1;
import java.util.Random;

/**
 * This class contains method for filling array with random numbers
 * @author Anna Hulita
 * @version 1.0
 * @see FillReverseSorted
 * @see FillDirectSorted
 * @see FillDirectSortedWithRandom
 */
@FillClass
public class FillRandom extends Filler {

    /**
     * Fill array with random numbers
     * @param mass array, which will be filled
     */
    @Override
    public void fill(int[] mass) {
        Random r = new Random();
        for(int i=0; i<mass.length; i++){
            mass[i] = r.nextInt(1000);
        }
    }


}
