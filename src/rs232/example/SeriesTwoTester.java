/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs232.example;

/**
 *
 * @author Ian Van Schaick
 */
public class SeriesTwoTester {
    public static void main(String[] args) {
        SeriesTwo test2 = new SeriesTwo();
        String calculateChksum = test2.calculateChksum("int val = -32768; String hex = Integer.toHexString(val); int parsedResult = (int) Long.parseLong(hex, 16); System.out.println(parsedResult); That's how you can do it. The reason why it doesn't work your way: Integer.parseInt takes a signed int, while toHexString produces an unsigned result.");
    }
}
