/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsdpirate
 */
public class ScanThread implements Runnable {

    
    private InetAddress inAddr;
    private int timeout;
    private ConnectionResult[] results;
    private int offset;

   
    public ScanThread(InetAddress addr, ConnectionResult[] cr, int p) {
        this.offset = p * 50;
        this.results = cr;
        this.inAddr = addr;
    }

    @Override
    public void run() {

        Socket soc = new Socket();

        int limit = this.offset + 50;

        if (this.offset + 50 >= this.results.length) {
            limit = this.results.length - 1;

        }
        for (int i = this.offset; i < limit + 1; ++i) {

            InetSocketAddress insAddress;
            synchronized (this.inAddr) {
                insAddress = new InetSocketAddress(this.inAddr, this.results[i].getPort());
            }

            String strR = "";

            try {
                soc.connect(insAddress, 1000 + this.offset + 50);
                strR = "open";
                System.out.println(insAddress.getPort() + " is open!");
                
            } catch (SocketTimeoutException ex) {
                strR = "closed";

            } catch (IOException ex) {
                strR = "blocked";
                
            }
            
            soc = new Socket();
            synchronized (this.results) {

                this.results[i].setStatus(strR);

            }

        }
        try {
            soc.close();
        } catch (IOException ex) {
            
        }
    }

}
