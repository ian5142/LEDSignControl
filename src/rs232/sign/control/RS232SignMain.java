package rs232.sign.control;

//import jssc.SerialPortException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        GetWeather weather = new GetWeather();
        String lastUpdated = "";
        ArrayList<String> forecast = new ArrayList<> ();
        Date lastDownloaded = new Date();
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("mm");
        String lastDownloadedStr = simpleFormatter.format(lastDownloaded);
        System.out.println("Starting");
        try {
            SeriesTwo test2 = new SeriesTwo();
            Date hour = simpleFormatter.parse("00");
            Date halfhour = simpleFormatter.parse("45");
            
            while (true) {
                Date current = new Date();
                String currentStr = simpleFormatter.format(current);
                int count = 0;
                //(current.after(hour) && current.before(halfhour)) && 
//                if (current.after(hour) && current.before(halfhour)) {
//                System.out.println("Getting Forecast");
                weather.getDoc();
                forecast = weather.getForecast();
                lastUpdated = weather.getLastUpdated();
//                }
                ArrayList<String> sent = new ArrayList<>();
                for (int i = 0; i < forecast.size(); i++) {
                    boolean portConnected = test2.getConnected();
                    if (forecast.get(i).equals("Night")) {
                        count++;
                    }
                    if (count == 0) {
                        test2.write(forecast.get(i), true);
                        int length = forecast.get(i).length();
                        if (length <= 10) {
                            Thread.sleep(2 * 1000);
                        } else if (length > 10 && length <= 20) {
                            Thread.sleep(4 * 1000);
                        } else if (length > 20 && length <= 40) {
                            Thread.sleep(6 * 1000);
                        } else if (length > 40 && length <= 60) {
                            Thread.sleep(8 * 1000);
                        } else if (length > 60) {
                            Thread.sleep(8 * 1000);
                        }
                        //+ forecast.get(i+1)
                        boolean acknowledge = test2.readAck();

//                    System.out.println("The message is: " + i);
                        //System.out.println("Isupdated is: " + test.selectisUpdated());
                        //System.out.println("Acknowledged: " + acknowledge);
//                    if (acknowledge) {
//                        test.setisUpdated();
//                    }
                        //System.out.println("Isupdated is: " + test.selectisUpdated());
                        boolean acknowledge6 = false;
                        do {
                            test2.writeFillChars();
                            acknowledge6 = test2.readAck();
                        } while (acknowledge6 == false);
                        Thread.sleep(250);
                        acknowledge = false;

                    }
                    if (i + 7 == forecast.size()) {
                        test2.write(lastUpdated, true);
                        boolean acknowledge2 = test2.readAck();
                        Thread.sleep(2 * 1000);
                        break;
                    }
                    
                }
                
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(RS232SignMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
