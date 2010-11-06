/*
This file is part of Open CSTA.

    Open CSTA is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Open CSTA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Open CSTA.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.opencsta.utils.blackbox.network;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * @author chrismylonas
 * 
 */
public class ServerSide implements Runnable {

	/**
	 * 
	 */
	protected static Logger bblog = Logger
			.getLogger(org.opencsta.utils.blackbox.network.ServerSide.class);

	/**
	 * 
	 */
	private Properties properties;

	/**
	 * 
	 */
	private Socket serverSideConnectionSocket;

	/**
	 * 
	 */
	DataOutputStream out;

	/**
	 * 
	 */
	DataInputStream in;

	/**
	 * 
	 */
	private boolean runFlag;

	/**
	 * 
	 */
	private NetworkBlackBox parent;

	/**
	 * 
	 */
	private int line;

	/**
	 * 
	 */
	private StringBuffer chris;

	/**
	 * 
	 */
	private byte[] buf;

	/**
	 * 
	 */
	private String portNumber;

	/**
	 * 
	 */
	private String host;

	/**
	 * @param _parent
	 * @param _props
	 */
	public ServerSide(NetworkBlackBox _parent, Properties _props) {
		this.parent = _parent;
		this.properties = _props;
		setProperties();
		chris = new StringBuffer();
		buf = new byte[1024];
		setRunFlag(true);
	}

	/**
	 * 
	 */
	private void setProperties() {
		try {
			portNumber = properties.getProperty("SERVERSIDE_TARGET_PORTNUMBER");
			host = properties.getProperty("SERVERSIDE_TARGET_HOST");
			System.out.println("Serverside connecting to host:portnumber "
					+ host + ":" + portNumber);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SecurityException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.out.println("Connecting to Hipath");
		try {
			serverSideConnectionSocket = new Socket(host,
					Integer.parseInt(portNumber));
			out = new DataOutputStream(
					serverSideConnectionSocket.getOutputStream());
			in = new DataInputStream(
					serverSideConnectionSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: kq6py.eng");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
		System.out.println("Connection to Hipath complete");
		while (runFlag) {
			try {
				line = in.read(buf);
				System.out.println(line + " bytes received");
				buf2SBChris(line);
				// Send data back to parent
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
			parent.serverSideToClientSide(new String(chris));
		}
	}

	/**
	 * @return
	 */
	public boolean isRunFlag() {
		return runFlag;
	}

	/**
	 * @param runFlag
	 */
	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	/**
	 * 
	 */
	public void listenSocket() {

	}

	/**
	 * @param line
	 */
	public void sendData(int line) {
		char[] chbyte = { (char) line };
		try {
			out.writeBytes(new String(chbyte));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param str_rec
	 */
	public void sendData(String str_rec) {
		try {
			out.writeBytes(str_rec);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param length
	 */
	private void buf2SBChris(int length) {
		chris = new StringBuffer();
		for (int i = 0; i < length; i++) {

			if ((short) buf[i] < 0) {
				append2chris((int) buf[i] + 256);
			}

			else {
				Byte b = new Byte(buf[i]);
				append2chris((int) b.intValue());
			}
		}
		TestChris(chris);
	}

	/**
	 * @param thisByte
	 */
	private void append2chris(int thisByte) {
		chris.append((char) thisByte);
	}

	/**
	 * @param cm
	 */
	public void TestChris(StringBuffer cm) {
		System.out.print("Client <--- Server | R: ");
		for (int i = 0; i < cm.length(); i++) {
			System.out.print(Integer.toHexString((char) cm.charAt(i)) + " ");
		}
		System.out.println("");
	}
}
