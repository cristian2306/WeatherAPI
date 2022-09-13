package co.weather.servers;

import co.weather.connection.HttpWeatherConnection;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class HttpServer {
    
    public static String HTTP_HEADER = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    public static String JSON_HEADER = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n";
    public static String ERROR_HEADER = "HTTP/1.1 404 Bad/Request\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + getPort() + ".");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("GET")){
                    String query = inputLine.split(" ")[1];
                    if(query.contains("/")){
                        if(query.contains("?")){
                            System.out.println("Se llama a la API");
                            String city = query.split("=")[1];
                            System.out.println("City: " + city);
                            outputLine = weatherQuery(clientSocket, city);
                            out.println(outputLine);
                        }else{
                            outputLine = getForm(clientSocket);
                            out.println(outputLine);
                        }
                    }
                }
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            out.close();
            in.close();
            System.out.println("Se supone que se conecta al socket");
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return new Integer(System.getenv("PORT"));
        } else {
            return 35000;
        }
    }

    public static String getForm(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        String outputLine = HTTP_HEADER
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Form Example</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Weather Api</h1>\n"
                + "        <p>Give the name of the city from that you want to look its weather"
                + "        <form action=\"/hello\">\n"
                + "            <label for=\"city\">City:</label><br>\n"
                + "            <input type=\"text\" id=\"city\" name=\"city\" value=\"London\"><br><br>\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                + "        </form> \n"
                + "        <div id=\"getrespmsg\"></div>\n"
                + "\n"
                + "        <script>\n"
                + "            function loadGetMsg() {\n"
                + "                let nameVar = document.getElementById(\"city\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/consulta?lugar=\"+nameVar);\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\n"
                + "\n"
                + "    </body>\n"
                + "</html>";
        return outputLine;
    }
    
    public static String weatherQuery(Socket client, String city){
        try{
            PrintWriter out = new PrintWriter(client.getOutputStream());
            String outputline;
            outputline = HTTP_HEADER + HttpWeatherConnection.getWeather(city);
            return outputline;
        } catch (IOException ex) {
            return ERROR_HEADER;
        }
    }
}
