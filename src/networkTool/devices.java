package networkTool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;

public class devices {

	static ArrayList<String> deviceArray = new ArrayList<String>();
	static List deviceList = new List(gui.shlNetworkTool, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

	public static void scanDevices(String subnet)
			throws UnknownHostException {

		gui.shlNetworkTool.setSize(566, 276);
		MessageDialog.openWarning(gui.shlNetworkTool, "Scanning Devices...",
				"Program may become unresponsive when scanning for devices on the subnet. Please do not click the program until it has finished scanning.\n\n Scan may take a couple of minutes.");
		deviceList.setBounds(10, 92, 530, 135);

		int timeout = 1000;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

		// Loops through subnet up until 256 to find devices on the network and adds to
		// GUI and Array - May take a couple of minutes to scan
		deviceList.add(
				"Device                     -                                  Local Address                     -                Time Added ");
		for (int i = 1; i < 256; i++) {
			String host = subnet + "." + i;

			try {
				if (InetAddress.getByName(host).isReachable(timeout)) {
					InetAddress localhost = InetAddress.getByName(host);

					deviceList.add(localhost.getHostName() + "                                 " + host
							+ "                                 " + formatter.format(date));
					deviceArray.add(localhost.getHostName() + "                                 " + host
							+ "                                 " + formatter.format(date));
				}
			} catch (Exception e1) {
				System.out.println(e1);
			}
		}
	}
}
