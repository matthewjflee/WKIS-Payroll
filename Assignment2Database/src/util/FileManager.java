package util;

import model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    private User user;
    private String filename;

    public FileManager() {
    }

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
        String username = user.getUsername();
        String password = user.getPassword();
        String path;
        String ctrlFile = ".ctl";
        String logFile = ".log";

        String sqlldr = "sqlldr userid=" + username + "/" + password + " control="
                        + path + "/" + ctrlFile + " log=" + path + "/" + filename;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
