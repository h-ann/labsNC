package lab1;

/**
 * Abstract class with method for sorting array
 *
 * @author Anna Hulita
 * @version 1.0
 */
public abstract class Sorter {
    /**
     * Sort the array
     * @param mass array, witch will be sorted
     */
    public abstract  void sort (int[] mass);

    /**
     * Swap elements at i and j position in array mass
     * @param i
     * @param j
     * @param mass
     */
    protected void swap(int i, int j, int[] mass) {
        int t = mass[i];
        mass[i] = mass[j];
        mass[j] = t;
    }
}