package util;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileUtil {
    
    public static String loadFile(String name) throws Exception {
        StringBuffer data = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(name));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            data.append(readData);
        }
        reader.close();
        return data.toString();
    }
}        