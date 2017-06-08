package com.github.torchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TorChecker {

	private final static String EXIT_NODE_URL = "https://check.torproject.org/exit-addresses";
	private final static String EXIT_ADDRESS = "ExitAddress";
	private final static String SPACE = " ";
	
	private final static String MESSAGE_A_PROBLEM_OCCURED = "A problem occured while updating the addresses: ";
	private final static String MESSAGE_NO_NEW_ADDRESSES = "No new addresses was added, current list may be out of date";
	private final static String MESSAGE_NO_ADDRESSES_EMPTY_LIST = "No addresses were found, current list is empty";
	
	private static HashMap<String, ?> exitAddresses = new HashMap<>();
	
	public static void updateAddresses() throws TorCheckerException {
		HashMap<String, ?> newAddresses = new HashMap<>();
		try {
			addAddresses(newAddresses);
		} catch (Throwable t) {
			throw new TorCheckerException(MESSAGE_A_PROBLEM_OCCURED + t.getMessage(), t);
		}
		finalize(newAddresses);
	}
	
	private static void addAddresses(HashMap<String, ?> addresses) throws IOException {
		InputStream is = null;
		BufferedReader br;
		URL url = new URL(EXIT_NODE_URL);
		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		
		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith(EXIT_ADDRESS)) {
				addresses.put(getIpAddress(line), null);
			}
		}
	}
	
	private static void finalize(HashMap<String, ?> newAddresses) throws TorCheckerException {
		if (!newAddresses.isEmpty()) {
			exitAddresses.clear();
			exitAddresses = newAddresses;
		} else {
			String message = exitAddresses.isEmpty() ? MESSAGE_NO_ADDRESSES_EMPTY_LIST : MESSAGE_NO_NEW_ADDRESSES;
			throw new TorCheckerException(message);
		}
		
	}
	
	public static List<String> getExitAddressesAsList() {
		return new ArrayList<>(exitAddresses.keySet());
	}
	
	public static int getNumberOfAddresses() {
		return exitAddresses.size();
	}
	
	public static boolean isExitNode(String ip)  {
		if (ip == null || ip.isEmpty()) {
			return false;
		}
		return exitAddresses.containsKey(ip);
	}
	
	static String getIpAddress(String line) {
		String[] columns = line.split(SPACE);
		return columns[1]; // This is where the address will be
	}
	
	static void closeStream(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException ignore) {
		}
	}
	
}
