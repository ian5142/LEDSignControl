/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.sign.control;

import java.io.IOException;
import java.util.ArrayList;
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
    /**
     * Creates the GetWeather object. Calls getDoc.
     */
    public GetWeather () {
        getDoc();
    }
    
    /**
     * Gets the HTML document, initially called by the constructor
     */
    protected void getDoc () {
        try {
            doc = Jsoup.connect("https://weather.gc.ca/city/pages/ns-31_metric_e.html").get();
        } catch (HttpStatusException ex) {
            System.out.println("Error: 404.");
        } catch (IOException ex) {
            System.out.println("Error: 404.");
        }
    }
    
    /**
     * Selects all of the elements that match the parameters "tbody td"
     * @return The selected elements
     */
    private Elements selectForecast () {
        Elements allRows = doc.select("tbody td");
        return allRows;
    }
    
    /**
     * Selects all of the elements that match the parameters "section details summary span"
     * @return The selected elements
     */
    private Elements selectForecastUpdated () {
        Elements allRows = doc.select("section details summary span");
        return allRows;
    }
    
    /**
     * Formats the Unformatted Elements from the HTML into an ArrayList<String>, Removed the HTML tags, etc.
     * @param unformatted The unformatted Elements (returned from one of the select methods.
     * @return An ArrayList<String> with all of the text from the elements.
     */
    private ArrayList<String> format (Elements unformatted) { 
        ArrayList<String> formatted = new ArrayList<> ();
        unformatted.forEach(el -> formatted.add(el.text()) );
        return formatted;
    }
    
    /**
     * Gets the forecast for Sydney, NS from Environment Canada
     * Calls selectForecast to actually get the HTML.
     * Formats the HTML into an ArrayList by calling the format() method.
     * @return An ArrayList<String> with line by line of the forecast for Sydney, NS
     */
    protected ArrayList<String> getForecast () {
        ArrayList<String> forecast = new ArrayList<> ();
        Elements unformattedForecast = selectForecast();
        forecast = format(unformattedForecast);
        ArrayList<String> newForecast = new ArrayList<> ();
        for (String line : forecast) {
            if (line.length() > 20 ) {
                String paragraph[] = line.split("\\.");
                for(String shortLine : paragraph) {
                    newForecast.add(shortLine + ".");
                }
            }
            else {
                newForecast.add(line);
            }
        }
        newForecast.forEach(String -> System.out.println("Line:" + String));
        return newForecast;
    }
    
    /**
     * Creates a String of the date and time that the forecast was issued.
     * @return A string of the date and time that the forecast was issued.
     */
    protected String getLastUpdated () {
        ArrayList<String> lastUpdated = new ArrayList<> ();
        Elements unformattedForecast = selectForecast();
        Elements unformattedUpdate = selectForecastUpdated();
        lastUpdated = format(unformattedUpdate);
//        lastUpdated.forEach(String -> System.out.println("Line:" + String));
        String lastUpdatedStr = lastUpdated.get(1);
        return lastUpdatedStr;
    }
}
