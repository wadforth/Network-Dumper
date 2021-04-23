package networkTool;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class gui {

	protected static Shell shlNetworkTool;
	private Text txtV;
	private Text txtDefaultsubnet;
	static String defaultSubnet = "192.168.1";


	public static void main(String[] args) {
		try {
			gui window = new gui();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlNetworkTool.open();
		shlNetworkTool.layout();
		while (!shlNetworkTool.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	void createContents() {
		shlNetworkTool = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN & SWT.SHELL_TRIM & (~SWT.RESIZE));
		shlNetworkTool.setImage(SWTResourceManager.getImage(gui.class, "/networkTool/logo.png"));
		shlNetworkTool.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shlNetworkTool.setSize(566, 137);
		shlNetworkTool.setText("Network Dumper");
		shlNetworkTool.setLayout(null);

		Button btnScanNetwork = new Button(shlNetworkTool, SWT.NONE);
		btnScanNetwork.setBounds(10, 61, 84, 27);
		btnScanNetwork.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ports.portList.removeAll();
				try {
					devices.scanDevices(defaultSubnet);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnScanNetwork.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		btnScanNetwork.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 9, SWT.NORMAL));
		btnScanNetwork.setToolTipText("Scan devices on subnet");
		btnScanNetwork.setText("Scan Devices");

		Button btnNewButton = new Button(shlNetworkTool, SWT.NONE);
		btnNewButton.setToolTipText("Scan open ports on the local network");
		btnNewButton.setBounds(100, 61, 114, 27);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					devices.deviceList.removeAll();
					ports.socketOverview();
				} catch (IOException | ExecutionException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		btnNewButton.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 9, SWT.NORMAL));
		btnNewButton.setText("Scan Open Ports");

		Button btnDumpNetwork = new Button(shlNetworkTool, SWT.NONE);
		btnDumpNetwork.setToolTipText("Dump network information based on scans with AES encryption");
		btnDumpNetwork.setBounds(359, 61, 97, 27);
		btnDumpNetwork.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				encryptionPassword.passwordDialog();
			}
		});
		btnDumpNetwork.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 9, SWT.NORMAL));
		btnDumpNetwork.setText("Dump Network");

		Label label = new Label(shlNetworkTool, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 47, 550, 2);

		txtV = new Text(shlNetworkTool, SWT.NONE);
		txtV.setBounds(265, 83, 39, 14);
		txtV.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtV.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 7, SWT.NORMAL));
		txtV.setText("beta v0.1");

		Button button = new Button(shlNetworkTool, SWT.NONE);
		button.setBounds(511, 0, 39, 14);
		button.setToolTipText("Need Help?");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(shlNetworkTool, "Help",
						"Scan Devices - Scans all the current devices based on the IP address set in the subnet.\n\nPort/Open Ports - Goes through each port and find which ones are open on your local network.\n\nDump Network - Saves network information to a text file that is encrypted using a password.\n\nOpen Dump - Opens any previously dumped text files which is decrypted with a password.\n\nSubnet: Do not change the subnet unless you know what your subnet is. Changing it to someone that isn't a valid address may break the scan and dump.");
			}
		});
		button.setText("?");

		Button btnOpenDump = new Button(shlNetworkTool, SWT.NONE);
		btnOpenDump.setToolTipText("Open dump based on secret key provided by user");
		btnOpenDump.setBounds(462, 61, 81, 27);
		btnOpenDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				decryptionPassword.passwordDialog();
			}
		});
		btnOpenDump.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 9, SWT.NORMAL));
		btnOpenDump.setText("Open Dump");

		Label lblNetworkDumper = new Label(shlNetworkTool, SWT.NONE);
		lblNetworkDumper.setBounds(10, 10, 158, 31);
		lblNetworkDumper.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 14, SWT.NORMAL));
		lblNetworkDumper.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNetworkDumper.setText("Network Dumper");
		lblNetworkDumper.setFont(SWTResourceManager.getFont("Microsoft YaHei UI Light", 14, SWT.NORMAL));
		lblNetworkDumper.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNetworkDumper.setText("Network Dumper");

		txtDefaultsubnet = new Text(shlNetworkTool, SWT.BORDER);
		txtDefaultsubnet.setBounds(374, 10, 131, 24);
		txtDefaultsubnet.setText(defaultSubnet);
		txtDefaultsubnet.setToolTipText("Type in new subnet ");
		txtDefaultsubnet.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13 && !txtDefaultsubnet.getText().equals("")) {
					defaultSubnet = txtDefaultsubnet.getText();
					Label lblSubNetChange = new Label(gui.shlNetworkTool, SWT.NONE);
					lblSubNetChange.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					lblSubNetChange.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					lblSubNetChange.setBounds(229, 0, 126, 15);
					lblSubNetChange.setText("Subnet Changed");

				}
			}
		});

		Label lblSubnet = new Label(shlNetworkTool, SWT.NONE);
		lblSubnet.setBounds(328, 15, 39, 14);
		lblSubnet.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblSubnet.setText("Subnet");

	}
}

