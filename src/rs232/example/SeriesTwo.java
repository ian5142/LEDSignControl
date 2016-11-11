/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

/**
 * Creates string to send via RS232 via serial.
 * @author Ian Van Schaick
 */
public class SeriesTwo {
    String header;
    String address;
    String startMes;
//    String body;
    String endMes;
    String seq;
    int numseq;
    String chksum;
    String CR;
    String newline;
    Test tester;
    
    /**
     * Calls init() method and creates a Test object 
     */
    public SeriesTwo () {
        init();
        tester = new Test();
    }
    
    /**
     * initializes all of the variables
     */
    private void init () {
        header = Character.toString( (char) 1);
        address = "1";
        startMes = Character.toString( (char) 2);
//        body = "This is a Test by the way.";
        endMes = Character.toString( (char) 4);
        seq = "0";
        numseq = 0;
        CR = "\r";
        newline = "\n";
        chksum = "";
    }
    
    /**
     * Calculates the checksum from a given String body
     * @param body The string to calculate a checksum for
     * @return The checksum in String format
     */
    private String calculateChksum (String body) {
        String sum = header + address + startMes + endMes + seq;
        for (int i = 0 ; i < body.length() ; i++) {
            sum += body.charAt(i);
        }
        sum = sum.substring(sum.length()-2);
        if (sum.length() == 1) {
            chksum = "0" + sum;
        }
        return sum;
    }
    
    /**
     * Creates the final Message, sends it to the Test object
     * @param body The message to write to the screen.
     */
    protected void write (String body) {
        chksum = calculateChksum(body);    
        String message = header + address + startMes + body + endMes + seq + 
                chksum + newline + CR;
        boolean success = tester.testWrite(message);
//        String s = "";
//        for (int i = 0; i < message.length(); i++) {
//            char [] array = message.toCharArray();
//            for (int j = 0; j < array.length; j++) {
//                byte b = (byte) array[j];
//                s = s + " " + b;
//            }
//        }
//        System.out.println(s);
        checkSeq();
//        System.out.println("The test was successful: " + success);
    }
    
    /**
     * Tries to read a line from the serial port
     * @return The line read from the serial port
     */
    protected String read() {
        System.out.println("Waiting for readline: ");
        String line = tester.testRead();
        boolean testWrite = tester.testWrite(line);
        return line;
    }
    
    /**
     * Updates the sequence number, numseq.
     */
    private void checkSeq () {
        if (numseq == 9) {
            numseq = 0;
            seq = "0" + numseq;
        }
        else if (numseq >= 1) {
            numseq++;
            seq = "0" + numseq;
        }
        else if (numseq == 0) {
            numseq++;
            seq = "0" + numseq;
        }
        else {
            
        }
    }
}
