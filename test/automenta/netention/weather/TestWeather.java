/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.weather;

import org.bbelovic.weather.WUndergroundWeatherReader;

/**
 *
 * @author seh
 */
public class TestWeather {
    public static void main(String[] args) {
        //test to verify weather API can accept lat/lng coordinates for weather
        WUndergroundWeatherReader w = new WUndergroundWeatherReader("40,-80");

        w.process();
        
        System.out.println(w.getWeatherModel().getTemperature());
    }
}
