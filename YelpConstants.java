package technologies.chatbot.ai.simi.com.simisemanticsearches;


import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Location;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class YelpConstants {

    public static final String BEARER_TOKEN = "tZN2DClnkt5aA81_xEXUegZ7z1sKHL82P1NNQZ-EygfvDKc4cBAOX0C4ZHLWLGY87nIgRriNJ-GnIzi3pFCz1R14Vl8H-GwcmG01AlzzUGv-sGeUSTcCu41kCirPW3Yx";
    public static final String YELP_BASE_URL = "https://api.yelp.com/v3/businesses/search?location=indianapolis&term=food";
    public static final String YELP_LOCATION_QUERY_PARAM = "location";
    public static String printThis = "";





    public static ArrayList<String> runSearch(String typeQuestion, String location)throws java.io.IOException{
        //LocationManager lm = new (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
        YelpFusionApi yelpFusionApi = apiFactory.createAPI(BEARER_TOKEN);
        HashMap<String, String> params = new HashMap<>();
        params.put("term", typeQuestion);
        params.put("location", location);
        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
        Response<SearchResponse> response = call.execute();
        int howMany = response.body().getTotal();
        ArrayList<String> entities = new ArrayList<String>();
        System.out.println("There are this many entitities: " + howMany);
        if(howMany > 0){
        for(int x = 0; x < howMany; x++) {
            try {
                String postResults = response.body().getBusinesses().get(x).getName();
                String loc = response.body().getBusinesses().get(x).getLocation().getAddress1();
                String num = response.body().getBusinesses().get(x).getPhone();
                String rating = String.valueOf(response.body().getBusinesses().get(x).getRating());
                System.out.println(postResults);
                System.err.println(postResults);
                if(postResults == null){
                    continue;
                }
                System.err.println("Size of results is: " + howMany);
                entities.add("Name: " + postResults + ". Location: " + loc + ". Phone Number: " + num + ". Rating: " + rating);
                printThis = response.toString();
                if(entities.size() > 19){
                    break;
                }
            } catch (Exception e) {
                System.err.println("Index out of range possibly");
            }
        }
        }
        return entities;
    }
}
