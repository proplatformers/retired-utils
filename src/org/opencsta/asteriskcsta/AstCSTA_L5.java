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

import java.util.Properties;
import org.apache.log4j.Logger;
import org.opencsta.utils.blackbox.network.CSInterface;
import org.opencsta.utils.blackbox.network.ClientSide;

/**
 * 
 * @author chrismylonas
 */
/**
 * @author chrismylonas
 * 
 */
public class AstCSTA_L5 implements CSInterface {

	// protected static Logger alog = Logger.getLogger(AstCSTA_L5.class) ;

	/**
	 * 
	 */
	private static Properties theProps;

	/**
	 * 
	 */
	private AstCSTA_L7 layer7;

	/**
	 * 
	 */
	private int listenerPort;

	/**
	 * 
	 */
	private AsteriskCSTAClientSide clientSide;

	/**
	 * 
	 */
	private static Thread clientSideThread;

	/**
	 * 
	 */
	private byte[] buffer = new byte[256];

	/**
	 * 
	 */
	private boolean complete;

	/**
	 * @param _layer7
	 * @param _props
	 */
	public AstCSTA_L5(AstCSTA_L7 _layer7, Properties _props) {
		this.layer7 = _layer7;
		this.theProps = _props;
		Init();
	}

	/**
	 * 
	 */
	private void Init() {
		clientSide = new AsteriskCSTAClientSide(this, theProps);
		clientSideThread = new Thread(clientSide, "Client Side Thread");
		clientSideThread.run();
	}

	/**
	 * @param sb
	 * @param s_or_r
	 */
	public void WriteToLog(StringBuffer sb, char s_or_r) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opencsta.utils.blackbox.network.CSInterface#clientSideToServerSide
	 * (java.lang.String)
	 */
	public void clientSideToServerSide(String str_data) {
		System.out.println("The passed up string at Layer 5 has a length of: "
				+ str_data.length());
		StringBuffer sb = Strip(new StringBuffer(str_data));
		layer7.FromBelow(sb);
		throw new UnsupportedOperationException("STRING Not supported yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opencsta.utils.blackbox.network.CSInterface#clientSideToServerSide
	 * (int)
	 */
	public void clientSideToServerSide(int char_data) {
		throw new UnsupportedOperationException("CHAR Not supported yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencsta.utils.blackbox.network.CSInterface#clientHasConnected()
	 */
	public void clientHasConnected() {

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

	// Strip layer 5 data.
	/**
	 * @param curInStr
	 * @return
	 */
	private StringBuffer Strip(StringBuffer curInStr) {
		/*
		 * Check to see the length if it is 0x80, 0x81 or 0x82 and take the
		 * required chars away
		 */
		if (curInStr.charAt(1) == 0x80 || curInStr.charAt(1) == 0x81)
			curInStr = curInStr.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0);
		else if (curInStr.charAt(1) == 0x82)/*
											 * char for length must be 0x82 ; so
											 * take away the required numbers of
											 * bytes
											 */
			curInStr = curInStr.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0)
					.deleteCharAt(0);
		else if (curInStr.charAt(1) == 0x83)/*
											 * does 0x83 exist even?? delete
											 * required number if it does anyway
											 */
			curInStr = curInStr.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0)
					.deleteCharAt(0).deleteCharAt(0);
		else if (curInStr.charAt(1) == 0x84)/*
											 * delete 4 characters worth of
											 * length...
											 */
			curInStr = curInStr.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0)
					.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0);
		return curInStr;
	}

}
