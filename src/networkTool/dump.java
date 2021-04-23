package networkTool;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

public class dump {

	static Cipher cipher;
	static String secretKey;
	private static byte[] key;
	private static Scanner file;
	int i = 0;
	static int verification = 0;
	static Label lblDecrypted = new Label(gui.shlNetworkTool, SWT.NONE);
	static Label lblWrong = new Label(gui.shlNetworkTool, SWT.NONE);

	static ArrayList<String> readArray = new ArrayList<String>();

	public static void dumpNetwork(String secretKey) throws Exception {
		// Converts user password into the correct amount of bytes for AES encryption
		key = secretKey.getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKey originalKey = new SecretKeySpec(key, "AES");

		cipher = Cipher.getInstance("AES");

		// Checks if both of the scans have been made before proceeding with the network
		// dump
		if (devices.deviceArray.isEmpty() || ports.portArray.isEmpty()) {
			MessageDialog.openError(gui.shlNetworkTool, "Missing Scan",
					"Please scan devices and open ports before dumping network.");
		} else if (!devices.deviceArray.isEmpty() && !ports.portArray.isEmpty()) {
			MessageDialog.openInformation(gui.shlNetworkTool, "Devices and Ports Dumped",
					"Devices and ports have been scanned. Dumping information to text file at "
							+ System.getProperty("user.home")
							+ "\\Desktop\\NetworkDump.txt\" "
							+ "\n\nFile is encrypted, to view dump select 'Open Dump' on Network Dumper and enter your key to decrypt and open the file.");

			// Locates users Desktop file path
			FileWriter writer = new FileWriter(
					System.getProperty("user.home") + "\\Desktop\\NetworkDump.txt");

			// Writes to a text file encrypted
			String encryptedTitle = encrypt(
					"Scan initiated on " + gui.defaultSubnet
							+ " subnet\nDevice                     -                              Local Address                 -                Time Added ",
					originalKey);
			writer.write(encryptedTitle + System.lineSeparator() + System.lineSeparator());
			for (String loop : devices.deviceArray) {
				String encryptedTest = encrypt(loop, originalKey);
				writer.write(encryptedTest + System.lineSeparator());
			}

			String encryptedTitle2 = encrypt(
					"Foreign Address  -  Local Address   -  Port        -      State           -       Time Added ",
					originalKey);

			writer.write(System.lineSeparator() + encryptedTitle2 + System.lineSeparator() + System.lineSeparator());

			for (String loop : ports.portArray) {
				String encryptedText = encrypt(loop, originalKey);
				writer.write(encryptedText + System.lineSeparator());
			}
			writer.close();
		}
	}

	// Encryption using users password
	public static String encrypt(String filePath, Key originalKey) throws Exception {
		byte[] plainTextByte = filePath.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, originalKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = Base64.getEncoder().encodeToString(encryptedByte);
		return encryptedText;
	}

	// Opens network dump using previous users password (acting as secret key)
	public static void openDump() throws Exception {
		readArray();
		String secretKey = decryptionPassword.userPassword;
		file = new Scanner(new FileReader(System.getProperty("user.home") + "\\Desktop\\NetworkDump.txt"));
		byte[] key = secretKey.getBytes("UTF-8");

		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKey originalKey = new SecretKeySpec(key, "AES");

		cipher = Cipher.getInstance("AES");

		FileWriter writer = new FileWriter(System.getProperty("user.home") + "\\Desktop\\NetworkDump.txt");

		// Decrypting text line by line
		for (String loop : readArray) {
			String decryptedText = decrypt(loop,
					originalKey);
			if (verification == 0) {

				writer.write(decryptedText + System.lineSeparator());
			}
			else if (verification == 1) {
				writer.write(loop + System.lineSeparator());
			}
		}
		if (verification == 0) {
			MessageDialog.openInformation(gui.shlNetworkTool, "Decrypted", "Dump has been decrypted.");
		} else if (verification == 1) {
			MessageDialog.openError(gui.shlNetworkTool, "Incorrect", "Password Wrong - Try Again!");
		}

		readArray.clear();
		writer.close();

		if (Desktop.isDesktopSupported() && verification == 0) {
			try {
				File myFile = new File(System.getProperty("user.home") + "\\Desktop\\NetworkDump.txt");
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
			}
		}
		verification = 0;
	}

	public static void readArray() throws FileNotFoundException {

		file = new Scanner(new FileReader(System.getProperty("user.home") + "\\Desktop\\NetworkDump.txt"));

		while (file.hasNext()) {
			String line = file.nextLine();
			readArray.add(line);
		}
		return;
	}

	// Decryption based on users password
	public static String decrypt(String encryptedText, Key originalKey) throws Exception {
		try {

			byte[] encryptedTextByte = Base64.getDecoder().decode(encryptedText);
			cipher.init(Cipher.DECRYPT_MODE, originalKey);
			byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
			String decryptedText = new String(decryptedByte);
			return decryptedText;
		} catch (Exception e) {
			verification = 1;
		}
		return null;
	}
}
