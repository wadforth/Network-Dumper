package networkTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class decryptionPassword {

	protected Shell shlEnterPassword;
	private Text enteredPassword;
	public static String userPassword;

	public static void passwordDialog() {
		try {
			decryptionPassword window = new decryptionPassword();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlEnterPassword.open();
		shlEnterPassword.layout();
		while (!shlEnterPassword.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shlEnterPassword = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN & SWT.SHELL_TRIM & (~SWT.RESIZE));
		shlEnterPassword.setSize(241, 118);
		shlEnterPassword.setText("Enter Password");

		enteredPassword = new Text(shlEnterPassword, SWT.BORDER | SWT.PASSWORD);
		enteredPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					userPassword = enteredPassword.getText();
					shlEnterPassword.close();

					try {
						dump.openDump();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		enteredPassword.setBounds(71, 8, 148, 26);

		Label lblPassword = new Label(shlEnterPassword, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 9, SWT.NORMAL));
		lblPassword.setBounds(10, 10, 55, 15);
		lblPassword.setText("Password");

		Label lblPasswordIsUsed = new Label(shlEnterPassword, SWT.NONE);
		lblPasswordIsUsed.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 9, SWT.NORMAL));
		lblPasswordIsUsed.setBounds(15, 54, 238, 15);
		lblPasswordIsUsed.setText("Password is used to decrypt dump.");
	}
}

