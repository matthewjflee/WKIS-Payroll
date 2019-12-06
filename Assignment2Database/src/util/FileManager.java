package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import model.User;

//File manager
public class FileManager {
	
	
	//File Manager
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
	private String txtFileLocation;
	private String txtFileName;
	private String ctrlFile;
	private String path;
	private String logFile;
	private User user;
	private int exitValue;
	

    
    public void createControlFile() {
    	setTxtFileName();
    	setPath();
    	setCtrlFile();
    	setLogFile();
    	
    	try {
			FileWriter fw = new FileWriter(path + ctrlFile);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println("LOAD DATA");
			pw.println("INFILE " + "\'" + getTxtFileLocation() + "\\" + getTxtFileName() + "\'");
			pw.println("REPLACE");
			pw.println("INTO TABLE payroll_load");
			pw.println("FIELDS TERMINATED BY \';\' OPTIONALLY ENCLOSED BY \'\"\'");
			pw.println("TRAILING NULLCOLS");
			pw.println("(payroll_date DATE \"Month dd, yyyy\",");
			pw.println("employee_id,");
			pw.println("amount,");
			pw.println("status)");
			
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }

    public void loadTable() {
        String username = user.getUsername();
        String password = user.getPassword();
        path = getPath();
        ctrlFile = getCtrlFile() +  ".ctl";
        logFile = getLogFile() +  ".log";

        String sqlldr = "sqlldr userid=" + username + "/" + password + " control="
                        + path + "/" + ctrlFile + " log=" + path + "/" + logFile;
        
        	try {
        		Runtime rt = Runtime.getRuntime();
            	Process proc = rt.exec(sqlldr);
				exitValue = proc.waitFor();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	System.out.println(exitValue);
    }
    

    private void setTxtFileName() {
    	txtFileName = JOptionPane.showInputDialog("Please enter the name of the delimited text file (please include .txt):");
    }
    
    private void setPath() {
    	path = JOptionPane.showInputDialog("Please enter location for the files to be stored:");
    }
    
    private void setCtrlFile() {
    	System.out.println(txtFileName);
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	
    	for(int i = 0; i < textFileNameSplit.length; i++) {
    		System.out.println(textFileNameSplit[i]);
    	}
    	
    	ctrlFile = textFileNameSplit[0] + ".ctl";
    }
    
    private void setLogFile() {
    	
    	String[] textFileNameSplit = txtFileName.split("\\.");
    	logFile = textFileNameSplit[0] + ".log";

    }
    
    private String getTxtFileName() {
    	return txtFileName;
    }
    
    private String getTxtFileLocation() {
    	return txtFileLocation;
    }
    
    private String getPath() {
    	return path;
    }
    
    private String getCtrlFile() {
    	return ctrlFile;
    }
    
    private String getLogFile() {
    	return logFile;
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
    
}
