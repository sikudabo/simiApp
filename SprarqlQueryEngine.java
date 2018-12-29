package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
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
public class SprarqlQueryEngine {
    Model model;
    String queryType;
    String finalQuery;
    String answer = "";
    String[] answers;
    BufferedReader br;
    PrintWriter out;
    Socket clientSocket;
    String entityName;
    String entityType;
    String queryAnswer;
    public static String[] theAnswers = null;

    public SprarqlQueryEngine(Model model, String finalQuery, String queryType, Socket clientSocket, PrintWriter out, BufferedReader br, String entityName, String entityType) {
        this.model = model;
        this.queryType = queryType;
        this.finalQuery = finalQuery;
        this.clientSocket = clientSocket;
        this.br = br;
        this.out = out;
        this.entityName = entityName;
        this.entityType = entityType;
    }




    public String runQuery() throws IOException{



        /*ArrayList<String> answers = new ArrayList<String>();
        String answer = "";



        Query query = QueryFactory.create(finalQuery);

        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        if(!(results.hasNext())) {
            answers.add("I haven't learned how to do that yet!");
            String[] b = new String[answers.size()];
            String[] a = (String[])answers.toArray(b);
            return a;
        }
        else {
            Literal answerLiteral;
            Resource answerResource;
            while(results.hasNext()) {


                QuerySolution soln = results.nextSolution();
                try {
                    answerLiteral = soln.getLiteral(queryType);
                    String answerLiteralString = answerLiteral.toString();
                    String[] answerSplitter = answerLiteralString.split("/");
                    System.out.println(Arrays.toString(answerSplitter));
                    if(queryType.equalsIgnoreCase("servicetartear") || queryType.equalsIgnoreCase("serviceendyear") || queryType.equalsIgnoreCase("activeyearsstartdate") || queryType.equalsIgnoreCase("activeyearsenddate") || queryType.equalsIgnoreCase("birthdate") || queryType.equalsIgnoreCase("deathdate") || queryType.equalsIgnoreCase("termperiod")) {
                        String[] specialSplitter = answerLiteralString.split("^^");
                        String toSplit = specialSplitter[0];
                        String[] splitMore = toSplit.split(">");
                        String formattedSpecial = splitMore[splitMore.length - 1];
                        answer = formattedSpecial.replace("\"", "");
                        answer = answer.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
                        answers.add(answer);
                    }
                    else if(queryType.equals("populationTotal")){
                        try {
                            String[] specialSplitter = answerLiteralString.split("^^");
                            System.out.println(Arrays.toString(specialSplitter));
                            String toSplit = specialSplitter[0];
                            String[] splitMore = toSplit.split(">");
                            String formattedSpecial = splitMore[splitMore.length - 1];
                            answer = formattedSpecial.replace("\"", "");
                            answer = answer.replace("^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger>", "");
                            answers.add(answer);
                        }catch(Exception e){
                            answers.add("Could not find population");
                        }
                    }
                    else if(queryType.equals("weight") || queryType.equals("height")){
                        if(queryType.equals("weight")){
                            try {
                                String[] specialSplitter = answerLiteralString.split("^^");
                                System.out.println(Arrays.toString(specialSplitter));
                                String toSplit = specialSplitter[0];
                                String[] splitMore = toSplit.split(">");
                                String formattedSpecial = splitMore[splitMore.length - 1];
                                answer = formattedSpecial.replace("\"", "");
                                answer = answer.replace("^^http://www.w3.org/2001/XMLSchema#double", "");
                                double num = Double.parseDouble(answer);
                                double weight = num * .0022;
                                String w = String.valueOf(weight);
                                answers.add(w + " pounds");
                            }catch(Exception e){
                                answers.add("Could not calculate weight");
                            }
                        }
                        else if(queryType.equals("height")){
                            //Multiply meters by 3.28084
                            try{
                                String[] specialSplitter = answerLiteralString.split("^^");
                                System.out.println(Arrays.toString(specialSplitter));
                                String toSplit = specialSplitter[0];
                                String[] splitMore = toSplit.split(">");
                                String formattedSpecial = splitMore[splitMore.length - 1];
                                answer = formattedSpecial.replace("\"", "");
                                answer = answer.replace("^^http://www.w3.org/2001/XMLSchema#double", "");
                                System.out.println(answer);
                                double num = Double.parseDouble(answer);
                                double height = num * 3.28084;
                                String h = String.valueOf(height);
                                int addInch = Integer.valueOf(String.valueOf(h.charAt(2))) + 1;
                                String feet = String.valueOf(h.charAt(0));
                                String inches = String.valueOf(addInch);
                                answers.add(feet + " feet " + inches + " inches.");
                            }catch(Exception e){
                                answers.add("Could not calculate height");
                            }
                        }

                        else{
                            answers.add(answerLiteralString);
                        }
                    }

                    else if(queryType.equals("activeYearsStartYear") || queryType.equals("draftYear") || queryType.equals("activeYearsEndYear") || queryType.equals("foundingYear")){
                        String[] specialSplitter = answerLiteralString.split("^^");
                        System.out.println(Arrays.toString(specialSplitter));
                        String toSplit = specialSplitter[0];
                        String[] splitMore = toSplit.split(">");
                        String formattedSpecial = splitMore[splitMore.length - 1];
                        answer = formattedSpecial.replace("\"", "");
                        answer = answer.replace("^^http://www.w3.org/2001/XMLSchema#gDate", "");
                        answers.add(answer);
                    }

                    else if(answerLiteralString.contains("^^<http://www.w3.org/2001/XMLSchema#integer>")){
                        answerLiteralString = answerLiteralString.replace("^^<http://www.w3.org/2001/XMLSchema#integer>", "");
                        answerLiteralString = answerLiteralString.replace("\"", "");
                        answers.add(answerLiteralString);
                    }

                    else if(answerLiteralString.contains("^^<http://www.w3.org/2001/XMLSchema#gYear>")){
                        answerLiteralString = answerLiteralString.replace("^^<http://www.w3.org/2001/XMLSchema#gYear>", "");
                        answerLiteralString = answerLiteralString.replace("\"", "");
                        answers.add(answerLiteralString);
                    }
                    else {
                        String formatOneAnswerLiteral = answerSplitter[answerSplitter.length - 1].replace("_", " ");
                        answer = formatOneAnswerLiteral.replace("@en", "");
                        answers.add(answer);
                    }
                }
                catch(ClassCastException e) {
                    answerResource = soln.getResource(queryType);
                    String answerLiteralString = answerResource.toString();
                    String[] answerSplitter = answerLiteralString.split("/");
                    String formatOneAnswerLiteral = answerSplitter[answerSplitter.length - 1].replace("_", " ");
                    answer = formatOneAnswerLiteral.replace("@en", "");
                    answers.add(answer);
                }
            }
        }
        String[] e = new String[answers.size()];
        String[] g = (String[])answers.toArray(e);
        if(g.length > 1){
            g[g.length - 1] = " and " + g[g.length - 1];
        }
        return g;*/
        CreateSocket2 cs = new CreateSocket2(this, finalQuery, queryType, entityName, entityType);
        cs.execute();

        try {
            Socket sock = cs.get();
        } catch (Exception e) {

        }
            return queryAnswer;
        }



    public class CreateSocket2 extends AsyncTask<Socket, Integer, Socket> {
        SprarqlQueryEngine thisEngine;
        String finalQuery;
        String queryType;
        String entityName;
        String entityType;

        public CreateSocket2(SprarqlQueryEngine thisEngine, String finalQuery, String queryType, String entityName, String entityType){
            this.thisEngine = thisEngine;
            this.finalQuery = finalQuery;
            this.queryType = queryType;
            this.entityName = entityName;
            this.entityType = entityType;
        }

        @Override
        public Socket doInBackground(Socket... params){
            System.out.println("Creating client");
            try {

                System.out.println("Waiting for connection...");
                Socket clientSocket = new Socket("192.168.0.8", 1001);
                System.out.println("Connection made at: " + clientSocket);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String response = "";
                String line;
                System.out.println("Scanning for final query and querytype...");
                ArrayList<String> answers = new ArrayList<String>();
               String query = finalQuery;
               String sendMsg = entityName + " > " + queryType + " > " + entityType;
               while(true) {
                   out.println(sendMsg);
                   System.out.println(sendMsg);
                   System.out.println("Entering while loop");
                   response = br.readLine();
                   System.out.println(response);
                   answers.add(response);
                   String[] e = new String[answers.size()];
                   String[] g = (String[]) answers.toArray(e);
                   thisEngine.queryAnswer = response;
                   break;
               }


            }catch(Exception e){
                e.printStackTrace(System.out);
            }
            return clientSocket;
        }

        @Override
        public void onPreExecute(){

        }

        @Override
        public void onPostExecute(Socket i){
            //progressBar.setVisibility(View.GONE);

        }
    }

}


