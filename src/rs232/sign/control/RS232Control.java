/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.sign.control;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*; // Java Simple Serial Connector, the library that contains the serial methods

/**
 * Contains methods to open, close, read, write to a serial port
 *
 * @author Ian Van Schaick
 */
public class RS232Control {
    
    static SerialPort serialPort;
    String portName;
    static long portOpen;
    StringBuilder message;
    Boolean receivingMessage;
    SerialPortReader reader;
    String readLine;
    Boolean acknowledge;
    static final char acknowledgeStr = '\u0006';

    /**
     * Creates a serial port object
     */
    public RS232Control() {
        portName = "/dev/ttyUSB0";
        serialPort = new SerialPort(portName);
        message = new StringBuilder();
        receivingMessage = false;
        reader = new SerialPortReader();
        readLine = "";
        acknowledge = false;
}

    /**
     * Finds the serial port to be used, in Windows type COM1, for example In
     * Linux, type /dev/pts/3 for example. The custom USB-RS232 device, using a
     * MCP2200, is on /dev/ttyACM0/
     * All serial ports may not be listed.
     *
     * @return The serial port name in String format, used to open and close the
     * port
     */
    private String findPort() {
        System.out.println("List of COM ports:");
        String[] portNames = SerialPortList.getPortNames();
        for (String portName1 : portNames) {
            System.out.println(portName1);
        }
        
        System.out.println("What COM port are you using?");
        System.out.println("Please type it in how it appears above.");
        Scanner sc = new Scanner(System.in);
        String port = "";
        if (sc.hasNext()) {
            port = sc.next();
        } else {

        }
        return port;
    }
    
    /**
     * Checks if the serial port is connected
     * @return Returns true if any of the serial ports found using getPortNames() 
     * matches the portName global variable (what ever the user types in when 
     * findPort() is called).
     */
    protected boolean serialConnected () {
        boolean connected = false;
        String[] portNames = SerialPortList.getPortNames();
        for (String portName1 : portNames) {
            if (portName1.equals(portName) ) {
                connected = true;
//                System.out.println("Connected successfully to serial port: " + portName);
            }
            else {
//                System.out.println("Can not connect to serial port: " + portName);
            }
        }
        return connected;
    }

    /**
     * Opens a COM port at the specified settings (9600 8N1)
     * Can throw an error opening the port
     */
    private void openP() {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
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
            Logger.getLogger(RS232Control.class.getName()).log(Level.SEVERE, null, ex);
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
//            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(RS232Control.class.getName()).log(Level.SEVERE, null, ex);
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
//            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(RS232Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    /**
     * Opens the serial port. Tries to read a string from the serial port.
     * Closes the serial port.
     *
     * @return Returns the byte array read from the serial port.
     */
    protected byte [] testRead() {
        byte [] readArray = null;
        try {
            readArray = serialPort.readBytes(9);
            serialPort.closePort();
        } 
        
        catch (SerialPortException ex) {
            Logger.getLogger(RS232Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        return readArray;
    }

    /**
     * In this class must implement the method serialEvent, through it we learn
     * about events that happened to our port. But we will not report on all
     * events but only those that we put in the mask. In this case the arrival
     * of the data and change the status lines CTS and DSR
     */
    private class SerialPortReader implements SerialPortEventListener {

        /**
         * Reads the data bit by bit from the serial port Can throw a
         * SerialPortException error
         *
         * @param event
         */
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() == 10) {
                try {
                    String line = serialPort.readString(event.getEventValue());
//                    acknowledgeStr + acknowledgeStr + 
                    System.out.println("serialEvent: " + line);
                    if (line.contains((char) 0x6 + "")) {
                        acknowledge = true;
//                        System.out.println("Acknowledged");
                        
                    }
                    else {
                        acknowledge = false;
                    }
//                    System.out.println("Received response: " + readLine);
               
                } catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }
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
