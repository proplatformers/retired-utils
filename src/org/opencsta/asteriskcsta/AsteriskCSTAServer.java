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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.ManagerEvent;
import org.opencsta.servicetools.asterisk.AsteriskController;
import org.opencsta.servicetools.asterisk.AsteriskInterest;

/**
 * 
 * @author chrismylonas
 */
public class AsteriskCSTAServer implements AsteriskInterest {

	/**
	 * 
	 */
	protected static Logger alog = Logger.getLogger(AsteriskCSTAServer.class);

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
	private AsteriskController asterisk;

	/**
	 * 
	 */
	private String ami_host_address;

	/**
	 * 
	 */
	private String ami_host_port;

	/**
	 * 
	 */
	private boolean alive = false;

	/**
	 * @param _props
	 */
	@SuppressWarnings("static-access")
	public AsteriskCSTAServer(Properties _props) {
		this.theProps = _props;
		layer7 = new AstCSTA_L7(this, theProps);
		setProperties();
	}

	/**
     * 
     */
	private void setProperties() {
		try {
			ami_host_address = theProps.getProperty("AMI_HOST_ADDRESS");
			ami_host_port = theProps.getProperty("AMI_HOST_PORT");
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

	/**
     * 
     */
	public void run() {
		setAlive(true);
		while (isAlive()) {
			try {
				synchronized (this) {
					wait(2000);
				}
			} catch (InterruptedException e) {

			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties someProps = loadPropertiesFromFile();
		AsteriskCSTAServer asteriskcstaserver = new AsteriskCSTAServer(
				someProps);
		asteriskcstaserver.run();
	}

	/**
	 * @return
	 */
	private static Properties loadPropertiesFromFile() {
		return loadPropertiesFromFile("asteriskcstaserver.conf");
	}

	/**
	 * @param filename
	 * @return
	 */
	private static Properties loadPropertiesFromFile(String filename) {
		FileInputStream is;
		try {
			System.out.println("Trying to load properties from:  "
					+ System.getProperty("user.dir") + "/" + filename);
			is = new FileInputStream(
					(System.getProperty("user.dir") + "/" + filename));
			Properties props = new Properties();
			props.load(is);
			return props;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive
	 *            the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * @param amiusername
	 * @param amipassword
	 */
	public void connectAMI(String amiusername, String amipassword) {
		asterisk = new AsteriskController(this, ami_host_address, amiusername,
				amipassword);
	}

	/**
	 * @return the ami_host
	 */
	public String getAmi_host() {
		return ami_host_address;
	}

	/**
	 * @param ami_host
	 *            the ami_host to set
	 */
	public void setAmi_host(String ami_host) {
		this.ami_host_address = ami_host;
	}

	/**
	 * @param me
	 */
	public void AsteriskManagerEventReceived(ManagerEvent me) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @return the ami_host_port
	 */
	public String getAmi_host_port() {
		return ami_host_port;
	}

	/**
	 * @param ami_host_port
	 *            the ami_host_port to set
	 */
	public void setAmi_host_port(String ami_host_port) {
		this.ami_host_port = ami_host_port;
	}
}
