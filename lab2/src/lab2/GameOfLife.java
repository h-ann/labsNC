package lab2;

import java.util.Date;

public class GameOfLife {
    private int height = 10;//num of rows
    private int width = 10;//num of columns

    public void show(boolean[][] grid, Journal journal){
        journal.removeAll();
        Record record;
        Date time;
        String s = "";
        boolean val = false;
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++) {
                val = grid[i][j];
                if (val) {
                    time = new Date();
                    record = new RecordGame(time, Importance.four, "game", "message", i, j);
                    journal.add(record);
                    s += "*";
                } else {
                    s += ".";
                }
            }
            s += "\n";
        }
        System.out.println(s);
        journal.printRecords();
    }


    public  boolean[][] gen(){
        boolean[][] grid = new boolean[height][width];
        for(int r = 0; r < 10; r++)
            for(int c = 0; c < 10; c++)
                if( Math.random() > 0.7 )
                    grid[r][c] = true;
        return grid;
    }

    public static boolean[][] nextGen(boolean[][] world){
        boolean[][] newWorld
                = new boolean[world.length][world[0].length];
        int num;
        for(int r = 0; r < world.length; r++){
            for(int c = 0; c < world[0].length; c++){
                num = numNeighbors(world, r, c);
                if( occupiedNext(num, world[r][c]) )
                    newWorld[r][c] = true;
            }
        }
        return newWorld;
    }

    public static boolean occupiedNext(int numNeighbors, boolean occupied){
        if( occupied && (numNeighbors == 2 || numNeighbors == 3))
            return true;
        else if (!occupied && numNeighbors == 3)
            return true;
        else
            return false;
    }

    private static int numNeighbors(boolean[][] world, int row, int col) {
        int num = world[row][col] ? -1 : 0;
        for(int r = row - 1; r <= row + 1; r++)
            for(int c = col - 1; c <= col + 1; c++)
                if( inbounds(world, r, c) && world[r][c] )
                    num++;

        return num;
    }

    private static boolean inbounds(boolean[][] world, int r, int c) {
        return r >= 0 && r < world.length && c >= 0 &&
                c < world[0].length;
    }
}