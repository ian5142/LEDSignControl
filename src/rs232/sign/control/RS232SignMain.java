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

            while (true) {
                boolean portConnected = test2.getConnected();
                test.setPortError(portConnected);
                if (!test.selectisUpdated() && portConnected) {
                    String message = test.selectMessage();
                    boolean scroll = false;
                    if (test.selectscrollON()) {
                        scroll = true;
                    }
                    test2.write(message, scroll);
                    boolean acknowledge = test2.readAck();
                    //System.out.println("The message is: " + message);
                    //System.out.println("Isupdated is: " + test.selectisUpdated());

                    //System.out.println("Acknowledged: " + acknowledge);
                    test.setisUpdated();
                    //System.out.println("Isupdated is: " + test.selectisUpdated());
                }

                Thread.sleep(15 * 1000); // 15 second delay to only check the database and write to the sign every 15 seconds
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
