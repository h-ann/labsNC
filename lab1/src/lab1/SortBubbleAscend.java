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
public class SortBubbleAscend extends Sorter {

    /**
     * Method for sorting array using bubble sort ascent method
     * @param mass array, wich will be sorted
     */
    @Override
    public void sort(int[] mass) {
        //int temp;
        for (int i = 0; i < mass.length - 1; i++) {

            for (int j = 1; j < mass.length - i; j++) {
                if (mass[j - 1] > mass[j]) {
                    swap(j, j - 1, mass);
                }
            }
        }
    }
}

