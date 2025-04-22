/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.calculator_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * name: Yakimah Wiley 
 * assignment: M10-SSL
 * date: 4/14/2025 
 * class: CMPSC222 - Secure Coding
 *
 */
public class Calculator_Client {

    /**
     * This is the client side of the calculator.
     * The user provides inputs that are returned to the server application.
     * Depending on the inputs provided here, the server will return calculations 
     * in line with the server-provided options
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        //KeyStore
        System.setProperty("javax.net.ssl.keyStore", "");
        System.setProperty("javax.net.ssl.keyStorePassword", ""); //My password is “123456”
        System.setProperty("javax.net.debug", "ssl");

        //TrustStore
        System.setProperty("javax.net.ssl.trustStore", "");
        System.setProperty("javax.net.ssl.trustStorePassword", ""); //My password is “123456”
        System.setProperty("javax.net.debug", "ssl");

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (
                SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999); BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream())); BufferedWriter outToServer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));) {
            String string = null;
            while (((string = inFromUser.readLine()) != null)) {
                outToServer.write(string + '\n');
                outToServer.flush();
                String modifiedSentence = inFromServer.readLine(); //Read line from server
                if(modifiedSentence!=null && modifiedSentence.equals("Goodbye")){
                    break;
                }
                System.out.println("\nReceived from server: " + modifiedSentence);
            }
            System.out.println("Thank you and goodbye");
            inFromUser.close();
            inFromServer.close();
            outToServer.close();
        }
    }
}
