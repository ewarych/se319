package lab1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Admin {

    public void delete(String line) throws IOException{
        File tempFile = new File("TempFile.txt");
        File input = new File("chat.txt");
        BufferedReader br = new BufferedReader(new FileReader(input));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String currentLine;
        while((currentLine = br.readLine()) != null) {
            String number = currentLine.substring(0, 13);
            if(number.equals(line)) continue;
            writer.write(currentLine);
        }
        writer.close();
        br.close();
        tempFile.renameTo(input);
    }
}
