/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.xml.bind.DatatypeConverter;

/**
 * Creates string to send via RS232 via serial.
 * @author Ian Van Schaick
 */
public class SeriesTwo {
    char header;
    String address;
    char startMes;
//    String body;
    char endMes;
    String seq;
    int numseq;
    String chksum;
    char CR;
    char newline;
    Test tester;
    char posAck;
    char negAck;
    
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
        header = (char) 0x1;
        address = "1";
        startMes = (char) 0x2;
//        body = "This is a Test by the way.";
        endMes = (char) 0x4;
        seq = "1";
        numseq = 0;
        CR = (char) 0x13;
        newline = (char) 0x10;
        chksum = "";
        posAck = (char) 0x6;
        negAck = (char) 0x15;
    }
    
    /**
     * Calculates the checksum from a given String body
     * @param body The string to calculate a checksum for
     * @return The checksum in String format
     */
    protected String calculateChksum (String body) {
        System.out.println("Body: " + body);
        int hexHeader = 1;
        
        ArrayList<String> addressArray = toHex(address);
        
        long hexAddress = Long.parseLong(addressArray.get(0) + "", 16);
        int hexStartMes = Integer.parseInt("02", 16);
        int hexEndMes = Integer.parseInt("04", 16);
        
        ArrayList<String> seqArray = toHex(seq);
        
        long hexSeq = Long.parseLong(seqArray.get(0) + "", 16);
        long sum = hexHeader + hexAddress + hexStartMes + hexEndMes + hexSeq;
        
        ArrayList<String> bodyArray = toHex(body);
        
        long hexBody = 0;
        for (int i = 0 ; i < bodyArray.size() ; i++) {
            long l = Long.parseLong(bodyArray.get(i) + "", 16);
            hexBody += l;
        }
        
        sum += hexBody;
        sum = sum % 256;
        
        String checksum = Long.toHexString(sum);
        checksum = checksum.toUpperCase();
        
        System.out.println("This is the final sum in hex: " + checksum);
        return checksum;
    }
    
    /**
     * Converts a String to a hexadecimal value
     * @param arg The string to be converted
     * @return The string in hexadecimal format
     */
    protected ArrayList<String> toHex(String arg) {
        String s;
        s = DatatypeConverter.printHexBinary(arg.getBytes(StandardCharsets.US_ASCII));
        ArrayList<String> hexBody = new ArrayList<>(s.length() );
        System.out.println(s);
        
        char [] hexbody2 = s.toCharArray();
        
        int length = s.length();
        
        for (int i = 0 ; i < length ; ) {
            hexBody.add( s.substring(i, i+2) );
            i += 2;
        }
        return hexBody;
    }
    
    /**
     * Creates the final Message, sends it to the Test object
     * @param body The message to write to the screen.
     */
    protected void write (String body) {
        chksum = calculateChksum(body);    
//        String message = header + address + startMes + body + endMes + numseq + 
//                chksum + newline + CR;
        String message = startMes + (char) 0x19 + body + endMes + CR;
        boolean success = tester.testWrite(message);
        if (success) {
            boolean acknowledge = readAck();
            while(! acknowledge) {
                write(body);
            }
        }
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
    
    protected boolean readAck () {
        boolean acknowledge = tester.testRead();
//        if (line.equals(posAck)) {
//            System.out.print("The message was sent successfully.");
//            acknowledge = true;
//        }
//        else if (line.equals(negAck)) {
//            System.out.println("The message was not sent");
//            acknowledge = false;
//        }
        return acknowledge;
    }
    
    /**
     * Tries to read a line from the serial port
     * @return The line read from the serial port
     */
    protected String read() {
//        System.out.println("Waiting for readline: ");
//        String line = tester.testRead();
        //boolean testWrite = tester.testWrite(line);
        return "";
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
