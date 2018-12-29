package technologies.chatbot.ai.simi.com.simisemanticsearches;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.ActivityNotFoundException;
import android.speech.tts.TextToSpeech;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.arp.impl.RDFXMLParser;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.riot.Lang;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.TDBLoader;
import com.hp.hpl.jena.util.FileManager;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.CountingInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;




public class MainActivity extends AppCompatActivity implements CommunicationContract.View {



    private RecyclerView rvChatList; //This will be the list of messages in the RecyclerView
    private EditText etSearchBox; //This will be the EditText box.
    private ChatAdapter chatAdapter; //Create a new ChatAdapter object.
    private static ChatPresenter presenter; //Create a new ChatPresenter object.
    private ImageButton speakMic;
    private ProgressBar progressBar;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Model model = null;
    TextToSpeech tts;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Set the main layout as the ContentView.
        rvChatList = (RecyclerView) findViewById(R.id.mainRecyclerView);//Initialize the RecyclerView
        etSearchBox = (EditText) findViewById(R.id.edittext_chatbox);//Initialize the edit text object.
        etSearchBox.setOnEditorActionListener(searchBoxListener);
        speakMic = (ImageButton) findViewById(R.id.speakButton);
        //progressBar= findViewById(R.id.progressBar);

        //DownloadModel dm = new DownloadModel(this);
        //dm.execute();
        //model = dm.model;
        //CreateSocket cs = new CreateSocket(this);
        //cs.execute();



        //etSearchBox.setOnEditorActionListener(searchBoxListener); //Create an ActionListener.
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        //String myAnswer = spQuery.runQuery();
        if(model == null){
            System.out.println("Null");
        }
        this.presenter = new ChatPresenter(this);//Initialize the presenter object.

        presenter.attachView(this); //Attach this instance of the view to the presenter.

        speakMic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });



        // Instantiate the adapter and give it the list of chat objects

        this.chatAdapter = new ChatAdapter(presenter.getChatObjects());
        //Create a new adapter object. Pass an ArrayList of ChatObjects as the argument.



        // Set up the RecyclerView with adapter and layout manager

        rvChatList.setAdapter(chatAdapter);
        rvChatList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvChatList.setItemAnimator(new DefaultItemAnimator());
        //Setting the ItemAnimator will animiate the transition each time a new text is added.
        rvChatList.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                        int oldBottom){
                if(bottom < oldBottom){
                    rvChatList.postDelayed(new Runnable(){

                        @Override
                        public void run(){
                            rvChatList.smoothScrollToPosition(rvChatList.getAdapter().getItemCount() - 1);
                        }

                    }, 100);
                }
            }
        });
    }

    private EditText.OnEditorActionListener searchBoxListener = new EditText.OnEditorActionListener(){
        @Override
        public boolean onEditorAction(TextView tv, int actionId, KeyEvent keyEvent){
            if(actionId == EditorInfo.IME_ACTION_DONE){
                if(!etSearchBox.getText().equals("")) {
                    try {
                        presenter.onEditTextActionDone(etSearchBox.getText().toString());
                        etSearchBox.getText().clear();
                        String toSpeak = presenter.botResponse;
                        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        return true;
                    }
                    catch(IOException | ClassNotFoundException e){
                        presenter.botResponse = e.toString();
                        Toast.makeText(getApplicationContext(),
                                e.toString(),
                                Toast.LENGTH_SHORT).show();
                        tts.speak(e.toString(), TextToSpeech.QUEUE_FLUSH, null);
                        return true;
                    }


                }

            }
            return false;
        }
    };




    @Override

    public void notifyAdapterObjectAdded(int position) {
        //Method that must notify that a new item has been inserted.
        this.chatAdapter.notifyItemInserted(position);

    }



    @Override
    public void scrollChatDown() {
        //This will automatically scroll the screen downwards to the Bottom.
        //this.rvChatList.scrollToPosition(presenter.getChatObjects().size() - 1);
        this.rvChatList.scrollToPosition(presenter.getChatObjects().size() - 1);
    }



    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Could not recognize speech!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etSearchBox.setText(result.get(0));
                    if(!etSearchBox.getText().equals("")) {
                        try {
                            presenter.onEditTextActionDone(etSearchBox.getText().toString());
                            etSearchBox.getText().clear();
                            String toSpeak = presenter.botResponse;
                            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                        }
                        catch(IOException | ClassNotFoundException e){
                            presenter.botResponse = e.toString();
                            Toast.makeText(getApplicationContext(),
                                    e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            tts.speak(e.toString(), TextToSpeech.QUEUE_FLUSH, null);

                        }


                    }


                }
                break;
            }

        }
    }

   /* public class DownloadModel extends AsyncTask<String, Integer, Model> {
        Context context;
        Model model = null;
        ProgressDialog pd;

        public DownloadModel(Context context){
            this.context = context;
            this.pd = new ProgressDialog(context);
        }

        @Override
        public Model doInBackground(String... params){
            try {
                String filePath = context.getFilesDir() + File.separator + "my_turtle.ttl";
                File destinationFile = new File(filePath);
                FileOutputStream outputStream = new FileOutputStream(destinationFile);
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("sample_3.ttl");
                CountingInputStream c = new CountingInputStream(inputStream);
                System.out.println("The byte count is: " + c.read());
                int totalBytes = (int)c.getByteCount();
                BufferedInputStream bStream = new BufferedInputStream(inputStream);
               //Uri aye = Uri.parse("android.resource://technologies.chatbot.ai.simi.com.simisemanticsearches/raw/sample_3.ttl");


                byte[] buffer = new byte[10000000];
                //50000000
                int length = 0;
                int per = 0;


                while ((length = bStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                        try {
                            publishProgress((int) c.getByteCount() / c.getCount());
                        }catch(ArithmeticException e){
                            System.out.println("Cannot divide by 0");
                        }

                }
                //FileUtils.copyInputStreamToFile(inputStream, destinationFile);

                bStream.close();
                inputStream.close();
                outputStream.close();
                model = ModelFactory.createDefaultModel();



                TDBLoader.loadModel(model, filePath, false);
                //RDFParser.create().source(inputStream).lang(Lang.TURTLE).parse(model);
                //RDFParser m = null;
                MainActivity.presenter.setModel(model);

            }catch(FileNotFoundException e){
                e.printStackTrace(System.out);
            }
            catch(IOException e){
                e.printStackTrace(System.out);
            }
            return model;
        }

        @Override
        public void onProgressUpdate(Integer... progress){
            progressBar.setProgress(progress[0]);
            pd.setProgress(progress[0]);

        }

        @Override
        public void onPreExecute(){
            pd.setMessage("Loading Knowledgebase...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setProgress(0);
            pd.setMax(100);
            pd.show();
        }

        @Override
        public void onPostExecute(Model i){
            //progressBar.setVisibility(View.GONE);
            pd.dismiss();
        }
    }*/

   /* public class CreateSocket extends AsyncTask<Socket, Integer, Socket> {
        Context context;
        ProgressDialog pd;

        public CreateSocket(Context context){
            this.context = context;
            this.pd = new ProgressDialog(context);
        }

        @Override
        public Socket doInBackground(Socket... params){
            Socket clientSocket = null;
            try{
                InetAddress address = InetAddress.getByName("192.168.1.102");
                clientSocket = new Socket(address, 1001);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                MainActivity.presenter.setters(br, clientSocket, out);
            }catch(Exception e){
                e.printStackTrace(System.out);
            }

            return clientSocket;
        }

        @Override
        public void onPreExecute(){
            pd.setMessage("Connecting to my server...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setProgress(0);
            pd.setMax(100);
            pd.show();
        }

        @Override
        public void onPostExecute(Socket i){
            //progressBar.setVisibility(View.GONE);
            pd.dismiss();
        }
    }*/
}
