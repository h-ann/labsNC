package lab1;

/**
 * This class contains method for sorting array
 * @author Anna Hulita
 * @version 1.0
 * @see SortQuick
 * @see SortMerge
 */
@SortClass
public class SortBubleDescent extends Sorter {

    /**
     * Method for sorting array using bubble sort descent method
     * @param mass array, wich will be sorted
     */
    @Override
    public void sort(int[] mass) {
        int k=0;
        for (int i = mass.length -1; i >=0 ; i--) {
            k++;
            for (int j = mass.length -1; j >=k ; j--) {
                if (mass[j ] < mass[j-1]) {
                    swap(j, j - 1, mass);
                }
            }
        }
    }
}
