/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calculator_server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * name: Yakimah Wiley 
 * assignment: M10-SSL
 * date: 4/14/2025 
 * class: CMPSC222 - Secure Coding
 *
 */

/**
 * The class which holds the protocols followed by the application
 * @author Apache_PHP
 */
public class Calculator_Protocol {
    //These arrays helps prevent the application from becoming messy with free variables. Whenever
    //a particular value is needed, the relevant index for the specific array is called
    private final String[] states = {"Empty", "Obtaining menu option", "Obtaining first number", "Obtaining second number", "Processing Solution"};
    private final String[] menu_options = {"No Selection", "Addition", "Subtraction", "Division", "Multiplication", "Exit"};
    private final String[] text_selections = {"menu", "invalid input", "number selection", "divide by zero", "invalid number selected", "first number", "second number"};

    private BufferedWriter out; //This declaration is used to make writing to the client easier
    private boolean endProgram = false;
    
    private String current_state = states[0];
    private String selected_option;
    private double first_number;
    private double second_number;
    
    /**
     * Constructor to initialize the calculator
     * @param out
     * @throws IOException 
     */
    Calculator_Protocol(BufferedWriter out) throws IOException{
        System.out.println("Calculator Protocol Running...");
        this.out = out;
    }
    
    /**
     * This function sets the initial state of the program. And also indicates 
     * when user input is first desired. This function is called both at the beginning 
     * of the application and when each run is completed
     * @throws IOException 
     */
    private void StartProgram() throws IOException{
        current_state = states[1];
        selected_option = menu_options[0];
        WriteOut(UserMessage(text_selections[0]), true);
    }
    
    /**
     * This function uses the state and selected option to determine which part
     * of the process the user is currently in. 
     * It then directs the user to the relevant methods
     * 
     * @param input
     * @throws IOException 
     */
    public void CheckUserInput(String input) throws IOException{
        String statement;
        switch(current_state){
            case "Empty"->{
                System.out.println("Do nothing");
                StartProgram();
            }
            case "Obtaining menu option"->{
                short get_response = GetInput(input);
                
                if(get_response == -10527){
                    System.out.println(UserMessage(text_selections[1]));
                }else{
                    current_state = states[2];
                    selected_option = menu_options[get_response];
                    WriteOut(UserMessage(text_selections[5]), true);
                }
                break;
            }
            case "Obtaining first number" ->{
                double get_response = GetNumber(input);
                if(get_response == -10123){
                    System.out.println(UserMessage(text_selections[4]));
                }else{
                    first_number = get_response;
                    current_state = states[3];
                    WriteOut(UserMessage(text_selections[6]), true);
                }
                break;
            }
            case "Obtaining second number"->{
                double get_response = GetNumber(input);
                if (get_response == -10123) {
                    System.out.println(UserMessage(text_selections[4]));
                } else {
                    second_number = get_response;
                    current_state = states[4];
                    ProcessRequest();
                }
                break;
            }
        }
    }
    
    /**
     * This method is simply here to reduce code reuse
     * @param message
     * @param flush
     * @throws IOException 
     */
    private void WriteOut(String message, boolean flush) throws IOException{
        out.write(message);
        out.newLine();
        if(flush){
            out.flush();
        }
    }
    
    /**
     * This function is used to identify which part of the process the user is in
     * when they pass through input
     * @throws IOException 
     */
    private void ProcessRequest() throws IOException{
        switch (selected_option) {
            case "Addition" -> {
                add(first_number, second_number);
                break;
            }
            case "Subtraction" -> {
                sub(first_number, second_number);
                break;
            }
            case "Division" -> {
                div(first_number, second_number);
                break;
            }
            case "Multiplication" -> {
                mul(first_number, second_number);
                break;
            }
            default -> {
                //This option is meant to tell the client side when it's time to exit the program
                WriteOut("Goodbye", true);
                System.out.println("Thank you and goodbye");
                System.exit(0);
                break;
            }
        }
        StartProgram();
    }
    

    /**
     * Obtains the double value used within the math calculations
     * @param input
     * @return
     * @throws IOException 
     */
    private double GetNumber(String input) throws IOException{
        try{
            double value = Double.parseDouble(input);
            if(value < -99 || value > 99){
                return -10123; //Invalid number error
                
            }else{
                return value;
            }
        }catch(Exception ex){
            return -10123; //Invalid number error
        }
    }
    
    /**
     * This method obtains the type of math problem that the user wants to calculate
     * @param input
     * @return
     * @throws IOException 
     */
    private short GetInput(String input) throws IOException{
        try{
            int selection = Integer.parseInt(input);
            if(selection > 5 || selection < 1){
                System.out.println("Invalid selection. Try again.");
                return -10527; //Error code indicating invalid selection
            }else{
                return (short)selection;
            }
        }catch(Exception ex){
            switch(input.toLowerCase()){
                case "add", "addition" ->{
                    return 1;
                }
                case "subtract", "subtraction" ->{
                    return 2;
                }
                case "divide", "division" ->{
                    return 3;
                }
                case "multiply", "multiplication" ->{
                    return 4;
                }
                case "exit" ->{
                    return 5;
                }
                default -> {
                    System.out.println("Invalid selection. Try again.");
                    return -10527; //Error code indicating invalid selection
                }
            }
        }
    }
    
    /**
     * This function simply holds all messages that are returned to the client
     * @param value
     * @return 
     */
    private String UserMessage(String value){
        String selected = "";
        switch(value){
            case "menu" ->{
                selected ="SELECT AN OPTION\t\t1. Addition\t2. Subtration\t3. Division\t4. Multiplication\t5. Exit";
                break;
            }
            case "invalid input"->{
                selected = "Invalid selection. Try again. (Press Enter)";
                break;
            }
            case "number selection"->{
                selected = "Select an number between -99 and 99.";
                break;
            }
            case "divide by zero" ->{
                selected = "You can't divide by 0. Nice try though! (Press Enter)";
                break;
            }
            case "invalid number selected" ->{
                selected = "Invalid value. Select a number between -99 and 99.(Press Enter)";
                break;
            }
            case "first number"->{
                selected = "What's your first number?";
            }
            case "second number"->{
                selected = "What's your second number?";
            }
        }
        return selected;
    }
    
    
    /************ GETTERS ************/
    
    public String GetState(){
        return current_state;
    }
    
    public String GetSelection(){
        return selected_option;
    }
    
    public boolean EndProgram(){
        return endProgram;
    }
    
    /************ MATH FUNCTIONS ************/
    
    private void add(double x, double y) throws IOException{
        double solution = x + y;
        WriteOut(String.format("Addition: %,.5f + %,.5f = %,.5f", x, y, solution), true);
    }
    
    private void sub(double x, double y) throws IOException{
        double solution = x - y;
        WriteOut(String.format("Subtraction: %,.5f - %,.5f = %,.5f", x, y, solution), true);
    }
    
    private void div(double x, double y) throws IOException {
        if(y == 0){
            System.out.println("You can't divide by 0. Nice try though!");
        }else{
            double solution = (double)x / y;
            WriteOut(String.format("Division: %,.5f / %,.5f = %,.5f", x, y, solution), true);
        }        
    }
    
    private void mul(double x, double y) throws IOException {
        double solution = x * y;
        WriteOut(String.format("Multiplication: %,.5f * %,.5f = %,.5f", x, y, solution), true);
    }
    
    
}

