package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.content.Context;
import android.net.ConnectivityManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URL;
import java.util.Date;

/*
The purpose of this class is to send a weather request to the weather API and return a JSON Object.
I can work with the JSON object to return results in the form of Strings to the user who sends
the request about the weather.

TODO: Check to see if the weather API can search based on longitude and latitude so that the user
can make a request such as ("What is the weather like in myHomeTown", or "should I wear a coat").
 */

public class CheckWeather {

     public static boolean isNetworkAvailable(Context context){
         //Method that checks to see if there is a valid network connection.
         return((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
     }

     public static String executeQuery(String targetURL){
         //This method will execute the actual query. Enter the String of the "city" as the "targetURL"
         URL url;
         HttpURLConnection connection = null;
         try {
             //Create connection
             url = new URL(targetURL);
             connection = (HttpURLConnection)url.openConnection();
             connection.setRequestProperty("content-type", "application/json; charset=utf-8");
             connection.setRequestProperty("content-language", "en-US");
             connection.setUseCaches(false);
             connection.setDoInput(true);
             connection.setDoOutput(false);
             InputStream is;
             int status = connection.getResponseCode();
             if(status != HttpURLConnection.HTTP_OK){
                 is = connection.getErrorStream();
             }
             else{
                 is = connection.getInputStream();
             }

             BufferedReader rd = new BufferedReader(new InputStreamReader(is));
             String line;
             StringBuffer response = new StringBuffer();
             while((line = rd.readLine()) != null){
                 response.append(line);
                 response.append('\r');
             }
             rd.close();
             return response.toString();
         }
         catch(Exception e){
             e.printStackTrace(System.out);
             return null;
         }

         finally{
             if(connection != null){
                 connection.disconnect();
             }
         }
     }
}
