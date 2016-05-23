package lab1;

/**
 * This class contains method for sorting array
 * @author Anna Hulita
 * @version 1.0
 * @see SortQuick
 */
@SortClass
public class SortMerge extends  Sorter {
    private int[] array;
    private int[] tempMergArr;
    private int length;

     /**
     * Method for sorting array using merge sort  method
     * @param mass array, wich will be sorted
     */
    @Override
    public void sort(int[] mass) {
        this.array = mass;
        this.length = mass.length;
        this.tempMergArr = new int[length];
        mergeSort(0, length - 1);
    }

    /**
     * Method that makes recursive calls.
     * @param lowerIndex the left-most index of the subarray
     * @param higherIndex the right-most index of the subarray
     */
    private void mergeSort(int lowerIndex, int higherIndex) {

        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            mergeSort(lowerIndex, middle);
            mergeSort(middle + 1, higherIndex);
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }

    /**
     * Method that merges two sorted halves of a subarray.
     * @param lowerIndex the left-most index of the subarray
     * @param middle the middle index of the subarray
     * @param higherIndex the right-most index of the subarray
     */
    private void mergeParts(int lowerIndex, int middle, int higherIndex) {

        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] <= tempMergArr[j]) {
                array[k] = tempMergArr[i];
                i++;
            } else {
                array[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempMergArr[i];
            k++;
            i++;
        }
    }
}
