/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author demo
 */
public class AsteriskCSTAServer implements AsteriskInterest{
    protected static Logger alog = Logger.getLogger(AsteriskCSTAServer.class) ;
    private static Properties theProps ;
    private AstCSTA_L7 layer7 ;
    private AsteriskController asterisk ;
    private String ami_host_address ;
    private String ami_host_port ;
    private boolean alive = false ;

    @SuppressWarnings("static-access")
    public AsteriskCSTAServer(Properties _props){
        this.theProps = _props ;
        layer7 = new AstCSTA_L7(this,theProps) ;
        setProperties() ;
    }

    private void setProperties(){
            try{
                ami_host_address = theProps.getProperty("AMI_HOST_ADDRESS") ;
                ami_host_port = theProps.getProperty("AMI_HOST_PORT") ;
            }catch(NullPointerException e){
                e.printStackTrace();
                System.exit(1) ;
            }catch(SecurityException e){
                e.printStackTrace() ;
                System.exit(1) ;
            }catch(IllegalArgumentException e){
                e.printStackTrace();
                System.exit(1) ;
            }
    }

    public void run(){
        setAlive(true) ;
        while(isAlive()){
            try{
                synchronized(this){
                    wait(2000) ;
                }
            }catch(InterruptedException e){

            }catch(NullPointerException e2){
            	e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        Properties someProps = loadPropertiesFromFile() ;
        AsteriskCSTAServer asteriskcstaserver = new AsteriskCSTAServer(someProps) ;
        asteriskcstaserver.run() ;
    }

    private static Properties loadPropertiesFromFile(){
        return loadPropertiesFromFile("asteriskcstaserver.conf") ;
    }

    private static Properties loadPropertiesFromFile(String filename){
        FileInputStream is ;
        try {
            System.out.println("Trying to load properties from:  " + System.getProperty("user.dir") + "/" + filename) ;
            is = new FileInputStream( (System.getProperty("user.dir") + "/"+filename) );
            Properties props = new Properties() ;
            props.load(is) ;
            return props ;
        }catch (FileNotFoundException ex) {
            ex.printStackTrace() ;
        } catch (IOException ex) {
            ex.printStackTrace() ;
        }
        return null ;
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void connectAMI(String amiusername, String amipassword){
        asterisk = new AsteriskController(this,ami_host_address,amiusername,amipassword) ;
    }

    /**
     * @return the ami_host
     */
    public String getAmi_host() {
        return ami_host_address;
    }

    /**
     * @param ami_host the ami_host to set
     */
    public void setAmi_host(String ami_host) {
        this.ami_host_address = ami_host;
    }

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
     * @param ami_host_port the ami_host_port to set
     */
    public void setAmi_host_port(String ami_host_port) {
        this.ami_host_port = ami_host_port;
    }
}
