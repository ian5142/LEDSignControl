/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 * Creates string to send via RS232 via serial.
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
    Test tester;
    char posAck;
    char negAck;

    /**
     * Calls init() method and creates a Test object
     */
    public SeriesTwo() {
        init();
        tester = new Test();
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
     * Calculates the checksum from a given String body
     *
     * @param body The string to calculate a checksum for
     * @param scroll
     * @return The checksum in String format
     */
    protected String calculateChksum(String body, boolean scroll) {
        int hexHeader = 0x1;

        ArrayList<String> addressArray = toHex(address);

        long hexAddress = Long.parseLong(addressArray.get(0) + "", 16);
        int hexStartMes = 0x2;
        int hexEndMes = 0x4;

        ArrayList<String> seqArray = toHex(seq);

        long hexSeq = Long.parseLong(seqArray.get(0) + "", 16);
        long sum = hexHeader + hexAddress + hexStartMes + hexEndMes + hexSeq;
        if (scroll) {
            int scrollint = 25;
            sum += scrollint;
        }

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

        System.out.println("This is the final sum in hex: " + checksum);
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
     * Creates the final Message, sends it to the Test object
     *
     * @param body The message to write to the screen.
     * @param scroll
     */
    protected void write(String body, boolean scroll) {

//        String message = header + address + startMes + body + endMes + numseq + 
//                chksum + newline + CR;
        body = checkBlink(body);
        chksum = calculateChksum(body, scroll);
//        String message = (char) 0x1 + "1" + (char) 0x2 + "This is a test." + (char) 0x4 + "1" + "8C" + CR;
//        String message = header + address + startMes + body + 3 + endMes + numseq + chksum + CR;
        String message;
        System.out.println("Scroll ON: " + scroll);
        if (scroll) {
            char scrollChar = (char) 0x19;
            message = header + address + startMes + scrollChar + body + endMes + numseq + chksum + CR;
        } else {
            message = header + address + startMes + body + endMes + numseq + chksum + CR;
        }
        boolean success = tester.testWrite(message);
//            if (success) {
//                boolean acknowledge = readAck();
//                while (!acknowledge) {
//                    write(body);
//                }
//            }

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

    protected boolean readAck() {
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
     *
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
        } else {

        }
    }

    private String checkBlink(String body) {
        ArrayList<Character> blinkfix = new ArrayList<Character>();
        int blinkON = 0;
        if (body.contains("~|^")) {
            blinkON = body.indexOf("~|^");
        }
        int blinkOFF = 0;
        if (body.contains("^|~")) {
            blinkOFF = body.indexOf("^|~");
        }
        for (int i = 0; i < body.length(); i++) {
            char current = body.charAt(i);
            if ( i == blinkON ) {
                current = (char) 0x10;
            }
            if ( i == blinkOFF ) {
                current = (char) 0x12;
            }
//            if (current == '~') {
//                if ((i + 1) < body.length()) {
//                    if (body.charAt(i + 1) == '|') {
//                        if ((i + 2) < body.length()) {
//                            if (body.charAt(i + 2) == '^') {
//
//                            }
//                        }
//                    }
//                }
//            }
            blinkfix.add(current);
        }
        for (int j = 0 ; j < body.length() ; ) {
            if (blinkfix.get(j) == (char) 0x10) {
                blinkfix.remove(j+1);
                blinkfix.remove(j+2);
                j += 2;
            }
            else if (blinkfix.get(j) == (char) 0x12) {
                blinkfix.remove(j+1);
                blinkfix.remove(j+2);
                j += 2;
            }
        }
//        if (body.contains("~|^")) {
//            blinkfix = blinkfix.replaceAll("~|^", (char) 0x10 + "");
//        }
//        if (body.contains("^|~")) {
//            blinkfix = blinkfix.replaceAll("^|~", (char) 0x12 + "");
//        }
//        System.out.println("Blink fix: " + blinkfix);
        String newbody = "";
        for (int i = 0 ; i < blinkfix.size() ; i++) {
            newbody = newbody + blinkfix.get(i);
        }
        return newbody;
    }
}
