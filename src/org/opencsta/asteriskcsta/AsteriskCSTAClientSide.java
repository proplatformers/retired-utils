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

package org.opencsta.asteriskcsta;

import org.opencsta.utils.blackbox.network.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * @author chrismylonas
 * 
 */
public class AsteriskCSTAClientSide implements Runnable {

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
	private CSInterface parent;

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
	private boolean complete;

	/**
	 * @param _parent
	 * @param _props
	 */
	public AsteriskCSTAClientSide(CSInterface _parent, Properties _props) {
		this.parent = _parent;
		this.properties = _props;
		setProperties();
		chris = new StringBuffer();
		buf = new byte[1024];
		setRunFlag(true);
		System.out.println("Client side initialising");
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
		System.out.println("Starting clientside");
		try {
			clientSideConnectionSocket = new ServerSocket(
					Integer.parseInt(portNumber));
		} catch (IOException e) {
			System.out.println("Could not listen on port " + portNumber);
			System.exit(-1);
		} catch (NullPointerException e) {
			System.out.println(this.getClass().getName()
					+ " Null Pointer Exception");
		}

		try {
			client = clientSideConnectionSocket.accept();
			System.out.println("Accepted a client connection");
			parent.clientHasConnected();
		} catch (IOException e) {
			System.out.println("Accept failed: " + portNumber);
			System.exit(-1);
		} catch (NullPointerException e) {
			System.out.println(this.getClass().getName()
					+ " Null Pointer Exception");
		}

		try {
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			System.out.println("Accept failed: " + portNumber);
			System.exit(-1);
		} catch (NullPointerException e) {
			System.out.println(this.getClass().getName()
					+ " Null Pointer Exception");
		}

		while (runFlag) {
			try {
				line = in.read(buf);
				System.out.println(line + " bytes received");
				buf2SBChris(line);
				// line = in.read();
				// chris.append((char)line) ;
				// //Send data to server side
				// System.out.println("Sending: ") ;
				// System.out.print( Integer.toHexString(line) + " ") ;
				// parent.clientSideToServerSide(line);
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			} catch (NullPointerException e) {
				System.out.println(this.getClass().getName()
						+ " Null Pointer Exception");
			}
			if (isComplete()) {
				setComplete(false);
				parent.clientSideToServerSide(new String(chris));
				chris = new StringBuffer();
			}
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
			System.out.println(this.getClass().getName()
					+ " Null Pointer Exception");
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
		System.out.println("");
	}

	/**
	 * @param length
	 */
	private void buf2SBChris(int length) {
		// chris = new StringBuffer();
		for (int i = 0; i < length; i++) {

			if ((short) buf[i] < 0) {
				append2chris((int) buf[i] + 256);
			}

			else {
				Byte b = new Byte(buf[i]);
				append2chris((int) b.intValue());
			}
		}
		// TestChris(chris) ;
		checkBuffer();
	}

	/**
	 * @param thisByte
	 */
	private void append2chris(int thisByte) {
		chris.append((char) thisByte);
	}

	/**
	 * 
	 */
	private void checkBuffer() {
		if (chris.length() > 5) {
			TestChris(chris);
			if (isBufferResetableAndEven(chris)) {
				System.out.println(this.getClass().getName() + " -> "
						+ "Incoming Buffer is even and being reset");
				setComplete(true);
			} else if (isBufferStillReading(chris)) {
				System.out.println(this.getClass().getName() + " -> "
						+ "Buffer is still reading");
				// System.out.println("Incoming Buffer is still reading");
				// System.out.println("The length of chris at the moment is: " +
				// Integer.toString(chris.length() ) );
				// System.out.println("The length of chris full message should be (hex): "
				// + Integer.toHexString( (int)chris.charAt(2)) ) ;
				// System.out.println("The length of chris full message should be (dec): "
				// + Integer.toString( chris.charAt(2)) ) ;
			} else if (isBufferHoldingMoreThanOneMessage(chris)) {
				System.out.println(this.getClass().getName() + " -> "
						+ "Buffer is holding more than one message");
				// System.out.println("Incoming Buffer has over read more than one message")
				// ;
				// System.out.println("The length of chris at the moment is: " +
				// Integer.toString(chris.length() ) );
				// System.out.println("The length of chris full message should be (hex): "
				// + Integer.toHexString( (int)chris.charAt(2)) ) ;
				// System.out.println("The length of chris full message should be (dec): "
				// + Integer.toString( chris.charAt(2)) ) ;
				StringBuffer tmp = new StringBuffer(chris.substring(0,
						(((int) chris.charAt(2)) + 3)));

				chris = new StringBuffer(
						chris.substring(((int) chris.charAt(2) + 3)));
				checkBuffer();
			}
		}
	}

	/**
	 * @param sb
	 * @return
	 */
	private boolean isBufferResetableAndEven(StringBuffer sb) {
		if (chris.length() == ((int) chris.charAt(2) + 3)) {
			return true;
		}
		return false;
	}

	/**
	 * @param sb
	 * @return
	 */
	private boolean isBufferHoldingMoreThanOneMessage(StringBuffer sb) {
		if (chris.length() > ((int) chris.charAt(2) + 3)) {
			return true;
		}
		return false;
	}

	/**
	 * @param sb
	 * @return
	 */
	private boolean isBufferStillReading(StringBuffer sb) {
		String bufLength = Integer.toString(chris.length());
		String intendedLength = Integer.toHexString((int) chris.charAt(2));
		System.out.println(this.getClass().getName() + " -> "
				+ "Buffer status -> Intended Length: " + intendedLength
				+ " | Current Length: " + bufLength);
		if (chris.length() < (((int) chris.charAt(2)) + 3)) {
			System.out.println(this.getClass().getName() + " -> "
					+ "Buffer is still reading");
			return true;
		}
		System.out.println(this.getClass().getName() + " -> "
				+ "Buffer is complete, now ready for clearing");
		return false;
	}

	/**
	 * @return the complete
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * @param complete
	 *            the complete to set
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
