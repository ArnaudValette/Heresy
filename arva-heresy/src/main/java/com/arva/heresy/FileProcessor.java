package com.arva.heresy;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@FunctionalInterface
interface StringFunction {
    void apply(String s);
}

public class FileProcessor{

    public static void simpleCall(String pathName, StringFunction f) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathName))) {
                f.apply(reader.readLine());
        }
        catch (FileNotFoundException e) {
            System.err.println("NOENT");
        } catch (IOException e) {
        }

    }
    public static void iterativeCall(String pathName, StringFunction f){
        try (BufferedReader reader = new BufferedReader(new FileReader(pathName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                f.apply(line);
            }
        }
        catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
}
