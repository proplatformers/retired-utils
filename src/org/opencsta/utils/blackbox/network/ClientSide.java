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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * @author chrismylonas
 * 
 */
public class ClientSide implements Runnable {

	/**
	 * 
	 */
	protected static Logger bblog = Logger
			.getLogger(org.opencsta.utils.blackbox.network.ClientSide.class);

	/**
	 * 
	 */
	private Properties properties;

	/**
	 * 
	 */
	private ServerSocket clientSideConnectionSocket;

	/**
	 * 
	 */
	private Socket client;

	/**
	 * 
	 */
	private DataInputStream in;

	/**
	 * 
	 */
	private DataOutputStream out;

	/**
	 * 
	 */
	private int line;

	/**
	 * 
	 */
	private boolean runFlag = false;

	/**
	 * 
	 */
	private NetworkBlackBox parent;

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
	 * @param _parent
	 * @param _props
	 */
	public ClientSide(NetworkBlackBox _parent, Properties _props) {
		this.parent = _parent;
		this.properties = _props;
		setProperties();
		chris = new StringBuffer();
		buf = new byte[1024];
		setRunFlag(true);
		bblog.info("Client side initialising");
	}

	/**
	 * 
	 */
	private void setProperties() {
		try {
			portNumber = properties
					.getProperty("CLIENTSIDE_LISTENER_PORTNUMBER");
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
		bblog.info("Starting clientside");
		try {
			clientSideConnectionSocket = new ServerSocket(
					Integer.parseInt(portNumber));
		} catch (IOException e) {
			bblog.info("Could not listen on port " + portNumber);
			System.exit(-1);
		} catch (NullPointerException e) {
			bblog.info(this.getClass().getName() + " Null Pointer Exception");
		}

		try {
			client = clientSideConnectionSocket.accept();
			bblog.info("Accepted a client connection");
			parent.clientHasConnected();
		} catch (IOException e) {
			bblog.info("Accept failed: " + portNumber);
			System.exit(-1);
		} catch (NullPointerException e) {
			bblog.info(this.getClass().getName() + " Null Pointer Exception");
		}

		try {
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			bblog.info("Accept failed: " + portNumber);
			System.exit(-1);
		} catch (NullPointerException e) {
			bblog.info(this.getClass().getName() + " Null Pointer Exception");
		}

		while (runFlag) {
			try {
				line = in.read(buf);
				bblog.info(line + " bytes received");
				buf2SBChris(line);
				// line = in.read();
				// chris.append((char)line) ;
				// //Send data to server side
				// bblog.info("Sending: ") ;
				// System.out.print( Integer.toHexString(line) + " ") ;
				// parent.clientSideToServerSide(line);
			} catch (IOException e) {
				bblog.info("Read failed");
				System.exit(-1);
			} catch (NullPointerException e) {
				bblog.info(this.getClass().getName()
						+ " Null Pointer Exception");
			}
			parent.clientSideToServerSide(new String(chris));
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
	 * @param line
	 */
	public void receiveData(int line) {
		char[] chbyte = { (char) line };
		try {
			out.writeBytes(new String(chbyte));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			bblog.info(this.getClass().getName() + " Null Pointer Exception");
		}
	}

	/**
	 * @param str_rec
	 */
	public void receiveData(String str_rec) {
		try {
			out.writeBytes(str_rec);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param cm
	 */
	public void TestChris(StringBuffer cm) {
		System.out.print("Client ---> Server | S: ");
		for (int i = 0; i < cm.length(); i++) {
			System.out.print(Integer.toHexString((char) cm.charAt(i)) + " ");
		}
		bblog.info("");
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
}
