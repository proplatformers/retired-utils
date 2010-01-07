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


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
//import au.com.mrvoip.oscsta.server.CSTAServer;

public class NetworkBlackBox implements CSInterface{
        private Properties properties ;
        private ClientSide clientSide ;
        private ServerSide serverSide ;
        private static Thread clientSideThread ;
        private static Thread serverSideThread ;
        protected static Logger bblog = Logger.getLogger(org.opencsta.utils.blackbox.network.NetworkBlackBox.class) ;
        public static void main(String[] args) {

                NetworkBlackBox netbb = new NetworkBlackBox() ;
        }
        
    private static Properties loadPropertiesFromFile(){
        return loadPropertiesFromFile("networkblackbox.conf") ;
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
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace() ;
            System.exit(1) ;
        }
        return null ;
    }

        public NetworkBlackBox(){
                properties = loadPropertiesFromFile() ;
                clientSide = new ClientSide(this,properties) ;
                clientSideThread = new Thread(clientSide, "Client Side Thread") ;
                serverSide = new ServerSide(this,properties) ;
                serverSideThread = new Thread(serverSide, "Server Side Thread") ;
                clientSideThread.start();
//                serverSideThread.start();


        }

        public void clientHasConnected(){
            serverSideThread.start();
        }

        public void clientSideToServerSide(String str_data){
                serverSide.sendData(str_data) ;
        }

        public void clientSideToServerSide(int char_data){
                serverSide.sendData(char_data) ;
        }

        public void serverSideToClientSide(String str_data){
                clientSide.receiveData(str_data) ;
        }

        public void serverSideToClientSide(int char_data){
                clientSide.receiveData(char_data) ;
        }
}
