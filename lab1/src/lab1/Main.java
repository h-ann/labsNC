package lab1;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {

        System.out.println("just be happy");

        /*int[] mass = new int[600];
        FillDirectSorted fd = new FillDirectSorted();
        fd.fill(mass);
        for (int i = 0; i<mass.length; i++){
            System.out.println(mass[i]);
        }*/
        Adv advanced = new Adv();
        advanced.test();

    }

}
