package rs232.sign.control;

//import jssc.SerialPortException;
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
                String nextTour = test.selectMessage();
                boolean acknowledge = false;
                do {
                    test2.writeNextTour(nextTour);
                    acknowledge = test2.readAck();
                } while (acknowledge == false);

//                    boolean scroll = false;
//                    if (test.selectscrollON()) {
//                        scroll = true;
//                    }
                //System.out.println("The message is: " + message);
                //System.out.println("Isupdated is: " + test.selectisUpdated());
                //System.out.println("Acknowledged: " + acknowledge);
//                    if (acknowledge) {
//                        test.setisUpdated();
//                    }
                //System.out.println("Isupdated is: " + test.selectisUpdated());
//                }
                Thread.sleep(8 * 1000); // 8 second delay to hold the message on the screen for 8 seconds
                
                boolean acknowledge2 = false;
                do {
                    test2.writeCottagesAvail();
                    acknowledge2 = test2.readAck();
                } while (acknowledge2 == false);
                
                Thread.sleep(8 * 1000); // 8 second delay to hold the message on the screen for 8 seconds
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
