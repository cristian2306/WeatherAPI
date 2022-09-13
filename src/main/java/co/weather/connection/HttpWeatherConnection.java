/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.weather.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author cristian.castellanos
 */
public class HttpWeatherConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://api.openweathermap.org/data/2.5/weather?appid=f70fd1eb9fb8c63c6a242766f8f90670&q=";
    private static final String API_KEY = "f70fd1eb9fb8c63c6a242766f8f90670";

    public static String getWeather(String city) throws IOException {

        URL obj = new URL(GET_URL+city);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            return response.toString();
            // print result
        } else {
            return "GET request not worked";
        }
    }
}
