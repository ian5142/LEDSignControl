/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.sign.control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Ian Van Schaick
 */
public class GetWeather {
    Document doc;
    public GetWeather () {
        getDoc();
    }
    
    private void getDoc () {
        try {
            doc = Jsoup.connect("https://weather.gc.ca/city/pages/ns-31_metric_e.html").get();
        } catch (HttpStatusException ex) {
            System.out.println("Error: 404.");
        } catch (IOException ex) {
            Logger.getLogger(GetWeather.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Elements selectForecast () {
        Elements allRows = doc.select("tbody td");
        return allRows;
    }
}
