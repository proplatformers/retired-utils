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
import org.opencsta.servicetools.asterisk.AsteriskController;

/**
 *
 * @author demo
 */
public class AsteriskCSTAServer {
    protected static Logger alog = Logger.getLogger(AsteriskCSTAServer.class) ;
    private static Properties theProps ;
    private AstCSTA_L7 layer7 ;
    private AsteriskController asterisk ;
    private boolean alive = false ;

    @SuppressWarnings("static-access")
    public AsteriskCSTAServer(Properties _props){
        this.theProps = _props ;
        layer7 = new AstCSTA_L7(this,theProps) ;
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
}
