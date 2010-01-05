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

package org.opencsta.utils.blackbox;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientSide implements Runnable{

        private ServerSocket clientSideConnectionSocket;
        private Socket client;
        private DataInputStream in;
        private DataOutputStream out ;
        private int line;
        private boolean runFlag = false ;
        private NetworkBlackBox parent ;

private StringBuffer chris ;
private byte[] buf;

        public ClientSide(NetworkBlackBox _parent){
                this.parent = _parent ;
                chris = new StringBuffer() ;
                buf = new byte[1024] ;
                setRunFlag(true) ;
                System.out.println("Client side initialising") ;
        }

        public void run() {
                System.out.println("Starting clientside") ;
                try{
                        clientSideConnectionSocket = new ServerSocket(5038);
                } catch (IOException e) {
                        System.out.println("Could not listen on port 7000");
                        System.exit(-1);
                } catch (NullPointerException e){
                    System.out.println( this.getClass().getName() + " Null Pointer Exception") ;
                }

                try{
                        client = clientSideConnectionSocket.accept();
                        System.out.println("Accepted a client connection") ;
                        parent.clientHasConnected() ;
                } catch (IOException e) {
                        System.out.println("Accept failed: 7000");
                        System.exit(-1);
                } catch (NullPointerException e){
                    System.out.println( this.getClass().getName() + " Null Pointer Exception") ;
                }

                try{
                        in = new DataInputStream(client.getInputStream());
                        out = new DataOutputStream( client.getOutputStream() );
                } catch (IOException e) {
                        System.out.println("Accept failed: 4444");
                        System.exit(-1);
                } catch (NullPointerException e){
                    System.out.println( this.getClass().getName() + " Null Pointer Exception") ;
                }

                while(runFlag){
                        try{
                                line = in.read(buf);
                                System.out.println(line + " bytes received") ;
                                buf2SBChris(line) ;
//                              line = in.read();
//                              chris.append((char)line) ;
//                              //Send data to server side
//                              System.out.println("Sending: ") ;
//                              System.out.print( Integer.toHexString(line) + " ") ;
//                              parent.clientSideToServerSide(line);
                        } catch (IOException e) {
                                System.out.println("Read failed");
                                System.exit(-1);
                        } catch (NullPointerException e){
                               System.out.println( this.getClass().getName() + " Null Pointer Exception") ;
                        }
                        parent.clientSideToServerSide(new String(chris) ) ;
                }
        }
        public boolean isRunFlag() {
                return runFlag;
        }

        public void setRunFlag(boolean runFlag) {
                this.runFlag = runFlag;
        }

        public void receiveData(int line){
                char[] chbyte = {(char)line} ;
                try {
                        out.writeBytes(new String(chbyte) ) ;
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (NullPointerException e){
                    System.out.println( this.getClass().getName() + " Null Pointer Exception") ;
                }
        }

        public void receiveData(String str_rec){
                try {
                        out.writeBytes( str_rec ) ;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace() ;
                }
        }

        public void TestChris(StringBuffer cm){
                System.out.print("Client ---> Server | S: ") ;
                for( int i = 0 ; i < cm.length() ; i++ ){
                        System.out.print( Integer.toHexString((char)cm.charAt(i)) + " " ) ;
                }
                System.out.println("") ;
        }

        private void buf2SBChris(int length){
                chris = new StringBuffer();
                for( int i = 0 ; i < length ; i++){

            if( (short)buf[i] < 0 ){
                append2chris( (int)buf[i] + 256 ) ;
            }

            else{
                Byte b = new Byte(buf[i]) ;
                append2chris( (int) b.intValue() ) ;
            }
                }
                TestChris(chris) ;
        }

        private void append2chris(int thisByte){
                chris.append((char)thisByte) ;
        }
}