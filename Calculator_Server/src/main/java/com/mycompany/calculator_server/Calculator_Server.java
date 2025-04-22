/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.calculator_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * name: Yakimah Wiley 
 * assignment: M10-SSL
 * date: 4/14/2025 
 * class: CMPSC222 - Secure Coding
 *
 */
public class Calculator_Server {

    /**
     * This application is the server side of the calculator.
     * The server-side recieves input from the client, then determines how that input is used
     * within the program.
     * 
     * The options provided are for: addition, subtraction, multiplication, and division
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException{
        //Set up keystore
        System.setProperty("javax.net.ssl.keyStore", "");
        System.setProperty("javax.net.ssl.keyStorePassword", ""); //My password is “123456”
        System.setProperty("javax.net.debug", "ssl");
        //Set up trustStore
        System.setProperty("javax.net.ssl.trustStore", "");
        System.setProperty("javax.net.ssl.trustStorePassword", ""); //My password is “123456”
        System.setProperty("javax.net.debug", "ssl");

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try(
                SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999); 
                SSLSocket sslSocket = (SSLSocket) sslserversocket.accept(); 
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(sslSocket.getInputStream())); 
                BufferedWriter outToClient = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            ){
            //Initializes calculator
            Calculator_Protocol calculator = new Calculator_Protocol(outToClient);
            boolean endProgram = false;
            
            String line = null;
            while (((line = inFromClient.readLine()) != null) && endProgram == false) {
                System.out.println("Received from client:" + line);
                //The below two statements are meant to lookout for when the application is meant to close
                //Writing the code this way helps prevent the application from crashing
                endProgram = calculator.EndProgram();
                if (endProgram == true) {
                    inFromClient.close();
                    outToClient.close();
                    System.exit(0);
                }
                calculator.CheckUserInput(line);
            }
        }
    }
}
