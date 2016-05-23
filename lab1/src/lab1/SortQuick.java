package lab1;

/**
 * This class contains method for sorting array
 * @author Anna Hulita
 * @version 1.0
 * @see SortMerge
 */
@SortClass
public class SortQuick extends Sorter{

    private int array[];
    private int length;

     /**
     * Method for sorting array using quick sort method
     * @param mass array, witch will be sorted
     */
    @Override
    public void sort(int[] mass) {
        if (mass == null || mass.length == 0) {
            return;
        }
        this.array = mass;
        length = mass.length;
        quickSort(0, length - 1);
    }

    /**
     * Sort array from lowerIndex to higherIndex using quick sort method
     * @param lowerIndex the left-most index of the subarray
     * @param higherIndex the right-most index of the subarray
     */
    private void quickSort(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        while (i <= j) {
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                swap(i, j, array);
                i++;
                j--;
            }
        }
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
}
