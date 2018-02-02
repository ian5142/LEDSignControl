/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import jssc.*;

/**
 * Contains methods to open, close, read, write to a serial port
 *
 * @author Ian Van Schaick
 */
public class Test {

    static SerialPort serialPort;
    String portName;
    static long portOpen;
    StringBuilder message;
    Boolean receivingMessage;
    SerialPortReader reader;
    String readLine;
    public final Object lock = new Object();
    Boolean acknowledge;

    /**
     * Creates a serial port object
     */
    public Test() {
        portName = findPort();
        serialPort = new SerialPort(portName);
        message = new StringBuilder();
        receivingMessage = false;
        reader = new SerialPortReader();
        readLine = "";
    }

    /**
     * Finds the serial port to be used, in Windows type COM1, for example In
     * Linux, type /dev/pts/3 for example. All serial ports may not be listed.
     *
     * @return The serial port name in String format, used to open and close the
     * port
     */
    private String findPort() {
        System.out.println("What is the port you are using?");

        String[] portNames = SerialPortList.getPortNames();
        for (String portName1 : portNames) {
            System.out.println(portName1);
        }

        Scanner sc = new Scanner(System.in);
        String port = "";
        if (sc.hasNext()) {
            port = sc.next();
        } else {

        }
        return port;
    }

    /**
     * Opens a COM port at the specified settings, can throw an error opening
     * the port
     */
    private void openP() {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_7,
                    SerialPort.STOPBITS_2,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_OUT);
            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(reader);
            serialPort.setRTS(false);
            serialPort.setDTR(false);
            acknowledge = true;
        } catch (SerialPortException ex) {
            System.out.println("There is an error opening port т: " + ex);
        }
    }

    /**
     * Closes the serial port, can throw a SerialPortException error.
     *
     * @return
     */
    private boolean close() {
        boolean success = false;
        try {
            serialPort.closePort();
            success = true;
        } catch (SerialPortException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    /**
     * Writes the String "Hello World Write" to the serial port
     *
     * @return Returns true if the write was successful
     */
    protected boolean testWrite() {
        boolean success = false;
        try {
            openP();
            serialPort.writeString("Hello World Write");
            success = true;
            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    /**
     * Writes the String message to the serial port
     *
     * @param message The string to write to the serial port
     * @return Returns true if the write was successful
     */
    protected boolean testWrite(String message) {
        boolean success = false;
        
        try {
            openP();
            serialPort.writeString(message);
            success = true;
            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    /**
     * Opens the serial port. Tries to read a string from the serial port.
     * Closes the serial port.
     *
     * @return
     */
    protected boolean testRead() {

        try {
            //        boolean success = false;
//        String line = "";
//        try {
//            //openP();
//            line = serialPort.readString(128);
//            success = true;
//            serialPort.closePort();
//        } catch (SerialPortException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (success == true) {
//            System.out.println("Line read: " + line);
//            return line;
//        } else {
//            line = "Did not read.";
//            return line;
//        }
            openP();
            
            //serialPort.addEventListener(new SerialPortReader());
//            readLine = serialPort.readString(10, 5000);
            if(acknowledge == true) {
                System.out.println("The message was acknowledged");
            } 
            else {
                System.out.println("Not acknowledged.");
            }
            serialPort.closePort();
        } 
//          catch (SerialPortException ex) {
//            System.out.println("Error in receiving string from COM-port: " + ex);
//        } catch (SerialPortTimeoutException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.INFO, null, ex);
//            try {
//                serialPort.closePort();
//            } catch (SerialPortException ex1) {
//                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex1);
//            }
        //} 
        catch (SerialPortException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return acknowledge;
    }

    /**
     * In this class must implement the method serialEvent, through it we learn
     * about events that happened to our port. But we will not report on all
     * events but only those that we put in the mask. In this case the arrival
     * of the data and change the status lines CTS and DSR
     */
    private class SerialPortReader implements SerialPortEventListener {
//        StringBuilder message = new StringBuilder();
//        Boolean receivingMessage = false;
//
//        @Override
//        public void serialEvent(SerialPortEvent event) {
//            if (event.isRXCHAR() && event.getEventValue() > 0) {
//                try {
//                    byte buffer[] = serialPort.readBytes();
//                    for (byte b : buffer) {
//                        if (b == '>') {
//                            receivingMessage = true;
//                            message.setLength(0);
//                        } else if (receivingMessage == true) {
//                            if (b == '\r') {
//                                receivingMessage = false;
//                                String toProcess = message.toString();
//                                Platform.runLater(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        processMessage(toProcess);
//                                    }
//                                });
//                            } else {
//                                message.append((char) b);
//                            }
//                        }
//                    }
//                } catch (SerialPortException ex) {
//                    System.out.println(ex);
//                    System.out.println("serialEvent");
//                }
//            }
//        }

        /**
         * Reads the data bit by bit from the serial port Can throw a
         * SerialPortException error
         *
         * @param event
         */
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String line = serialPort.readString(event.getEventValue());
                    if (line.equals((char) 0x6 + "")) {
                        acknowledge = true;
                    }
                    else {
                        acknowledge = false;
                    }
//                    System.out.println("Received response: " + readLine);
//            byte buffer[] = serialPort.readBytes(10);
//            for (byte b: buffer) {
//                if (b == '>') {
//                    receivingMessage = true;
//                    message.setLength(0);
//                }
//                else if (receivingMessage == true) {
//                    if (b == '\r') {
//                        receivingMessage = false;
//                        String toProcess = message.toString();
//                        Platform.runLater(new Runnable() {
//                            @Override public void run() {
//                                processMessage(toProcess);
//                           }
//                        });
//                    }
//                    else {
//                        message.append((char)b);
//                    }
//                }
//            }                
                } catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }

//        public void serialEvent(SerialPortEvent event) {
//            if (event.isRXCHAR()) {//If data is available
//                if (event.getEventValue() == 10) {//Check bytes count in the input buffer
//                    //Read data, if 10 bytes available 
//                    try {
//                        byte buffer[] = serialPort.readBytes(10);
//                    } catch (SerialPortException ex) {
//                        System.out.println(ex);
//                    }
//                }
//            } else if (event.isCTS()) {//If CTS line has changed state
//                if (event.getEventValue() == 1) {//If line is ON
//                    System.out.println("CTS - ON");
//                } else {
//                    System.out.println("CTS - OFF");
//                }
//            } else if (event.isDSR()) {///If DSR line has changed state
//                if (event.getEventValue() == 1) {//If line is ON
//                    System.out.println("DSR - ON");
//                } else {
//                    System.out.println("DSR - OFF");
//                }
//            }
//        }
    }

    /**
     * Prints out the message read from the serial port
     *
     * @param message
     */
    protected void processMessage(String message) {
//        System.out.println(message);
    }
}
