package lab1;

/**
 * This class contains method for sorting array
 * @author Anna Hulita
 * @version 1.0
 * @see SortQuick
 * @see SortMerge
 */
@SortClass
public class SortExchange extends Sorter{

     /**
     * Method for sorting array using exchange sort method
     * @param mass array, wich will be sorted
     */
    @Override
    public void sort(int[] mass) {
        int m=1;
        while(m!=0){
             m=0;
            for(int i=0; i<mass.length-1;i++) {
                if (mass[i] > mass[i + 1]) {
                    swap(i, i + 1, mass);
                    m = 1;
                }
            }
        }
    }
}
