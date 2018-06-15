package rs232.sign.control;

//import jssc.SerialPortException;

import java.time.LocalTime;

/**
 * Creates a SeriesTwo and CheckDB objects calls the various methods in both
 * classes
 *
 * @author Ian Van Schaick
 */
public class RS232SignMain {

    /**
     * Creates a SeriesTwo and CheckDB objects calls the various methods in both
     * classes
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SeriesTwo test2 = new SeriesTwo();
            CheckDB test = new CheckDB();
            int index = 0;
            while (true) {
                boolean portConnected = test2.getConnected();
                test.setPortError(portConnected);
                if (index == 0 && portConnected) {
                    System.out.println("Ready for tour time input via web page. Default is 10AM");
                    index++;
                }
//                if (!test.selectisUpdated() && portConnected) {
                LocalTime now = LocalTime.now();
                LocalTime onLimit = LocalTime.parse("19:00");
                LocalTime offLimit = LocalTime.parse("08:00");
                
                LocalTime onLimit2 = LocalTime.parse("07:00");
                LocalTime offLimit2 = LocalTime.parse("09:30");
                
                if (now.isAfter(onLimit) && now.isBefore(offLimit)) {
                    String nextTour = test.selectMessage();
                    boolean acknowledge = false;
                    do {
                        test2.writeNextTour(nextTour);
                        acknowledge = test2.readAck();
                    } while (acknowledge == false);

                    boolean cottagesAvail = false;
                    if (test.selectscrollON()) {
                        cottagesAvail = true;
                    }
                    //System.out.println("The message is: " + message);
                    //System.out.println("Isupdated is: " + test.selectisUpdated());
                    //System.out.println("Acknowledged: " + acknowledge);
//                    if (acknowledge) {
//                        test.setisUpdated();
//                    }
                    //System.out.println("Isupdated is: " + test.selectisUpdated());
//                }
                    Thread.sleep(8 * 1000); // 8 second delay to hold the message on the screen for 8 seconds

                    boolean acknowledge3 = false;
                    do {
                        test2.writeFillChars();
                        acknowledge3 = test2.readAck();
                    } while (acknowledge3 == false);
                    Thread.sleep(250); // 250 ms delay to blank the screen

                    boolean acknowledge2 = false;
                    do {
                        test2.writeCottagesAvail(cottagesAvail);
                        acknowledge2 = test2.readAck();
                    } while (acknowledge2 == false);

                    Thread.sleep(8 * 1000); // 8 second delay to hold the message on the screen for 8 seconds

                    boolean acknowledge4 = false;
                    do {
                        test2.writeFillChars();
                        acknowledge4 = test2.readAck();
                    } while (acknowledge4 == false);
                    Thread.sleep(250); // 250 ms delay to blank the screen

                } //End of time check
                
                boolean acknowledge5 = false;
                do {
                    test2.writeOfficeDir();
                    acknowledge5 = test2.readAck();
                } while (acknowledge5 == false);
                
                Thread.sleep(8 * 1000); // 8 second delay to hold the message on the screen for 8 seconds
                
                boolean acknowledge6 = false;
                do {
                    test2.writeFillChars();
                    acknowledge6 = test2.readAck();
                } while (acknowledge6 == false);
                Thread.sleep(250); // 250 ms delay to blank the screen
                
//                if (now.isAfter(onLimit2) && now.isBefore(offLimit2)) {
                boolean acknowledge7 = false;
                do {
                    test2.writeOfficeOpens();
                    acknowledge7 = test2.readAck();
                } while (acknowledge7 == false);

                Thread.sleep(8 * 1000); // 8 second delay to hold the message on the screen for 8 seconds

                boolean acknowledge8 = false;
                do {
                    test2.writeFillChars();
                    acknowledge8 = test2.readAck();
                } while (acknowledge8 == false);
                Thread.sleep(250); // 250 ms delay to blank the screen
//            }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
