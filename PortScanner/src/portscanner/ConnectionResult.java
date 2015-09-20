/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

/**
 *
 * @author lsdpirate
 */
public class ConnectionResult {
    private int port;
    private String status;

    public String getStatus() {
        return status;
    }
    
    
    public ConnectionResult(int p){
        this.port = p;
    }
    public ConnectionResult (int p, String status){
    
        this.port = p;
        this.status = status;
    }
    
    
    public void setStatus(String s){
        this.status = s;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public String toString(){
    String r = "";
    r += port;
    r += " is ";
    r += status;    
    return r;    
    }
}
