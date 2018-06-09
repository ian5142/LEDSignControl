/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.sign.control;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.xml.bind.DatatypeConverter;

/**
 * Creates string to send via RS232 via serial using RS232Control
 *
 * @author Ian Van Schaick
 */
public class SeriesTwo {

    char header;
    String address;
    char startMes;
//    String body;
    char ETX;
    char endMes;
    String seq;
    int numseq;
    String chksum;
    char CR;
    char newline;
    RS232Control controller;
    char posAck;
    char negAck;

    /**
     * Calls init() method and creates a RS232Control object
     */
    public SeriesTwo() {
        init();
        controller = new RS232Control();
    }

    /**
     * initializes all of the variables
     */
    private void init() {
        header = (char) 0x1;
        address = "1";
        startMes = (char) 0x2;
//        body = "This is a Test by the way.";
        ETX = (char) 0x3;
        endMes = (char) 0x4;
        seq = "1";
        numseq = 1;
        CR = (char) 0xD;
        newline = (char) 0x10;
        chksum = "";
        posAck = (char) 0x6;
        negAck = (char) 0x15;
    }
    
    /**
     * Calls the serialConnected method in RS232Control.java
     * @return Returns the value sent by serialConnected, true if the serial 
     * port is connected.
     */
    protected boolean getConnected () {
        boolean connected = controller.serialConnected();
        return connected;
    }
    
    /**
     * Calculates the checksum from a given String body
     *
     * @param body The string to calculate a checksum for
     * @param scroll True if scrolling text is required, else false
     * @return The checksum in String format
     */
    protected String calculateChksum(String body) {
        int hexHeader = 0x1;

        ArrayList<String> addressArray = toHex(address);

        long hexAddress = Long.parseLong(addressArray.get(0) + "", 16);
        int hexStartMes = 0x2;
        int hexEndMes = 0x4;

        ArrayList<String> seqArray = toHex(seq);

        long hexSeq = Long.parseLong(seqArray.get(0) + "", 16);
        long sum = hexHeader + hexAddress + hexStartMes + hexEndMes + hexSeq;
//        if (scroll) {
//            int scrollint = 25;
//            sum += scrollint;
//        }

        ArrayList<String> bodyArray = toHex(body);

        long hexBody = 0;
        for (int i = 0; i < bodyArray.size(); i++) {
            long l = Long.parseLong(bodyArray.get(i) + "", 16);
            hexBody += l;
        }

        sum += hexBody;
        sum = sum % 256;

        String checksum = Long.toHexString(sum);
        checksum = checksum.toUpperCase();

//        System.out.println("This is the final sum in hex: " + checksum);
        return checksum;
    }

    /**
     * Converts a String to a hexadecimal value
     *
     * @param arg The string to be converted
     * @return The string in hexadecimal format
     */
    protected ArrayList<String> toHex(String arg) {
        String s;
        s = DatatypeConverter.printHexBinary(arg.getBytes(StandardCharsets.US_ASCII));
        ArrayList<String> hexBody = new ArrayList<>(s.length());

        char[] hexbody2 = s.toCharArray();

        int length = s.length();

        for (int i = 0; i < length;) {
            hexBody.add(s.substring(i, i + 2));
            i += 2;
        }
        return hexBody;
    }

    /**
     * Creates the final Next Boat Tour Message, sends it to the RS232Control object
     *
     * @param nextTour The next tour time in string format (ex: 10AM)
     */
    protected void writeNextTour(String nextTour) {

        String body = "Next Boat Tour: " + nextTour;
        chksum = calculateChksum(body);
        String message;
//        if (scroll) {
//            char scrollChar = (char) 0x19;
//            message = header + address + startMes + scrollChar + body + endMes + 
//                    numseq + chksum + CR;
//        } else {
            message = header + address + startMes + body + endMes + numseq + chksum + CR;
//        }
        boolean success = controller.testWrite(message);
        checkSeq();
    }
    
    /**
     * Creates the final Cottages Available Message, sends it to the RS232Control object
     *
     */
    protected void writeCottagesAvail() {

        String body = "Cottages Available";
        chksum = calculateChksum(body);
        String message;
//        if (scroll) {
//            char scrollChar = (char) 0x19;
//            message = header + address + startMes + scrollChar + body + endMes + 
//                    numseq + chksum + CR;
//        } else {
            message = header + address + startMes + body + endMes + numseq + chksum + CR;
//        }
        boolean success = controller.testWrite(message);
        checkSeq();
    }

    /**
     * Reads the Acknowledge or Not Acknowledge character back from the sign
     * @return Returns true if the Acknowledge character is received, else false.
     */
    protected boolean readAck() {
        byte [] readArray = controller.testRead();
        boolean acknowledge = false;
        if (readArray[3] == 6) {
            acknowledge = true;
//            System.out.print("The message was sent successfully.");
        }
        return acknowledge;
    }

    /**
     * Tries to read a line from the serial port
     *
     * @return The line read from the serial port
     */
    protected String read() {
        byte [] readArray = controller.testRead();
        String readString = new String(readArray);
        return readString;
    }

    /**
     * Updates the sequence number, numseq.
     * The sequence number is a decimal number between 0 and 9. It is incremented 
     * after each message is sent. 
     * When it gets to 9 it resets to 0. The sequence number helps identify the message.
     */
    private void checkSeq() {
        if (numseq == 9) {
            numseq = 0;
            seq = numseq + "";
        } else if (numseq >= 1) {
            numseq++;
            seq = numseq + "";
        } else if (numseq == 0) {
            numseq++;
            seq = numseq + "";
        }
    }
    
    /**
     * Checks if there are any blink character sequences in the body.
     * The Start Blink character sequence is "~|^".
     * The Stop Blink character sequence is "^|~".
     * These are replaced with 0x10 and 0x12 respectively.
     * @param body
     * @return 
     */
    private String checkBlink(String body) {
        ArrayList<Character> blinkfix = new ArrayList<Character>();
        int blinkON = -1;
        int blinkOFF = -1;
        
        if (body.contains("~|^") && body.contains("^|~")) {
            blinkON = body.indexOf("~|^");
            blinkOFF = body.indexOf("^|~");
        }
        
        for (int i = 0; i < body.length(); i++) {
            char current = body.charAt(i);
            if ( i == blinkON && blinkON != -1) {
                current = (char) 0x10;
                i += 2;
            }
            if ( i == blinkOFF && blinkOFF != -1) {
                current = (char) 0x12;
                i += 2;
            }
            blinkfix.add(current);
        }
        
        String newbody = "";
        for (int i = 0 ; i < blinkfix.size() ; i++) {
            newbody = newbody + blinkfix.get(i);
        }
        
        return newbody;
    }
}
