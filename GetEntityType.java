package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.content.Context;
import android.content.res.AssetManager;
import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import java.io.*;
import java.util.*;
import java.lang.*;

public class GetEntityType {

    public  Map<String, String> entityTypeMap;
    public  String entityName = "";
    public  Context context;
    private static MapDictionary<String> dictionary = null;
    private static final boolean returnAllMatches = true;
    private static final boolean caseSensitive = true;

    public GetEntityType(Context context){
        this.context = context;
    }

    public  void loadMap() throws ClassNotFoundException, IOException {
        //String filePath = context.getFilesDir() + File.separator + "entityTypeMap.ser";
        //File destinationFile = new File(filePath);
        //FileOutputStream outputStream = new FileOutputStream(destinationFile);
        //ObjectOutputStream outObject = new ObjectOutputStream(outputStream);
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("dictSampleTest.ser");
        //outObject.writeObject(inputStream);
        //inputStream.close();
        //outObject.close();
        //FileInputStream fInput = new FileInputStream(filePath);
        ObjectInputStream oInput = new ObjectInputStream(inputStream);
        dictionary = (MapDictionary<String>)oInput.readObject();
        //fInput.close();
        //oInput.close();
    }

    public  String getType(String userQuestion) throws ClassNotFoundException, IOException {
        try {
            loadMap();
            userQuestion = userQuestion.toLowerCase();
            userQuestion = userQuestion.trim();
            userQuestion = userQuestion.replace("?", "");
            userQuestion = userQuestion.replace("'s", "");
            if (userQuestion.contains("which award") || userQuestion.contains("what award")) {
                userQuestion = userQuestion.replace("win", "");
            } else if (userQuestion.contains("what kind of government") || userQuestion.contains("what type of government")) {
                userQuestion = userQuestion.replace("have", "");
            } else if (userQuestion.contains("which country") || userQuestion.contains("what country")) {
                userQuestion = userQuestion.replace("in", "");
            }
            else if (userQuestion.contains("when was") && userQuestion.contains("born")) {
            userQuestion = userQuestion.replace("when was", "");
            userQuestion = userQuestion.replace("born", "");
            }
            else if (userQuestion.contains("children") || userQuestion.contains("child") || userQuestion.contains("kid")) {
                userQuestion = userQuestion.replace("who are", "");
                userQuestion = userQuestion.replace("who is", "");
                userQuestion = userQuestion.replace("kid", "");
                userQuestion = userQuestion.replace("kids", "");
                userQuestion = userQuestion.replace("children", "");
                userQuestion = userQuestion.replace("child", "");
                userQuestion = userQuestion.replace("Who were", "");
            }

            else if (userQuestion.contains("which battles") || userQuestion.contains("what battles")){
                userQuestion = userQuestion.replace("which battles", "");
                userQuestion = userQuestion.replace("what battle", "");
                userQuestion = userQuestion.replace("in", "");
                userQuestion = userQuestion.replace("fight", "");
                userQuestion = userQuestion.replace("was","");
                userQuestion = userQuestion.replace("did", "");
            }

            else if ((userQuestion.contains("who are")) && userQuestion.contains("children") || userQuestion.contains("children") || userQuestion.contains("kids")){
                userQuestion = userQuestion.replace("who are", "");
                userQuestion = userQuestion.replace("children", "");
                userQuestion = userQuestion.replace("kids", "");
            }






            userQuestion = userQuestion.replace("what is the capital of", "");
            userQuestion = userQuestion.replace("?", "");
            userQuestion = userQuestion.replace("where did", "");
            userQuestion = userQuestion.replace("what is the currency of", "");
            userQuestion = userQuestion.replace("which currency does", "");
            userQuestion = userQuestion.replace("when was", "");
            userQuestion = userQuestion.replace("which party was", "");
            userQuestion = userQuestion.replace(".", "");
            userQuestion = userQuestion.replace("how much does", "");
            userQuestion = userQuestion.replace("where was", "");
            userQuestion = userQuestion.replace("military command", "");
            userQuestion = userQuestion.replace("what is the population of", "");
            userQuestion = userQuestion.replace("born", "");
            userQuestion = userQuestion.replace("die", "");
            userQuestion = userQuestion.replace("when did", "");
            userQuestion = userQuestion.replace("what are people from", "");
            userQuestion = userQuestion.replace("called", "");
            userQuestion = userQuestion.replace("who is the president of", "");
            userQuestion = userQuestion.replace("who is the leader of", "");
            userQuestion = userQuestion.replace("who is the prime miniester of", "");
            userQuestion = userQuestion.replace("'s", "");
            userQuestion = userQuestion.replace("who is", "");
            userQuestion = userQuestion.replace("wife", "");
            userQuestion = userQuestion.replace("husband", "");
            userQuestion = userQuestion.replace("married to", "");
            userQuestion = userQuestion.replace("where is", "");
            userQuestion = userQuestion.replace("what kind of leader does", "");
            userQuestion = userQuestion.replace(" have", "");
            userQuestion = userQuestion.replace("what is the long name of", "");
            userQuestion = userQuestion.replace("what is the longname of", "");
            userQuestion = userQuestion.replace("founded", "");
            userQuestion = userQuestion.replace("what is the area of", "");
            userQuestion = userQuestion.replace("what percentage water is", "");
            userQuestion = userQuestion.replace("is water", "");
            userQuestion = userQuestion.replace("what percentage of", "");
            userQuestion = userQuestion.replace("what is the largest city in", "");
            userQuestion = userQuestion.replace("what is the biggest city in", "");
            userQuestion = userQuestion.replace("type of government", "");
            userQuestion = userQuestion.replace("kind of government", "");
            userQuestion = userQuestion.replace("who wrote", "");
            userQuestion = userQuestion.replace("who directed", "");
            userQuestion = userQuestion.replace("who was the director for", "");
            userQuestion = userQuestion.replace("who distributed", "");
            userQuestion = userQuestion.replace("who played in", "");
            userQuestion = userQuestion.replace("who wrote", "");
            userQuestion = userQuestion.replace("who was the distributor", "");
            userQuestion = userQuestion.replace("who wrote the music for", "");
            userQuestion = userQuestion.replace("who composed the music for", "");
            userQuestion = userQuestion.replace("who was the writer for", "");
            userQuestion = userQuestion.replace("how long was", "");
            userQuestion = userQuestion.replace("how long was", "");
            userQuestion = userQuestion.replace("who did the editing for", "");
            userQuestion = userQuestion.replace("who edited", "");
            userQuestion = userQuestion.replace("who was the editor for", "");
            userQuestion = userQuestion.replace("what was the budget for", "");
            userQuestion = userQuestion.replace("how much money", "");
            userQuestion = userQuestion.replace("what was the revenue for", "");
            userQuestion = userQuestion.replace("what was the gross profit for", "");
            userQuestion = userQuestion.replace("what was the profit for", "");
            userQuestion = userQuestion.replace("which team did", "");
            userQuestion = userQuestion.replace("play for", "");
            userQuestion = userQuestion.replace("play on", "");
            userQuestion = userQuestion.replace("which team does", "");
            userQuestion = userQuestion.replace("which teams did", "");
            userQuestion = userQuestion.replace("what team", "");
            userQuestion = userQuestion.replace("which team drafted", "");
            userQuestion = userQuestion.replace("what team drafted", "");
            userQuestion = userQuestion.replace("what number does", "");
            userQuestion = userQuestion.replace("who drafted", "");
            userQuestion = userQuestion.replace("what number", "");
            userQuestion = userQuestion.replace("retire", "");
            userQuestion = userQuestion.replace("start playing", "");
            userQuestion = userQuestion.replace("career begin", "");
            userQuestion = userQuestion.replace("stop playing", "");
            userQuestion = userQuestion.replace("weight", "");
            userQuestion = userQuestion.replace("how much does", "");
            userQuestion = userQuestion.replace("how tall is", "");
            userQuestion = userQuestion.replace("weigh", "");
            userQuestion = userQuestion.replace("which position", "");
            userQuestion = userQuestion.replace("what position", "");
            userQuestion = userQuestion.replace("which award", "");
            userQuestion = userQuestion.replace("what award", "");
            userQuestion = userQuestion.replace("what awards", "");
            userQuestion = userQuestion.replace("which awards", "");
            userQuestion = userQuestion.replace("wear", "");
            userQuestion = userQuestion.replace("which college did", "");
            userQuestion = userQuestion.replace("go to school", "");
            userQuestion = userQuestion.replace("go to college", "");
            userQuestion = userQuestion.replace("go to", "");
            userQuestion = userQuestion.replace("play", "");
            userQuestion = userQuestion.replace("what year was", "");
            userQuestion = userQuestion.replace("drafted", "");
            userQuestion = userQuestion.replace("what is the nickname for", "");
            userQuestion = userQuestion.replace("what is the nickname of", "");
            userQuestion = userQuestion.replace("what is the population for", "");
            userQuestion = userQuestion.replace("what is the metro population of", "");
            userQuestion = userQuestion.replace("who is the mayor of", "");
            userQuestion = userQuestion.replace("what is the twin city for", "");
            userQuestion = userQuestion.replace("what is the twin city of", "");
            userQuestion = userQuestion.replace("what are the twin cities for", "");
            userQuestion = userQuestion.replace("what are the twin cities of", "");
            userQuestion = userQuestion.replace("what type of leader", "");
            userQuestion = userQuestion.replace("who is the leader of", "");
            userQuestion = userQuestion.replace("which kind of leader", "");
            userQuestion = userQuestion.replace("what kind of leader", "");
            userQuestion = userQuestion.replace("which type of leader", "");
            userQuestion = userQuestion.replace("what is the urban population of", "");
            userQuestion = userQuestion.replace("what is the urban population of", "");
            userQuestion = userQuestion.replace("what type of city is", "");
            userQuestion = userQuestion.replace("what type of town", "");
            userQuestion = userQuestion.replace("what is the area code for", "");
            userQuestion = userQuestion.replace("what is the areacode for", "");
            userQuestion = userQuestion.replace("what is the area code of", "");
            userQuestion = userQuestion.replace("what is the areacode of", "");
            userQuestion = userQuestion.replace("what is the postal code for", "");
            userQuestion = userQuestion.replace("what is the postalcode for", "");
            userQuestion = userQuestion.replace("what is the postal code of", "");
            userQuestion = userQuestion.replace("what is the postalcode of", "");
            userQuestion = userQuestion.replace("what kind of government does", "");
            userQuestion = userQuestion.replace("which kind of government does", "");
            userQuestion = userQuestion.replace("what is the metro population for", "");
            userQuestion = userQuestion.replace("what is the metro population of", "");
            userQuestion = userQuestion.replace("what is the total population for", "");
            userQuestion = userQuestion.replace("what is the metro population of", "");
            userQuestion = userQuestion.replace("what is the timezone for", "");
            userQuestion = userQuestion.replace("which timezone is ", "");
            userQuestion = userQuestion.replace("what is the timezone of", "");
            userQuestion = userQuestion.replace("what is the timezone in", "");
            userQuestion = userQuestion.replace("a part of", "");
            userQuestion = userQuestion.replace("which state", "");
            userQuestion = userQuestion.replace("which country", "");
            userQuestion = userQuestion.replace("which region", "");
            userQuestion = userQuestion.replace("which county", "");
            userQuestion = userQuestion.replace("what is the homepage of", "");
            userQuestion = userQuestion.replace("what is the home page of", "");
            userQuestion = userQuestion.replace("what is the homepage for", "");
            userQuestion = userQuestion.replace("what is the home page for", "");
            userQuestion = userQuestion.replace("what is the motto of", "");
            userQuestion = userQuestion.replace("what is the motto of", "");
            userQuestion = userQuestion.replace("what is the language of", "");
            userQuestion = userQuestion.replace("which language do they speak in", "");
            userQuestion = userQuestion.replace("which language do they speak in", "");
            userQuestion = userQuestion.replace("children", "");
            userQuestion = userQuestion.replace("child", "");
            userQuestion = userQuestion.replace("kid", "");
            userQuestion = userQuestion.replace("kids", "");
            userQuestion = userQuestion.replace("which language is spoken in", "");
            userQuestion = userQuestion.replace("what language is spoken in", "");
            userQuestion = userQuestion.replace("what is the total area of", "");
            userQuestion = userQuestion.replace("which country is", "");
            userQuestion = userQuestion.replace("what country is", "");
            userQuestion = userQuestion.replace("tell me about", "");
            userQuestion = userQuestion.replace("occupation", "");
            userQuestion = userQuestion.replace("profession", "");
            userQuestion = userQuestion.replace("vice president", "");
            userQuestion = userQuestion.replace("height", "");
            userQuestion = userQuestion.replace("which round was", "");
            userQuestion = userQuestion.replace("which pick was", "");
            userQuestion = userQuestion.replace("what pick", "");
            userQuestion = userQuestion.replace("played for", "");
            userQuestion = userQuestion.replace("weight", "");
            userQuestion = userQuestion.replace("weigh", "");
            userQuestion = userQuestion.replace("how much", "");
            userQuestion = userQuestion.replace("which teams has", "");
            userQuestion = userQuestion.replace("what teams has", "");
            userQuestion = userQuestion.replace("what are the requirements", "");
            userQuestion = userQuestion.replace("what kind of system", "");
            userQuestion = userQuestion.replace("what type of system", "");
            userQuestion = userQuestion.replace("artist", "");
            userQuestion = userQuestion.replace("director", "");
            userQuestion = userQuestion.replace("directed", "");
            userQuestion = userQuestion.replace("tell me about", "");
            userQuestion = userQuestion.replace("can you tell me about", "");
            userQuestion = userQuestion.replace("to play", "");
            userQuestion = userQuestion.replace("which genre", "");
            userQuestion = userQuestion.replace("what genre", "");
            userQuestion = userQuestion.replace("who created", "");
            userQuestion = userQuestion.replace("who produced", "");
            userQuestion = userQuestion.replace("who developed", "");
            userQuestion = userQuestion.replace("release date", "");
            userQuestion = userQuestion.replace("computing platform", "");
            userQuestion = userQuestion.replace("video game system", "");
            userQuestion = userQuestion.replace("kind of game", "");
            userQuestion = userQuestion.replace("type of game", "");
            userQuestion = userQuestion.replace("who was the composer for", "");
            userQuestion = userQuestion.replace("released", "");
            userQuestion = userQuestion.replace("created", "");
            userQuestion = userQuestion.replace("which languages influenced", "");
            userQuestion = userQuestion.replace("which languages were influenced by", "");
            userQuestion = userQuestion.replace("license", "");
            userQuestion = userQuestion.replace("licencses", "");
            userQuestion = userQuestion.replace("who wrote", "");
            userQuestion = userQuestion.replace("who is the author of", "");
            userQuestion = userQuestion.replace("isbn", "");
            userQuestion = userQuestion.replace("isbn number", "");
            userQuestion = userQuestion.replace("which language", "");
            userQuestion = userQuestion.replace("what language", "");
            userQuestion = userQuestion.replace("written in", "");
            userQuestion = userQuestion.replace("how many pages are in", "");
            userQuestion = userQuestion.replace("what kind of book", "");
            userQuestion = userQuestion.replace("what type of book", "");
            userQuestion = userQuestion.replace("what genre", "");
            userQuestion = userQuestion.replace("which genre", "");
            userQuestion = userQuestion.replace("who published", "");
            userQuestion = userQuestion.replace("who is the publisher for", "");
            userQuestion = userQuestion.replace("what kind of media", "");
            userQuestion = userQuestion.replace("what type of media", "");
            userQuestion = userQuestion.replace("what were the dates of", "");
            userQuestion = userQuestion.replace("what was the outcome of", "");
            userQuestion = userQuestion.replace("what was the result of", "");
            userQuestion = userQuestion.replace("what territory", "");
            userQuestion = userQuestion.replace("which territory", "");
            userQuestion = userQuestion.replace("take place", "");
            userQuestion = userQuestion.replace("fought at", "");
            userQuestion = userQuestion.replace("who fought in", "");
            userQuestion = userQuestion.replace("who was a combatant in", "");
            userQuestion = userQuestion.replace("who were the combatants in", "");
            userQuestion = userQuestion.replace("who was the leader of", "");
            userQuestion = userQuestion.replace("who lead", "");
            userQuestion = userQuestion.replace("who was the commander of", "");
            userQuestion = userQuestion.replace("who were the commanders of", "");
            userQuestion = userQuestion.replace("what kind of artist is", "");
            userQuestion = userQuestion.replace("which type of artist is", "");
            userQuestion = userQuestion.replace("which kind of instrument", "");
            userQuestion = userQuestion.replace("which instrument", "");
            userQuestion = userQuestion.replace("what type of instrument", "");
            userQuestion = userQuestion.replace("what kind of music", "");
            userQuestion = userQuestion.replace("what type of music", "");


            userQuestion = userQuestion.trim();

            ExactDictionaryChunker dictionaryChunker = new ExactDictionaryChunker(dictionary, IndoEuropeanTokenizerFactory.INSTANCE, returnAllMatches, caseSensitive);
            Chunking chunking = dictionaryChunker.chunk(userQuestion);
            ArrayList<String> types = new ArrayList<String>();
            types.add("President");
            types.add("Country");
            //types.add("Film");
            types.add("BasketballPlayer");
            types.add("City");
            types.add("State");
            types.add("AmericanFootballPlayer");
            types.add("Disease");
            types.add("University");
            types.add("Boxer");
            types.add("VideoGame");
            types.add("ProgrammingLanguage");
            types.add("MilitaryConflict");
            types.add("Book");
            types.add("MusicalArtist");
            String queryType = "";
            for (Chunk chunk : chunking.chunkSet()) {
                String type = chunk.type();
                if (types.contains(type)) {
                    int start = chunk.start();
                    int end = chunk.end();
                    entityName = userQuestion.substring(start, end);
                    String[] nameSplitter = entityName.split(" ");
                    queryType = type;
                    System.out.println(entityName + " type: " + type);
                    if(nameSplitter.length >= 2 && (type.equals("President") || type.equals("BasketballPlayer")|| type.equals("AmericanFootballPlayer") || type.equals("Boxer") || type.equals("MusicalArtist") || type.equals("Book") || type.equals("MilitaryConflict"))){
                        break;
                    }
                    else if(nameSplitter.length >= 3 && (types.equals("VideoGame"))){
                        break;
                    }


                }
            }
            System.out.println("This is a " + queryType + " type query");
            return queryType;
        }

			catch(Exception e) {
        return "Error";
    }
}

    public String getEntityName() {
        return entityName;
    }
}
