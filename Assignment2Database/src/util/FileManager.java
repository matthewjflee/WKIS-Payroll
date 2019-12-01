package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

    private void loadFile(String filename) {
        try (BufferedReader inFile = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] spl;

            while ((line = inFile.readLine()) != null) {
                spl = line.split(";");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTable() {
        String username, password;
        String path;
        String ctrlFile = ".ctl";
        String logFile = ".log";

        String sqlldr = "sqlldr userid=" + username + "/" + password + " control="
                        + path + "/" + ctrlFile + " log=" + path + "/" + filename;
    }
}
