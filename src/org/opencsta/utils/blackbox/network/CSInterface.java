/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opencsta.utils.blackbox.network;

/**
 *
 * @author demo
 */
public interface CSInterface {
    public void clientSideToServerSide(String str_data) ;
    public void clientSideToServerSide(int char_data) ;
    public void clientHasConnected() ;
}
