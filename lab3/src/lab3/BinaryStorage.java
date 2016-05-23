package lab3;

import journal.Journal;

import java.io.*;

public class BinaryStorage extends Firm  {

    BinaryStorage(String name,Journal journal){
        super(name,journal);
    }

    @Override
    public void writeData() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("firm.bin"));
        out.writeObject(this);
        out.close();
    }

    @Override
    public Firm readData() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("firm.bin"));
     return (Firm) in.readObject();
    }
}
