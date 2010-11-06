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
import org.opencsta.servicedescription.common.helpers.CSTA_Base;

/**
 * 
 * @author chrismylonas
 */
public class AstCSTA_L7 extends CSTA_Base {

	/**
	 * 
	 */
	protected static Logger alog = Logger.getLogger(AstCSTA_L7.class);

	/**
	 * 
	 */
	private static Properties theProps;

	/**
	 * 
	 */
	private AsteriskCSTAServer asteriskcstaserver;

	/**
	 * 
	 */
	private AstCSTA_L5 layer5;

	/**
	 * 
	 */
	private String ami_username;

	/**
	 * 
	 */
	private String ami_password;

	/**
	 * 
	 */
	private boolean username;

	/**
	 * 
	 */
	private boolean password;

	/**
	 * @param _asteriskcstaserver
	 * @param _props
	 */
	@SuppressWarnings("static-access")
	public AstCSTA_L7(AsteriskCSTAServer _asteriskcstaserver, Properties _props) {
		this.asteriskcstaserver = _asteriskcstaserver;
		this.theProps = _props;
		setUsername(false);
		setPassword(false);
		layer5 = new AstCSTA_L5(this, theProps);
	}

	/**
	 * @param curInStr
	 */
	public void FromBelow(StringBuffer curInStr) {
		if (curInStr.charAt(0) == 0x60) {
			System.out.println("Received a CSTA Log On Request");
			handleCSTALogOnRequest(curInStr);
		}

	}

	/**
	 * @param curInStr
	 */
	private void handleCSTALogOnRequest(StringBuffer curInStr) {
		while (!isPassword()) {
			if (curInStr.charAt(0) == 0x60) {
				curInStr = DeleteChars(curInStr, 2,
						"Removing Log On Flag chars");
			} else if (curInStr.charAt(0) == 0xa1) {
				curInStr = DeleteChars(curInStr, 9,
						"Removing first part of stuff");
			} else if (curInStr.charAt(0) == 0x8a) {
				curInStr = DeleteChars(curInStr, 4,
						"Removing first part of stuff");
			} else if (curInStr.charAt(0) == 0xac) {
				curInStr = DeleteChars(curInStr, 2,
						"Removing first part of stuff");
			} else if (curInStr.charAt(0) == 0xa2) {
				curInStr = DeleteChars(curInStr, 2,
						"Removing first part of stuff");
			} else if (curInStr.charAt(0) == 0xa0) {
				curInStr = DeleteChars(curInStr, 2,
						"Removing first part of stuff");
			} else if (curInStr.charAt(0) == 0x04) {
				if (!isUsername()) {
					int length = (int) curInStr.charAt(1);
					ami_username = curInStr.substring(2, (length + 2));
					setUsername(true);
					curInStr = DeleteChars(curInStr, (length + 2));
					System.out.println("AMI USERNAME: " + ami_username);
				} else {
					int length = (int) curInStr.charAt(1);
					ami_password = curInStr.substring(2, (length + 2));
					setPassword(true);
					curInStr = DeleteChars(curInStr, (length + 2));
					System.out.println("AMI PASSWORD: " + ami_password);
				}

			}
		}
		asteriskcstaserver.connectAMI(ami_username, ami_password);
	}

	/**
	 * @return the username
	 */
	public boolean isUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(boolean username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public boolean isPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(boolean password) {
		this.password = password;
	}

}
