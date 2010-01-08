/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opencsta.asteriskcsta;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.opencsta.servicedescription.common.helpers.CSTA_Base;

/**
 *
 * @author demo
 */
public class AstCSTA_L7 extends CSTA_Base{
    protected static Logger alog = Logger.getLogger(AstCSTA_L7.class) ;
    private static Properties theProps ;
    private AsteriskCSTAServer asteriskcstaserver ;
    private AstCSTA_L5 layer5 ;
    private String ami_username ;
    private String ami_password ;
    private boolean username ;
    private boolean password ;

    @SuppressWarnings("static-access")
    public AstCSTA_L7(AsteriskCSTAServer _asteriskcstaserver,Properties _props){
        this.asteriskcstaserver = _asteriskcstaserver ;
        this.theProps = _props ;
        setUsername(false) ;
        setPassword(false) ;
        layer5 = new AstCSTA_L5(this,theProps) ;
    }


    public void FromBelow( StringBuffer curInStr ){
        if(curInStr.charAt(0) == 0x60){
            System.out.println("Received a CSTA Log On Request") ;
            handleCSTALogOnRequest(curInStr) ;
        }

    }

    private void handleCSTALogOnRequest(StringBuffer curInStr){
        while( !isPassword() ){
            if( curInStr.charAt(0) == 0x60 ){
                curInStr = DeleteChars(curInStr,2,"Removing Log On Flag chars") ;
            }
            else if( curInStr.charAt(0) == 0xa1 ){
                curInStr = DeleteChars(curInStr,9,"Removing first part of stuff") ;
            }
            else if( curInStr.charAt(0) == 0x8a ){
                curInStr = DeleteChars(curInStr,4,"Removing first part of stuff") ;
            }
            else if( curInStr.charAt(0) == 0xac ){
                curInStr = DeleteChars(curInStr,2,"Removing first part of stuff") ;
            }
            else if( curInStr.charAt(0) == 0xa2 ){
                curInStr = DeleteChars(curInStr,2,"Removing first part of stuff") ;
            }
            else if( curInStr.charAt(0) == 0xa0 ){
                curInStr = DeleteChars(curInStr,2,"Removing first part of stuff") ;
            }
            else if( curInStr.charAt(0) == 0x04 ){
                if( !isUsername() ){
                    int length = (int)curInStr.charAt(1) ;
                    ami_username = curInStr.substring(2,(length+2)) ;
                    setUsername(true) ;
                    curInStr = DeleteChars(curInStr,(length+2)) ;
                    System.out.println("AMI USERNAME: " + ami_username ) ;
                }else{
                    int length = (int)curInStr.charAt(1) ;
                    ami_password = curInStr.substring(2,(length+2)) ;
                    setPassword(true) ;
                    curInStr = DeleteChars(curInStr,(length+2)) ;
                    System.out.println("AMI PASSWORD: " + ami_password ) ;
                }

            }
        }
        asteriskcstaserver.connectAMI(ami_username,ami_password) ;
    }

    /**
     * @return the username
     */
    public boolean isUsername() {
        return username;
    }

    /**
     * @param username the username to set
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
     * @param password the password to set
     */
    public void setPassword(boolean password) {
        this.password = password;
    }

}
