package fr.ulm.centrage;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.ulm.centrage.data.Ulm;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() throws IOException, ClassNotFoundException {

        final String fileName = "test.obj";

        // SERIALIZATION
        FileOutputStream fout = new FileOutputStream(fileName);
        ObjectOutputStream serializer = new ObjectOutputStream(fout);
        serializer.writeObject(new Ulm());
        serializer.close();

        // DESERIALIZATION
        FileInputStream fin = new FileInputStream(fileName);
        ObjectInputStream deserializer = new ObjectInputStream(fin);
        Ulm ulm = (Ulm) deserializer.readObject();
        deserializer.close();

        // PRINT
        System.out.println(ulm.getElement(Ulm.ElementDefaut.BAG).levier);
    }
}