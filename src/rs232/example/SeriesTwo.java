/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;

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
        header = Integer.toHexString(1);
        address = "1";
        startMes = Integer.toHexString(2);
//        body = "This is a Test by the way.";
        endMes = Integer.toHexString(4);
        seq = "1";
        numseq = 0;
        CR = Integer.toHexString(13);
        newline = Integer.toHexString(10);
        chksum = "";
    }
    
    /**
     * Calculates the checksum from a given String body
     * @param body The string to calculate a checksum for
     * @return The checksum in String format
     */
    private String calculateChksum (String body) {
        System.out.println("Body: " + body);
        int hexheader = 1;
        int hexAddress = Integer.parseInt(address, 16);
        int hexStartMes = 2;
        int hexEndMes = 4;
        int hexSeq = Integer.parseInt(seq, 16);
        int sum = hexheader + hexAddress + hexStartMes + hexEndMes + hexSeq;
        int hexBody = Integer.parseInt(toHex(body), 16);
        System.out.println("Hexbody: " + hexBody);
        sum += hexBody;
        
        String checksum = sum + "";
        checksum = checksum.substring(checksum.length()-2, checksum.length());
        if (checksum.length() == 1) {
            checksum = "0" + checksum;
        }
        return checksum;
    }
    
    /**
     * Converts a String to a hexadecimal value
     * @param arg The string to be converted
     * @return The string in hexadecimal format
     */
    public String toHex(String arg) {
        String s;
        s = DatatypeConverter.printHexBinary(arg.getBytes(StandardCharsets.US_ASCII));
        System.out.println("To Hex: " + s);
        return s;
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
