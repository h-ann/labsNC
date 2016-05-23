package lab1;

import java.util.Arrays;

/**
 * This class contains method for sorting array
 * @author Anna Hulita
 * @version 1.0
 * @see SortQuick
 * @see SortMerge
 */
@SortClass
public class SortNormal extends Sorter {

    /**
     * Method for sorting array using standard method
     * @param mass array, witch will be sorted
     */
    @Override
    public void sort(int[] mass) {
        Arrays.sort(mass);
    }
}
