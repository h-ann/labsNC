package lab2;

import java.text.ParseException;
import java.util.Scanner;

/**
 * Created by anna on 13.11.2015.
 */
public class Main {
    public static void main(String[] args) throws ParseException {

        GameOfLife gameOfLife = new GameOfLife();

        CollectionJournal collectionJournal = new CollectionJournal();
        ArrayJournal arrayJournal = new ArrayJournal();

        playWithFournal(gameOfLife, arrayJournal); // test arrayJournal
        //playWithFournal(gameOfLife, collectionJournal); // or test collectionJournal

    }

    private static void playWithFournal(GameOfLife gameOfLife, Journal journal) {
        boolean[][] world = gameOfLife.gen();
        gameOfLife.show(world, journal);
        System.out.println();
        world = gameOfLife.nextGen(world);
        gameOfLife.show(world, journal);
        Scanner s = new Scanner(System.in);
        while (s.nextLine().length() == 0) {
            System.out.println();
            world = gameOfLife.nextGen(world);
            gameOfLife.show(world, journal);
        }
    }
}
