package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

public class GUIApplication {

	protected Shell shlIntegrationAssignment;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUIApplication window = new GUIApplication();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlIntegrationAssignment.open();
		shlIntegrationAssignment.layout();
		while (!shlIntegrationAssignment.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlIntegrationAssignment = new Shell();
		shlIntegrationAssignment.setBackground(SWTResourceManager.getColor(75, 0, 130));
		shlIntegrationAssignment.setSize(450, 300);
		shlIntegrationAssignment.setText("Integration Assignment");
		shlIntegrationAssignment.setLayout(null);
		
		Label stepOneLabel = new Label(shlIntegrationAssignment, SWT.NONE);
		stepOneLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		stepOneLabel.setFont(SWTResourceManager.getFont("Baskerville Old Face", 12, SWT.BOLD));
		stepOneLabel.setBackground(SWTResourceManager.getColor(75, 0, 130));
		stepOneLabel.setBounds(10, 64, 268, 30);
		stepOneLabel.setText("Step 1: Database Login and Check");
		
		Button btnCheck = new Button(shlIntegrationAssignment, SWT.NONE);
		btnCheck.setBounds(301, 64, 55, 25);
		btnCheck.setText("Check");
		
		Label stepTwoLabel = new Label(shlIntegrationAssignment, SWT.NONE);
		stepTwoLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		stepTwoLabel.setFont(SWTResourceManager.getFont("Baskerville Old Face", 12, SWT.BOLD));
		stepTwoLabel.setBackground(SWTResourceManager.getColor(75, 0, 130));
		stepTwoLabel.setBounds(10, 110, 268, 20);
		stepTwoLabel.setText("Step 2: Process Delimited Text File");
		
		Button btnProcess = new Button(shlIntegrationAssignment, SWT.NONE);
		btnProcess.setBounds(301, 110, 55, 25);
		btnProcess.setText("Process");
		
		Label stepThreeLabel = new Label(shlIntegrationAssignment, SWT.NONE);
		stepThreeLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		stepThreeLabel.setFont(SWTResourceManager.getFont("Baskerville Old Face", 12, SWT.BOLD));
		stepThreeLabel.setBackground(SWTResourceManager.getColor(75, 0, 130));
		stepThreeLabel.setBounds(10, 149, 268, 25);
		stepThreeLabel.setText("Step 3: Perform Month End");
		
		Button btnPerform = new Button(shlIntegrationAssignment, SWT.NONE);
		btnPerform.setBounds(301, 149, 55, 25);
		btnPerform.setText("Perform");
		
		Label step4Label = new Label(shlIntegrationAssignment, SWT.NONE);
		step4Label.setForeground(SWTResourceManager.getColor(255, 255, 255));
		step4Label.setFont(SWTResourceManager.getFont("Baskerville Old Face", 12, SWT.BOLD));
		step4Label.setBackground(SWTResourceManager.getColor(75, 0, 130));
		step4Label.setBounds(10, 191, 268, 25);
		step4Label.setText("Step 4: Export Data");
		
		Button btnExport = new Button(shlIntegrationAssignment, SWT.NONE);
		btnExport.setBounds(301, 191, 55, 25);
		btnExport.setText("Export");
		
		Button btnClose = new Button(shlIntegrationAssignment, SWT.BORDER);
		btnClose.setBounds(383, 226, 41, 25);
		btnClose.setText("Close");
		
		Label titleLabel = new Label(shlIntegrationAssignment, SWT.NONE);
		titleLabel.setFont(SWTResourceManager.getFont("Baskerville Old Face", 16, SWT.BOLD));
		titleLabel.setBackground(SWTResourceManager.getColor(75, 0, 130));
		titleLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		titleLabel.setBounds(125, 10, 180, 30);
		titleLabel.setText("WKIS Accounting");

	}
}
