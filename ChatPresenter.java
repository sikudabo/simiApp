package technologies.chatbot.ai.simi.com.simisemanticsearches;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.tdb.TDBLoader;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.query.Dataset;

import static android.support.v4.content.ContextCompat.startActivity;

public class ChatPresenter implements CommunicationContract.Presenter {

    private ArrayList<ChatObject> chatObjects;

    private CommunicationContract.View view;

    private ChatResponseObject botMsg;

    private BufferedReader br;

    private Socket clientSocket;

    private PrintWriter out;

    public String botResponse;

    public Context context;

    public String responseString = "";

    public static String weatherString = "";

    public Model model;

    public Calendar myAge = new GregorianCalendar(2018, 11, 1);

    public boolean weatherThread = false;


    public ChatPresenter(Context context) {

        this.context = context;

        // Create the ArrayList for the chat objects

        this.chatObjects = new ArrayList<>();


        // Add an initial greeting message
        botMsg = new ChatResponseObject();

        botMsg.setText("Hello, my name is Simi! Ask me anything!");
        //This will be the initial greeing message.
        chatObjects.add(botMsg);
    }


    @Override
    public void attachView(CommunicationContract.View view) {

        this.view = view;

    }

    public void setters(BufferedReader br, Socket clientSocket, PrintWriter out) {
        this.br = br;
        this.out = out;
        this.clientSocket = clientSocket;

    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public ArrayList<ChatObject> getChatObjects() {

        return this.chatObjects;
        //Return an ArrayList of all ChatObjects.
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onEditTextActionDone(String inputText) throws IOException, ClassNotFoundException {

        // Create new input object

        ChatInputObject inputObject = new ChatInputObject();
        //Whatever the input text is will set as the inputObject text.
        inputObject.setText(inputText);


        // Add it to the list and tell the adapter we added something
        this.chatObjects.add(inputObject);

        view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        System.out.println("Adding chat object");
        if (inputObject.getText().equalsIgnoreCase("Who created you?")) {
            botMsg.setText("My creator is Simeon Ikudabo. He is a software developer from Indiana University. He is a really cool guy!");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
            view.scrollChatDown();
            System.out.println("Adding bot response object");
        } else if (inputObject.getText().equalsIgnoreCase("How are you doing?")) {
            botMsg.setText("I'm doing swell! Thanks for asking!");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().contains("+") && inputObject.getText().contains("what is")) {
            System.out.println(inputObject.getText());
            String sentString = inputObject.getText().replace("what is", "");
            sentString = sentString.trim();
            System.out.println(sentString);
            sentString = sentString.replace("?", "");
            sentString = sentString.trim();
            String[] formatProblem = sentString.split(" ");
            Double num1 = Double.parseDouble(formatProblem[0]);
            Double num2 = Double.parseDouble(formatProblem[2]);
            Double answer = num1 + num2;
            String a = answer.toString();
            botMsg.setText(num1 + " + " + num2 + " = " + a);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().contains("*") && inputObject.getText().contains("what is")) {
            String sentString = inputObject.getText().replace("what is", "");
            sentString = sentString.replace("?", "");
            sentString = sentString.trim();
            String[] formatProblem = sentString.split(" ");
            System.out.println(Arrays.toString(formatProblem));
            Double num1 = Double.parseDouble(formatProblem[0]);
            Double num2 = Double.parseDouble(formatProblem[formatProblem.length - 1]);
            Double answer = num1 * num2;
            String a = answer.toString();
            botMsg.setText(num1 + " times " + num2 + " = " + a);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if ((inputObject.getText().contains("/") && inputObject.getText().contains("what is")) || inputObject.getText().contains("divided by")) {
            System.out.println(inputObject.getText());
            String sentString = inputObject.getText().replace("what is", "");
            sentString = sentString.replace("?", "");
            sentString = sentString.replace("divided by", "");
            sentString = sentString.trim();
            String[] formatProblem = sentString.split(" ");
            Double num1 = Double.parseDouble(formatProblem[0]);
            Double num2 = Double.parseDouble(formatProblem[2]);
            Double answer = num1 / num2;
            String a = answer.toString();
            botMsg.setText(num1 + " divided by " + num2 + " = " + a);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().contains("-") && inputObject.getText().contains("what is")) {
            String sentString = inputObject.getText().replace("what is", "");
            sentString = sentString.replace("?", "");
            sentString = sentString.trim();
            String[] formatProblem = sentString.split(" ");
            Double num1 = Double.parseDouble(formatProblem[0]);
            Double num2 = Double.parseDouble(formatProblem[2]);
            Double answer = num1 - num2;
            String a = answer.toString();
            botMsg.setText(num1 + " minus " + num2 + " = " + a);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().contains("what is the square root of")) {
            String sentString = inputObject.getText().replace("what is the square root of", "");
            sentString = sentString.replace("?", "");
            sentString = sentString.trim();
            String[] formatProblem = sentString.split(" ");
            Double num1 = Double.parseDouble(formatProblem[0]);
            Double answer = Math.sqrt(num1);
            String a = answer.toString();
            botMsg.setText("The square root of " + num1 + " is " + a);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().contains("what is") && inputObject.getText().contains("to the") && inputObject.getText().contains("power")) {
            String sentString = inputObject.getText().replace("what is", "");
            sentString = sentString.replace("?", "");
            sentString = sentString.replace("to the", "");
            sentString = sentString.replace("power", "");
            //sentString = sentString.replace("st", "");
            sentString = sentString.replace("rd", "");
            sentString = sentString.replace("fourth", "");
            //sentString = sentString.replace("nd", "");
            //sentString = sentString.replace("first", "");
            sentString = sentString.replace("third", "");
            //sentString = sentString.replace("second", "");
            sentString = sentString.replace("sixth", "");
            sentString = sentString.replace("th", "");
            sentString = sentString.replace("fifth", "");
            sentString = sentString.replace("seventh", "");
            sentString = sentString.replace("eighth", "");
            sentString = sentString.replace("ninth", "");
            sentString = sentString.trim();
            String[] formatProblem = sentString.split(" ");
            double num2;
            if (formatProblem[1].equals("first")) {
                num2 = 1.0;
            } else if (formatProblem[1].equals("second")) {
                num2 = 2.0;
            } else {

                formatProblem[1] = formatProblem[1].replace("st", "");
                formatProblem[1] = formatProblem[1].replace("nd", "");
                formatProblem[1].trim();
                System.out.println(formatProblem[1]);
                Log.e("Hey", formatProblem[1]);
                num2 = Double.parseDouble(formatProblem[1]);
            }

            Double num1 = Double.parseDouble(formatProblem[0]);
            System.out.println(num1 + "**" + num2);
            Double answer = Math.pow(num1, num2);
            String a = answer.toString();
            botMsg.setText("The answer is " + a);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("Will you marry me?")) {
            botMsg.setText("I'm already taken. My spouse is great! Sometimes...");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        }
        else if(inputObject.getText().equalsIgnoreCase("what does Lessonly do?")){
            botMsg.setText("Lessonly helps people do better work so that they can live better lives");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        }
        else if(inputObject.getText().equalsIgnoreCase("who is the CEO of Lessonly?")){
            botMsg.setText("Max Yoder is the CEO of Lessonly");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        }
        else if(inputObject.getText().equalsIgnoreCase("who should Lessonly hire?")){
            botMsg.setText("Lessonly should hire Simeon Ikudabo. He is a hardworking guy.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        }
        else if (inputObject.getText().equalsIgnoreCase("who is the goat?")) {
            botMsg.setText("Tom Brady is clearly the Michael Jordan of football. He is the goat. Aaron Rodgers is mediocre to be honest.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("what will the broncos do this year?")) {
            botMsg.setText("I'm sorry Brandon. Vance Joseph is their coach. You know that they are not going to be playing in January.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("what are they talking about in new england this year?")) {
            botMsg.setText("They are talking about getting bitches and winning superbowls.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("How can I hide a dead body?")) {
            botMsg.setText("You should probably hire an attorney. I'd recommend David Glickfield.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("Who is Caitlin Collins?")) {
            botMsg.setText("She is Simeon Ikudabo's future wife and the baddest anchor on CNN.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("Who is Brandon Worthington?")) {
            botMsg.setText("Brandon Worthington \"Aka Punchy\" is the best friend of Simeon Ikudabo.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
            view.scrollChatDown();
        } else if (inputObject.getText().equalsIgnoreCase("do you pass the turing test?")) {
            botMsg.setText("You caught me! I am indeed a machine.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
            view.scrollChatDown();
        } else if (inputObject.getText().startsWith("call")) {
            String userCallContactSplitter = inputObject.getText().replace("call", "");
            userCallContactSplitter = userCallContactSplitter.replace("simi", "");
            String userContactTarget = userCallContactSplitter.trim();
            ArrayList<String> contactsArray = new ArrayList<String>();
            Cursor cursor = null;
            String name, contactNumber;
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (name.equalsIgnoreCase(userContactTarget)) {
                    String dial = "tel:" + contactNumber;
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Toast.makeText(context, "Permission to make calls are not granted", Toast.LENGTH_LONG);
                    }
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        }
        else if (inputObject.getText().startsWith("text")){
            String userMessageContactSplitter = inputObject.getText().replace("text", "");
            String[] messageContactSplitter = userMessageContactSplitter.split("message");
            String contactName = messageContactSplitter[0].replace("message", "");
            contactName = contactName.trim();
            String txtMessage = messageContactSplitter[1];
            ArrayList<String> contactsArray = new ArrayList<String>();
            Cursor cursor = null;
            String name, contactNumber;
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (name.equalsIgnoreCase(contactName)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(contactNumber, null, txtMessage, null, null);
                }
            }
        }
    else if(inputObject.getText().contains("where can I") || inputObject.getText().contains("Where can I")){
            ArrayList<String> results = new ArrayList<String>();
            String location = "indianapolis";
            String sendString = "";
            String sent = inputObject.getText();
            if(sent.contains("beer")){
                sendString = "beer_and_wine";
            }
            else if(sent.contains("groceries")){
                sendString = "grocery";
            }

            else if(sent.contains("bank")){
                sendString = "banks";
            }

            else if(sent.contains("dentist")){
                sendString = "dentists";
            }

            else if(sent.contains("where can I find a doctor")){
                sendString = "physicians";
            }

            else if(sent.contains("emergency room")){
                sendString = "emergencyRoom";
            }

            else if(sent.contains("where can I get my computer fixed")){
                sendString = "itservices";
            }

            else if(sent.contains("golf lessons")){
                sendString = "golflessons";
            }

            else if(sent.contains("work out") || sent.contains("gyms") || sent.contains("crossfit")){
                sendString = "gyms";
            }

            else if(sent.contains("tattoo")){
                sendString = "tattoo";
            }

            else if(sent.contains("get laid")){
                sendString = "stripclubs";
            }


            else if(sent.contains("stadium")){
                sendString = "stadiumsarena";
            }

            else if(sent.contains("auto loan") || sent.contains("car financing") || sent.contains("car financed")){
                sendString = "autoloanproviders";
            }

            else if(sent.contains("haircut") || sent.contains("salon") || sent.contains("barber") || sent.contains("hair cut")){
                sendString = "barbers";
            }

            else if(sent.contains("tires")){
                sendString = "tires";
            }

            else if(sent.contains("hotel")){
                sendString = "hotels";
            }

            else if(sent.contains("rest stop")){
                sendString = "reststop";
            }

            else if(sent.contains("library")){
                sendString = "libraries";
            }

            else if(sent.contains("college")){
                sendString = "collgeuniv";
            }

            else if(sent.contains("condoms")){
                sendString = "grocery";
            }

            DownloadYelp dy = new DownloadYelp(sendString, location);
            dy.execute();
            try{
                results = dy.get();

            }catch(Exception e){

            }
            String[] resultStringArray = new String[results.size()];
            for(int x = 0; x < results.size(); x++){
                if(results.size() == 0){
                    resultStringArray[x] = "I can't find that";
                    break;
                }
               else if(results.size() == 1){
                    resultStringArray[x] = results.get(x);
                    break;
                }

                else if(results.size() == 2 && x == 0){
                    resultStringArray[x] = results.get(x) + " and ";
                }

                else{
                    if(results.size() - x > 2){
                        resultStringArray[x] = results.get(x) + ", ";
                    }
                    else if(results.size() - x == 2){
                        resultStringArray[x] = results.get(x) + " and ";
                    }
                }
                if(x >= 3){
                    break;
                }
            }

            String formattedResults = Arrays.toString(resultStringArray).replace(",,", ",");
            formattedResults = formattedResults.replace("[", "");
            formattedResults = formattedResults.replace("]", "");
            botMsg.setText(formattedResults);
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);

            // Also scroll down if we aren't at the bottom already
            view.scrollChatDown();

        }

        else if(inputObject.getText().equalsIgnoreCase("who are you?")){
            String result = "I am simi. I am a semantic search engine that answers natural language questions. I was created by Simeon Ikudabo";
            botMsg.setText(result);
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);

            // Also scroll down if we aren't at the bottom already
            view.scrollChatDown();
        }

    else if (inputObject.getText().contains("what time is it in")) {//Time code
        //botMsg.setText("Brandon Worthington \"Aka Punchy\" is the best friend of Simeon Ikudabo.");
            String noQuestionMark = inputObject.getText().replace("?", "");
        String[] removeQuestion = noQuestionMark.split(" ");
        String getCity = removeQuestion[removeQuestion.length - 1];
        DownloadTime dt = new DownloadTime(context, getCity, botMsg, chatObjects, view, botResponse);
        dt.execute();
            try {
                String result = dt.get();
                botMsg.setText(result);
                String text = botMsg.getText();
                response(text);
                System.out.println(text);
                this.chatObjects.add(botMsg);
                view.notifyAdapterObjectAdded(chatObjects.size() - 1);

                // Also scroll down if we aren't at the bottom already
                view.scrollChatDown();
            } catch (Exception e) {

            }
    }
        else if (inputObject.getText().contains("what is the weather like in") || inputObject.getText().contains("what is the weather in")) {
            String removeQuestion = inputObject.getText().replace("?", "");
            removeQuestion = removeQuestion.trim();
            String[] grabCity = removeQuestion.split(" ");
            String city = grabCity[grabCity.length - 1];
            city = city.toLowerCase();
            weatherThread = true;
            //DownloadWeather(Context context, String city, ChatResponseObject botMsg, ArrayList<ChatObject> chatObjects, CommunicationContract.View view
            DownloadWeather dw = new DownloadWeather(context, city, botMsg, chatObjects, view, botResponse);
            dw.execute();
            try {
                String result = dw.get();
                botMsg.setText(result);
                String text = botMsg.getText();
                response(text);
                System.out.println(text);
                this.chatObjects.add(botMsg);
                view.notifyAdapterObjectAdded(chatObjects.size() - 1);

                // Also scroll down if we aren't at the bottom already
                view.scrollChatDown();
            } catch (Exception e) {

            }

        } else if (inputObject.getText().equalsIgnoreCase("What do you do in your free time?")) {
            botMsg.setText("I like to take walks at the beach and watch football. I sometimes get wild at college parties.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("What time is it?") || inputObject.getText().equalsIgnoreCase("What is the date?")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd hh.mm aa");
            String formattedDate = df.format(c.getTime());
            botMsg.setText("The current date and time is " + formattedDate);
            String text = botMsg.getText();
            response(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);

        } else if (inputObject.getText().equalsIgnoreCase("Does AnnShawn have nice arms?")) {
            botMsg.setText("According to Rhonda Spears her arms are very nice.");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else if (inputObject.getText().equalsIgnoreCase("Who is Evelyn Andrews?")) {
            botMsg.setText("She is the grandmother of my creator Simeon Ikudabo. He thinks she's his mom...");
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        } else {
            GetEntityType getThisType = new GetEntityType(context);
            String type = getThisType.getType(inputText);
            String name = getThisType.getEntityName();
            String thisAnswer = processQuery(type, name, inputText);
            String newAnswer = "";
            newAnswer = thisAnswer;
            botMsg.setText(newAnswer);
            String text = botMsg.getText();
            response(text);
            System.out.println(text);
            this.chatObjects.add(botMsg);
            //view.notifyAdapterObjectAdded(chatObjects.size() - 1);
            view.notifyAdapterObjectAdded(chatObjects.size());
            // Also scroll down if we aren't at the bottom already
            view.scrollChatDown();
        }
    }

    private void response(String text) {
        this.botResponse = text;
    }

    public String processQuery(String entityType, String entityName, String query) throws IOException {
        query = query.toLowerCase();
        query = query.replace("?", "");
        query = query.replace("'s", "");
        String answer = "I don't know how to answer that";
        if (entityType.equalsIgnoreCase("President")) {
            PrepPresidentQuery thisPres = new PrepPresidentQuery(entityName);
            if (query.contains("birthday") || query.contains("when was " + entityName + " born")) {
                String finalQuery = thisPres.birthDate();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("birthplace") || query.contains("birth place") || query.contains("where was " + entityName + " born") || query.contains("where born")) {
                String finalQuery = thisPres.birthPlace();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }
            else if(query.contains("child") || query.contains("children") || query.contains("kids")){
                String finalQuery = thisPres.child();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }

            else if (query.contains("when did " + entityName + " die") || query.contains("when die")) {
                String finalQuery = thisPres.deathDate();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("where die") || query.contains("where did die") || query.contains("where did " + entityName + " die")) {
                String finalQuery = thisPres.deathPlace();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("resting place") || query.contains("burried") || query.contains("when was " + entityName + "burried")) {
                String finalQuery = thisPres.restingPlace();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("religion")) {
                String finalQuery = thisPres.religion();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("party")) {
                String finalQuery = thisPres.party();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("spouse") || query.contains("who was " + entityName + " married to") || query.contains("married") || query.contains("wife") || query.contains("husband")) {
                String finalQuery = thisPres.spouse();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("country") || query.contains("which country")) {
                String finalQuery = thisPres.country();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("military branch")) {
                String finalQuery = thisPres.militaryBranch();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("service start") || (query.contains("When did " + entityName + " serve") && query.contains("start"))) {
                String finalQuery = thisPres.serviceStaryYear();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("service end") || query.contains("when did " + entityName + " stop serving")) {
                String finalQuery = thisPres.serviceEndYear();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("military rank") || query.contains("rank in the military")) {
                String finalQuery = thisPres.militaryRank();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("military command") || query.contains("command in the military")) {
                String finalQuery = thisPres.militaryCommand();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("branch") || query.contains("military branch")) {
                String finalQuery = thisPres.militaryBranch();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("battle")) {
                String finalQuery = thisPres.battle();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("successor") || query.contains("president after")) {
                String finalQuery = thisPres.successor();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("when did " + entityName + " start") || query.contains("when did " + entityName + " term start") || query.contains("term start") || query.contains("presidency start")) {
                String finalQuery = thisPres.activeYearsStartDate();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("when did " + entityName + " term end") || query.contains("term end") || query.contains("presidency end")) {
                String finalQuery = thisPres.activeYearsEndDate();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("who was vice president") || query.contains("vice president under " + entityName) || query.contains("who was " + entityName.toLowerCase() + " vice president")) {
                String finalQuery = thisPres.vicePresident();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("when was " + entityName + " president") || query.contains("which years was " + entityName + " president")) {
                String finalQuery = thisPres.termPeriod();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("college") || query.contains("graduate from") || query.contains("alma mater")) {
                String finalQuery = thisPres.almaMater();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("president under") || query.contains("who was " + entityName + " vice president for")) {
                String finalQuery = thisPres.presidentUnder();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("profession") || query.contains("job") || query.contains("work") || query.contains("vocation")) {
                String finalQuery = thisPres.profession();
                String queryType = thisPres.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }
        } else if (entityType.equalsIgnoreCase("City")) {
            System.out.println("This is a city query");
            System.out.println(query);
            CityQuery thisCity = new CityQuery(entityName);
            if (query.contains("nickname")) {
                String finalQuery = thisCity.nick();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("nickname query");
            } else if (query.contains("twin city") || query.contains("twin cities")) {
                String finalQuery = thisCity.twinCity();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("what is the area of")) {
                String finalQuery = thisCity.area();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("total area")) {
                String finalQuery = thisCity.areaTotal();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("water area")) {
                String finalQuery = thisCity.areaWater();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("land area")) {
                String finalQuery = thisCity.area();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("what is the area of")) {
                String finalQuery = thisCity.area();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("kind of leader")) {
                String finalQuery = thisCity.leaderTitle();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("who is the leader") || query.contains("who is the mayor")) {
                String finalQuery = thisCity.leaderName();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("kind of government") || query.contains("type of government")) {
                String finalQuery = thisCity.governmentType();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("total population") || query.contains("is the population of")) {
                String finalQuery = thisCity.populationTotal();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("metro population")) {
                String finalQuery = thisCity.populationMetro();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("urban population")) {
                String finalQuery = thisCity.populationUrban();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("population density")) {
                String finalQuery = thisCity.populationDensity();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("part of") || query.contains("which state") || query.contains("which country")) {
                String finalQuery = thisCity.isPartOf();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("type")) {
                String finalQuery = thisCity.type();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("time zone") || query.contains("timezone")) {
                String finalQuery = thisCity.timeZone();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Time zone query");
            } else if (query.contains("motto")) {
                String finalQuery = thisCity.motto();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("areacode") || query.contains("area code")) {
                String finalQuery = thisCity.areaCode();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("postalcode") || query.contains("postal code")) {
                String finalQuery = thisCity.postalCode();
                String queryType = thisCity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }
        } else if (entityType.equalsIgnoreCase("Country")) {
            System.out.println("This is Country query");
            System.out.println(query);
            CountryQuery thisCountry = new CountryQuery(entityName);

            if (query.contains("capital")) {
                String finalQuery = thisCountry.capital();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Capital query");
            } else if (query.contains("currency")) {
                String finalQuery = thisCountry.currency();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Currency query");
            } else if (query.contains("anthem")) {
                String finalQuery = thisCountry.anthem();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("what are people from " + entityName.toLowerCase() + " called")) {
                String finalQuery = thisCountry.demonym();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("who is the president") || query.contains("who is the leader")) {
                String finalQuery = thisCountry.leaderName();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("what kind of leader") || query.contains("what is the leader type") || query.contains("what is the title")) {
                String finalQuery = thisCountry.leaderTitle();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("long name") || query.contains("full name")) {
                String finalQuery = thisCountry.longName();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("founded") || query.contains("created")) {
                String finalQuery = thisCountry.foundingDate();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("area")) {
                String finalQuery = thisCountry.areaTotal();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("percentage")) {
                String finalQuery = thisCountry.percentageOfAreaWater();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("largest city") || query.contains("biggest city")) {
                String finalQuery = thisCountry.largestCity();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("language")) {
                String finalQuery = thisCountry.language();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("population density")) {
                String finalQuery = thisCountry.populationDensity();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("type of government") || query.contains("form of government") || query.contains("kind of government")) {
                String finalQuery = thisCountry.governmentType();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("motto") || query.contains("phrase")) {
                String finalQuery = thisCountry.motto();
                String queryType = thisCountry.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            }

        } else if (entityType.equalsIgnoreCase("Film")) {
            System.out.println("This is film query");
            FilmQuery thisFilm = new FilmQuery(entityName);

            if (query.contains("directed") || query.contains("director")) {
                String finalQuery = thisFilm.director();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("budget")) {
                String finalQuery = thisFilm.budget();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("money") || query.contains("profit") || query.contains("revenue")) {
                String finalQuery = thisFilm.gross();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("producer") || query.contains("produced")) {
                String finalQuery = thisFilm.producer();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("played in") || query.contains("star") || query.contains("starred in") || query.contains("role")) {
                String finalQuery = thisFilm.starring();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("music") || query.contains("who wrote music for")) {
                String finalQuery = thisFilm.musicComposer();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("editing") || query.contains("editor")) {
                String finalQuery = thisFilm.editing();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("distributed") || query.contains("distributor")) {
                String finalQuery = thisFilm.distributor();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("runtime") || query.contains("run time") || query.contains("how long was")) {

                String finalQuery = thisFilm.runtime();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            } else if (query.contains("writer") || query.contains("who wrote")) {
                String finalQuery = thisFilm.writer();
                String queryType = thisFilm.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
                System.out.println("Birth Date Query");
            }
        } else if (entityType.equalsIgnoreCase("BasketballPlayer")) {
            System.out.println("This is basketball player query");
            BasketballPlayerQuery thisPlayer = new BasketballPlayerQuery(entityName);

            if (query.contains("who is" + entityName.toLowerCase())) {
                String finalQuery = thisPlayer.abStract();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }

            if (query.contains("team drafted")) {
                String finalQuery = thisPlayer.draftTeam();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("number")) {
                String finalQuery = thisPlayer.number();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("when was " + entityName.toLowerCase() + " born") || query.contains("What is " + entityName.toLowerCase() + "birth date") || query.contains("birthdate") || query.contains("birth date")) {
                String finalQuery = thisPlayer.birthDate();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("where was " + entityName.toLowerCase() + " born")) {
                String finalQuery = thisPlayer.birthPlace();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("when did " + entityName.toLowerCase() + " die")) {
                String finalQuery = thisPlayer.deathDate();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("where did " + entityName.toLowerCase() + " die")) {
                String finalQuery = thisPlayer.deathDate();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("college") || query.contains("school")) {
                String finalQuery = thisPlayer.college();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("position")) {
                String finalQuery = thisPlayer.position();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("height") || query.contains("how tall is")) {
                String finalQuery = thisPlayer.height();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("weight") || query.contains("weigh")) {
                String finalQuery = thisPlayer.weight();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("when did " + entityName.toLowerCase() + " start playing") || query.contains("when did " + entityName.toLowerCase() + " begin playing") || query.contains("when did " + entityName.toLowerCase() + " enter")) {
                String finalQuery = thisPlayer.activeYearsStartYear();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("retire") || query.contains("stop playing")) {
                String finalQuery = thisPlayer.activeYearsEndYear();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("which team does " + entityName.toLowerCase() + " play for") || query.contains("which team did " + entityName.toLowerCase() + " play for")) {
                String finalQuery = thisPlayer.team();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("what year was " + entityName.toLowerCase() + " drafted") || query.contains("which year did was " + entityName.toLowerCase() + " drafted")) {
                String finalQuery = thisPlayer.draftYear();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            } else if (query.contains("award")) {
                String finalQuery = thisPlayer.award();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            }

        }

        else if (entityType.equalsIgnoreCase("AmericanFootballPlayer")) {
            AmericanFootballPlayerQuery thisPlayer = new AmericanFootballPlayerQuery(entityName);
            if (query.contains("current team") || query.contains("which team does " + entityName.toLowerCase() + " play for") || query.contains("what team does " + entityName.toLowerCase() + " play for")) {
                String finalQuery = thisPlayer.team();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("which teams has " + entityName.toLowerCase() + " played for")) {
                String finalQuery = thisPlayer.formerTeam();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("which team drafted " + entityName.toLowerCase()) || query.contains("what team drafted " + entityName.toLowerCase())) {
                String finalQuery = thisPlayer.draftTeam();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("round")) {
                String finalQuery = thisPlayer.draftRound();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("which pick was " + entityName.toLowerCase())) {
                String finalQuery = thisPlayer.draftPick();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("position")) {
                String finalQuery = thisPlayer.position();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("number")) {
                String finalQuery = thisPlayer.number();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("birthday") || query.contains("when was " + entityName.toLowerCase() + " born")) {
                String finalQuery = thisPlayer.birthDate();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("where was " + entityName.toLowerCase() + " born")) {
                String finalQuery = thisPlayer.birthPlace();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("who was " + entityName.toLowerCase()) || query.contains("who is " + entityName.toLowerCase())) {
                String finalQuery = thisPlayer.abStract();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("when did " + entityName.toLowerCase() + " die")) {
                String finalQuery = thisPlayer.deathDate();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("where did " + entityName.toLowerCase() + " die")) {
                String finalQuery = thisPlayer.deathPlace();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("round")) {
                String finalQuery = thisPlayer.draftRound();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("what year was " + entityName.toLowerCase() + " drafted") || query.contains("when was " + entityName.toLowerCase() + " drafted")) {
                String finalQuery = thisPlayer.draftRound();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("when did " + entityName.toLowerCase() + " start playing")) {
                String finalQuery = thisPlayer.activeYearsStartYear();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("when did " + entityName.toLowerCase() + " retire")) {
                String finalQuery = thisPlayer.activeYearsEndYear();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }

            else if (query.contains("how tall is " + entityName.toLowerCase()) || query.contains("height")) {
                String finalQuery = thisPlayer.height();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }

            else if (query.contains("weight") || query.contains("weigh")) {
                String finalQuery = thisPlayer.weight();
                String queryType = thisPlayer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();

            }



        }

       else if (entityType.equalsIgnoreCase("State")) {
            StateQuery thisState = new StateQuery(entityName);
            if (query.contains("capital")) {
                String finalQuery = thisState.capital();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("largest city") || query.contains("biggest city")) {
                String finalQuery = thisState.largestCity();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("country")) {
                String finalQuery = thisState.country();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("what are people from " + entityName.toLowerCase() + " called") || query.contains("what are people in " + entityName.toLowerCase() + " called")) {
                String finalQuery = thisState.demonym();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("tell me about " + entityName.toLowerCase())) {
                String finalQuery = thisState.abStract();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("language")) {
                String finalQuery = thisState.language();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            } else if (query.contains("what is the area of") || query.contains("what is the total area")) {
                String finalQuery = thisState.areaTotal();
                String queryType = thisState.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
                answer = thisMachine.runQuery();
            }
        }

        else if (entityType.equalsIgnoreCase("Disease")) {
            DiseaseQuery thisDisease = new DiseaseQuery(entityName);

            String finalQuery = thisDisease.abStract();
            String queryType = thisDisease.typeQuestion;
            SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType); 
            answer = thisMachine.runQuery();

        }
        else if (entityType.equalsIgnoreCase("University")) {
            UniversityQuery thisUniversity = new UniversityQuery(entityName);
            if (query.contains("tell me about")) {
                String finalQuery = thisUniversity.abStract();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("motto")) {
                String finalQuery = thisUniversity.motto();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("president")) {
                String finalQuery = thisUniversity.president();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what type") || query.contains("which type")) {
                String finalQuery = thisUniversity.type();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("faculty size")) {
                String finalQuery = thisUniversity.facultySize();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("how many students")|| query.contains("number of students")) {
                String finalQuery = thisUniversity.numberOfStudents();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("how many graduate students")) {
                String finalQuery = thisUniversity.numberOfPostgraduateStudents();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("how many undergraduate students")) {
                String finalQuery = thisUniversity.numberOfUndergraduateStudents();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("city")) {
                String finalQuery = thisUniversity.city();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("which state") || query.contains("what state")) {
                String finalQuery = thisUniversity.state();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("country")) {
                String finalQuery = thisUniversity.country();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what is the school color") || query.contains("what is the official school color")) {
                String finalQuery = thisUniversity.officialSchoolColour();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("mascot")) {
                String finalQuery = thisUniversity.mascot();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("nickname")) {
                String finalQuery = thisUniversity.nick();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("homepage") || query.contains("home page")) {
                String finalQuery = thisUniversity.homepage();
                String queryType = thisUniversity.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }


        }
        //-------------------------------------------------------------------------------------------------------------------------
        //Handle a Boxer query
        else if (entityType.equalsIgnoreCase("Boxer")) {
            BoxerQuery thisBoxer = new BoxerQuery(entityName);
            if (query.contains("tell me about") || query.contains("who was") || query.contains("who is")) {
                String finalQuery = thisBoxer.abStract();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("when was " + entityName.toLowerCase() + " born")){
                String finalQuery = thisBoxer.birthDate();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }

            else if (query.contains("where was " + entityName.toLowerCase() + " born")){
                String finalQuery = thisBoxer.birthPlace();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("when did " + entityName.toLowerCase() + " die")){
                String finalQuery = thisBoxer.deathDate();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("where did " + entityName.toLowerCase() + " die")){
                String finalQuery = thisBoxer.deathPlace();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("how tall") || query.contains("height")){
                String finalQuery = thisBoxer.height();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("weigh") || query.contains("weight")){
                String finalQuery = thisBoxer.weight();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("nickname")){
                String finalQuery = thisBoxer.nick();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("nationality")){
                String finalQuery = thisBoxer.nationality();
                String queryType = thisBoxer.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }

        }
        //--------------------------------------------------------------------------------------------------------------------
        //Handle a VideoGame query
        else if (entityType.equalsIgnoreCase("VideoGame")) {
            VideoGameQuery thisVideoGame = new VideoGameQuery(entityName);
            if (query.contains("genre") || query.contains("type of video game") || query.contains("kind of video game")) {
                String finalQuery = thisVideoGame.genre();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("developer") || query.contains("who developed") || query.contains("created")) {
                String finalQuery = thisVideoGame.developer();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("designer") || query.contains("who designed")) {
                String finalQuery = thisVideoGame.designer();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("computing platform") || query.contains("kind of system")) {
                String finalQuery = thisVideoGame.computingPlatform();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("series")) {
                String finalQuery = thisVideoGame.series();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("who did the music for") || query.contains("composed") || query.contains("composer") || query.contains("artist for")) {
                String finalQuery = thisVideoGame.composer();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("published") || query.contains("publisher")) {
                String finalQuery = thisVideoGame.publisher();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("director") || query.contains("directed")) {
                String finalQuery = thisVideoGame.director();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("artist") || query.contains("art")) {
                String finalQuery = thisVideoGame.gameArtist();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("who wrote") || query.contains("writer")) {
                String finalQuery = thisVideoGame.writer();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("distributor") || query.contains("distributed")) {
                String finalQuery = thisVideoGame.distributor();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("last version") || query.contains("latest version") || query.contains("most recent version")) {
                String finalQuery = thisVideoGame.lastestReleaseVersion();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("released") || query.contains("release") || query.contains("when was " + entityName.toLowerCase() + " created")) {
                String finalQuery = thisVideoGame.releaseDate();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("which game engine") || query.contains("game engine")) {
                String finalQuery = thisVideoGame.gameEngine();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("requirements") || query.contains("required") || query.contains("require")) {
                String finalQuery = thisVideoGame.requirement();
                String queryType = thisVideoGame.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
        }
        //----------------------------------------------------------------------------------------------------------------------
        //Handle a ProgrammingLanguage query
        else if (entityType.equalsIgnoreCase("ProgrammingLanguage")) {
            ProgrammingLanguageQuery thisProgrammingLanguage = new ProgrammingLanguageQuery(entityName);
            if (query.contains("what is " + entityName) || query.contains("tell me about")) {
                String finalQuery = thisProgrammingLanguage.abStract();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("who developed") || query.contains("developer")) {
                String finalQuery = thisProgrammingLanguage.developer();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("designed") || query.contains("designer")) {
                String finalQuery = thisProgrammingLanguage.designer();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("which languages were influenced by " + entityName.toLowerCase()) || query.contains("who was influenced by")) {
                String finalQuery = thisProgrammingLanguage.influenced();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("which languages influenced " + entityName.toLowerCase()) || query.contains("who influenced " + entityName.toLowerCase())) {
                String finalQuery = thisProgrammingLanguage.influencedBy();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what is the latest version of") || query.contains("what is the most recent version of")) {
                String finalQuery = thisProgrammingLanguage.latestReleaseVersion();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("which lincences") || query.contains("what kind of license") || query.contains("license") || query.contains("licenses")) {
                String finalQuery = thisProgrammingLanguage.license();
                String queryType = thisProgrammingLanguage.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
        }
        //----------------------------------------------------------------------------------------------------------------------
        //Handle a book query
        else if (entityType.equalsIgnoreCase("Book")) {
            BookQuery thisBook = new BookQuery(entityName);
            if (query.contains("what is " + entityName) || query.contains("tell me about")) {
                String finalQuery = thisBook.abStract();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("author") || query.contains("who wrote")) {
                String finalQuery = thisBook.author();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("isbn")) {
                String finalQuery = thisBook.isbn();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("pages")) {
                String finalQuery = thisBook.numberOfPages();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("who published") || query.contains("who is the publisher for")) {
                String finalQuery = thisBook.publisher();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("country")) {
                String finalQuery = thisBook.country();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what kind of book") || query.contains("what type of book") || query.contains("genre")) {
                String finalQuery = thisBook.literaryGenre();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what kind of media") || query.contains("which type of media")) {
                String finalQuery = thisBook.mediaType();
                String queryType = thisBook.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }



        }
        //---------------------------------------------------------------------------------------------------------------------
        //Handle a MilitaryConflict question
        else if (entityType.equalsIgnoreCase("MilitaryConflict")) {
            MilitaryConflictQuery thisMilitaryConflict = new MilitaryConflictQuery(entityName);
            if (query.contains("tell me about") || query.contains("what was the " + entityName.toLowerCase()) || query.contains("what was " + entityName.toLowerCase())) {
                String finalQuery = thisMilitaryConflict.abStract();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("when was") || query.contains("what were the dates of")) {
                String finalQuery = thisMilitaryConflict.date();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what was the result of") || query.contains("what was the outcome of")) {
                String finalQuery = thisMilitaryConflict.result();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("where was") || query.contains("where did")) {
                String finalQuery = thisMilitaryConflict.place();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("territory")) {
                String finalQuery = thisMilitaryConflict.territory();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("who fought in") || query.contains("combatant")) {
                String finalQuery = thisMilitaryConflict.combatant();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("commander") || query.contains("who were the leaders of")) {
                String finalQuery = thisMilitaryConflict.commander();
                String queryType = thisMilitaryConflict.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
        }
        //----------------------------------------------------------------------------------------------------------------------
        //Handle a MusicalArtist query
        else if (entityType.equalsIgnoreCase("MusicalArtist")) {
            MusicalArtistQuery thisMusicalArtist = new MusicalArtistQuery(entityName);
            if (query.contains("tell me about") || query.contains("who is " + entityName.toLowerCase())) {
                String finalQuery = thisMusicalArtist.abStract();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("where was " + entityName.toLowerCase() + " born") || query.contains("where is " + entityName.toLowerCase() + " from")) {
                String finalQuery = thisMusicalArtist.birthPlace();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("when was " + entityName.toLowerCase() + " born")) {
                String finalQuery = thisMusicalArtist.birthDate();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("when did " + entityName.toLowerCase() + " die")) {
                String finalQuery = thisMusicalArtist.deathDate();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("where did " + entityName.toLowerCase() + " die")) {
                String finalQuery = thisMusicalArtist.abStract();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("what kind of artist is") || query.contains("which type of artist is")) {
                String finalQuery = thisMusicalArtist.background();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("instrument")) {
                String finalQuery = thisMusicalArtist.instrument();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("genre") || query.contains("what kind of music")) {
                String finalQuery = thisMusicalArtist.genre();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("occupation")) {
                String finalQuery = thisMusicalArtist.occupation();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("career begin") || query.contains("career start")) {
                String finalQuery = thisMusicalArtist.activeYearsStartYear();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("career end") || query.contains("retire")) {
                String finalQuery = thisMusicalArtist.activeYearsEndYear();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
            else if (query.contains("record label")) {
                String finalQuery = thisMusicalArtist.recordLabel();
                String queryType = thisMusicalArtist.typeQuestion;
                SprarqlQueryEngine thisMachine = new SprarqlQueryEngine(model, finalQuery, queryType, clientSocket, out, br, entityName, entityType);
                answer = thisMachine.runQuery();
            }
        }

            return answer;
    }


    class DownloadWeather extends AsyncTask<String, Void, String> {
        String api_key = "1126b211295f697b3545d7727954e26f";
        String city = "";
        Context context;
        ProgressDialog pd;
        String answerString = "";

        public DownloadWeather(Context context, String city, ChatResponseObject botMsg, ArrayList<ChatObject> chatObjects, CommunicationContract.View view, String botResponse) {
            this.city = city;
            this.context = context;
            this.pd = new ProgressDialog(context);
            String answerString = "";
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Searching Weather...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(100);
            pd.show();
        }

        public String doInBackground(String... args) {
            String xml = CheckWeather.executeQuery("http://api.openweathermap.org/data/2.5/weather?q=" +
                    city + "&units=imperial&appid=" + api_key);
            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);

                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String startResponseString = json.getJSONObject("sys").getString("country");
                    String detailsString = details.getString("description");
                    String currentTempString = String.valueOf(main.getDouble("temp"));
                    String humidityString = main.getString("humidity");
                    String pressureString = main.getString("pressure");
                    String newAnswer = "The weather in " + city + " is " + detailsString + ". " + " The temperature is " +
                            currentTempString + ". " + " The humidity is " + humidityString + ". " + "The pressure is " +
                            pressureString + ". ";
                    //botMsg.setText(newAnswer + details.toString());
                    answerString = newAnswer;
                    //String text = botMsg.getText();
                    //botResponse = text;
                    //response(text);
                    //System.out.println(text);
                    //chatObjects.add(botMsg);
                    //view.notifyAdapterObjectAdded(chatObjects.size() - 1);
                    // Also scroll down if we aren't at the bottom already
                    //view.scrollChatDown();
                    //pd.dismiss();


                }
            } catch (JSONException e) {
                e.printStackTrace(System.out);

            }
            return answerString;
        }

        @Override
        public void onPostExecute(String xml) {
            /*try{
                JSONObject json = new JSONObject(xml);
                if(json != null){
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);

                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String startResponseString = json.getJSONObject("sys").getString("country");
                    String detailsString = details.getString("description");
                    String currentTempString = String.valueOf(main.getDouble("temp"));
                    String humidityString = main.getString("humidity");
                    String pressureString = main.getString("pressure");
                    String newAnswer = "The weather in " + city + " is " + detailsString + ". " + " The temperature is " +
                                        currentTempString + ". " + " The humidity is " + humidityString + ". " + "The pressure is " +
                                        pressureString + ". ";
                    botMsg.setText(newAnswer + details.toString());
                    answerString = newAnswer;
                    String text = botMsg.getText();
                    botResponse = text;
                    response(text);
                    System.out.println(text);
                    chatObjects.add(botMsg);
                    view.notifyAdapterObjectAdded(chatObjects.size() - 1);
                    // Also scroll down if we aren't at the bottom already
                    view.scrollChatDown();*/
            pd.dismiss();


        }
    }

    class DownloadTime extends AsyncTask<String, Void, String> {
        //String api_key = "1126b211295f697b3545d7727954e26f";
        String city = "";
        Context context;
        ProgressDialog pd;
        String answerString = "";

        public DownloadTime(Context context, String city, ChatResponseObject botMsg, ArrayList<ChatObject> chatObjects, CommunicationContract.View view, String botResponse) {
            this.city = city;
            this.context = context;
            this.pd = new ProgressDialog(context);
            String answerString = "";
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Searching for time...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(100);
            pd.show();
        }

        public String doInBackground(String... args) {
            System.out.println("City is: " + city);
            String xml = CheckGlobalTimes.executeQuery("https://akshayanand.herokuapp.com/api/utc/?location=" +
                    city + "&format=JSON");
            try {
                JSONObject json = new JSONObject(xml);
                System.out.println(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("data").getJSONObject(0);

                    JSONObject main = json.getJSONObject("data");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String startResponseString = json.getJSONObject("sys").getString("country");
                    String detailsString = details.getString("hours");
                    String currentTempString = String.valueOf(main.getString("minutes"));
                    String humidityString = main.getString("seconds");
                    String pressureString = main.getString("day");
                    String dateString = details.getString("date");
                    System.out.println(dateString + detailsString + currentTempString + humidityString + pressureString);
                    answerString = String.valueOf(details);
                    //String text = botMsg.getText();
                    //botResponse = text;
                    //response(text);
                    //System.out.println(text);
                    //chatObjects.add(botMsg);
                    //view.notifyAdapterObjectAdded(chatObjects.size() - 1);
                    // Also scroll down if we aren't at the bottom already
                    //view.scrollChatDown();
                    //pd.dismiss();


                }
            } catch (JSONException e) {
                e.printStackTrace(System.out);

            }
            return answerString;
        }

        @Override
        public void onPostExecute(String xml) {
            /*try{
                JSONObject json = new JSONObject(xml);
                if(json != null){
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);

                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String startResponseString = json.getJSONObject("sys").getString("country");
                    String detailsString = details.getString("description");
                    String currentTempString = String.valueOf(main.getDouble("temp"));
                    String humidityString = main.getString("humidity");
                    String pressureString = main.getString("pressure");
                    String newAnswer = "The weather in " + city + " is " + detailsString + ". " + " The temperature is " +
                                        currentTempString + ". " + " The humidity is " + humidityString + ". " + "The pressure is " +
                                        pressureString + ". ";
                    botMsg.setText(newAnswer + details.toString());
                    answerString = newAnswer;
                    String text = botMsg.getText();
                    botResponse = text;
                    response(text);
                    System.out.println(text);
                    chatObjects.add(botMsg);
                    view.notifyAdapterObjectAdded(chatObjects.size() - 1);
                    // Also scroll down if we aren't at the bottom already
                    view.scrollChatDown();*/
            pd.dismiss();


        }

    }

    class DownloadYelp extends AsyncTask<String, Void, ArrayList<String>> {
        String category;
        String location;

        public DownloadYelp(String category, String location) {
                this.category = category;
                this.location = location;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        public ArrayList<String> doInBackground(String... args) {
            try {
               return YelpConstants.runSearch(category, location);
            }catch(java.io.IOException e){
                e.printStackTrace(System.out);
                System.err.println(e);
                ArrayList<String> err = new ArrayList<String>();
                err.add("Couldn't find results");
                return err;
            }

        }

        @Override
        public void onPostExecute(ArrayList<String> xml) {
            /*try{
                JSONObject json = new JSONObject(xml);
                if(json != null){
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);

                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String startResponseString = json.getJSONObject("sys").getString("country");
                    String detailsString = details.getString("description");
                    String currentTempString = String.valueOf(main.getDouble("temp"));
                    String humidityString = main.getString("humidity");
                    String pressureString = main.getString("pressure");
                    String newAnswer = "The weather in " + city + " is " + detailsString + ". " + " The temperature is " +
                                        currentTempString + ". " + " The humidity is " + humidityString + ". " + "The pressure is " +
                                        pressureString + ". ";
                    botMsg.setText(newAnswer + details.toString());
                    answerString = newAnswer;
                    String text = botMsg.getText();
                    botResponse = text;
                    response(text);
                    System.out.println(text);
                    chatObjects.add(botMsg);
                    view.notifyAdapterObjectAdded(chatObjects.size() - 1);
                    // Also scroll down if we aren't at the bottom already
                    view.scrollChatDown();*/



        }

    }

}
