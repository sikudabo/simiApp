package technologies.chatbot.ai.simi.com.simisemanticsearches;

import android.support.annotation.NonNull;

public abstract class ChatObject {

    /*An input object (user input) will have an automatic value of 0. Response object will have
   a value of 1.
    */
    public static final int INPUT_OBJECT = 0;
    public static final int RESPONSE_OBJECT = 1;
    private String text; //Text will represent the input text.

    @NonNull
    public String getText() {
        //Method that will retrieve the text from the TextInput widget.
        return text;

    }



    public void setText(@NonNull String text) {
        //Method that will set the text for the TextInput widget.
        this.text = text;

    }



    public abstract int getType();
    //Abstract method that will get the type of object (0 for input, 1 for response)

}


