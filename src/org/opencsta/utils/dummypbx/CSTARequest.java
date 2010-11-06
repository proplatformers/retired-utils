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

package org.opencsta.utils.dummypbx;

/**
 * 
 * @author chrismylonas
 */
public class CSTARequest implements Runnable {

	/**
	 * 
	 */
	protected char service_id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

	}

	/**
	 * @return the service_id
	 */
	public char getService_id() {
		return service_id;
	}

	/**
	 * @param service_id
	 *            the service_id to set
	 */
	public void setService_id(char service_id) {
		this.service_id = service_id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "";
	}

	/**
	 * @return
	 */
	public String toCSTAXML() {
		return "";
	}

	/**
	 * @return
	 */
	public StringBuffer toCSTAASN1() {
		return new StringBuffer();
	}
}
