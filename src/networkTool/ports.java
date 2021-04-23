package networkTool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;

public class ports {

	static ArrayList<String> portArray = new ArrayList<String>();
	static ArrayList<String> closedPorts = new ArrayList<String>();
	static List portList = new List(gui.shlNetworkTool, SWT.BORDER | SWT.V_SCROLL);

	public static void socketOverview(final String... args)
			throws IOException, ExecutionException, InterruptedException {

		gui.shlNetworkTool.setSize(566, 276);
		MessageDialog.openWarning(gui.shlNetworkTool, "Scanning Ports...",
				"Program may become unresponsive when scanning open ports. Please do not click the program until it has finished scanning.\n\n Scan may take a couple of minutes.");
		portList.setBounds(10, 92, 530, 135);

		// Executor service is used to predetermine the amount of CPU threads to use to
		// search for ports - the high the threads the faster it searches. For the sake
		// of other CPU's lowering the thread count will be slower but will stop high
		// CPU usage
		final ExecutorService es = Executors.newFixedThreadPool(300);
		InetAddress localhost = InetAddress.getLocalHost();
		final int timeout = 200;
		final ArrayList<Future<ScanResult>> futures = new ArrayList<>();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

		// Loops through ports to find which ones are open, uses ExecutorService to
		// speed things up and then adds to GUI and Array
		for (int port = 1; port <= 65535; port++) {
			futures.add(portIsOpen(es, localhost, port, timeout));
		}
		es.awaitTermination(200L, TimeUnit.MILLISECONDS);
		portList.add(
				"Foreign Address   -   Local Address   -   Port        -       State            -        Time Added ");
		for (final Future<ScanResult> f : futures) {
			if (f.get().isOpen()) {
				portList.add(localhost + "        " + f.get().getPort() + "         TCP/IP LISTENING       "
						+ formatter.format(date));
				portArray.add(localhost + "        " + f.get().getPort() + "         TCP/IP LISTENING       "
						+ formatter.format(date));
			}
		}
	}

	// Searchs for open sockets
	public static Future<ScanResult> portIsOpen(final ExecutorService es, final InetAddress ip, final int port,
			final int timeout) {
		return es.submit(new Callable<ScanResult>() {
			@Override
			public ScanResult call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					return new ScanResult(port, true);
				} catch (Exception ex) {
					return new ScanResult(port, false);
				}
			}
		});
	}

	public static class ScanResult {
		private int port;
		private boolean isOpen;

		public ScanResult(int port, boolean isOpen) {
			super();
			this.port = port;
			this.isOpen = isOpen;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public boolean isOpen() {
			return isOpen;
		}

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}
	}
}
