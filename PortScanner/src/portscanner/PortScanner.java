/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsdpirate
 */
public class PortScanner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {

        Scanner scanner = new Scanner(System.in);
        InetAddress ipAddr;
        Socket socket = new Socket();
        InetSocketAddress insAddress;

        System.out.println("**** Port Scanner ****");
        System.out.print("Enter the target ip: ");
        ipAddr = InetAddress.getByName(scanner.nextLine());

        System.out.print("Enter the port range (20-80): ");
        String portRange = scanner.nextLine();
        System.out.println(ipAddr.getHostName());
        
        int[] range = new int[2];

        String portRangeRegex = "((([0-9])|([1-9]([0-9]{1,4})))\\-(([0-9])|([1-9]([0-9]{1,4}))))";
        String singlePortRegex = "([0-9])|(([1-9])([0-9]{1,4}))";

        if (portRange.matches(portRangeRegex)) {

            int dashPosition = portRange.indexOf('-');
            String lowerLimit, upperLimit;
            lowerLimit = portRange.substring(0, dashPosition);
            upperLimit = portRange.substring(dashPosition + 1);

            range[0] = Integer.parseInt(lowerLimit);
            range[1] = Integer.parseInt(upperLimit);
            Arrays.sort(range);

        } else if (portRange.matches(singlePortRegex)) {

            range[0] = range[1] = Integer.parseInt(portRange);

        }

        
        
        ConnectionResult[] results = new ConnectionResult[range[1] - range[0] + 1];
        int threadNumber = (int)Math.ceil(results.length / 50.0);
        ScanThread st [] = new ScanThread[threadNumber];
        
        //Initializing results array
        for(int i = range[0]; i < range[1] + 1; ++i){
            results[i - range[0]] = new ConnectionResult(i);
        }
        
        //Thread workload distribution
        for(int i = 0; i < threadNumber; ++i){
            st[i] = new ScanThread(ipAddr, results, i);
        
        }
        
        double estimatedTime = (1000 + results.length + 50) * 50 / 1000 ;
        
        System.out.println("Starting. Estimated time: " + estimatedTime + "s");
       Thread ts [] = new Thread[st.length];
       for(int i = 0; i < ts.length; ++i){
       
           ts[i] = new Thread(st[i]);
           ts[i].start();
       }
       
       for(Thread t: ts){
       
            try {
                t.join();
            } catch (InterruptedException ex) {
                
            }
       }
       System.out.println("--------------------\n       Results:      \n--------------------");
       for(ConnectionResult cr: results){
           if(cr.getStatus().equals("open") || cr.getStatus().equals("blocked")){
               System.out.println(cr.toString());
           }
       }
       
//        System.out.println(Arrays.toString(results));

        /*
         boolean[] results = new boolean[range[1]];
         int j = 0;
         //Actual port testing
         for (int i = range[0]; i < range[1] + 1; ++i, ++j) {

         insAddress = new InetSocketAddress(ipAddr, i);
         String strR = "";
         try {
         socket.connect(insAddress, 1500);
         results[j] = true;
         strR = "open";

         } catch (SocketTimeoutException ex) {
                
         results[j] = false;
         strR = "closed";
                
         } catch (IOException ex) {
         strR = "may be open but filtered";
         }

         System.out.println(i + " status: " + strR);
         socket = new Socket();
         }
         */
       // System.out.println(portRange.matches(portRangeRegex));
       // System.out.println(portRange.matches(singlePortRegex));
       // System.out.println(Arrays.toString(range));
        

    }

}
