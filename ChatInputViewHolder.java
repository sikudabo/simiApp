package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.view.View;

import android.widget.TextView;

public class ChatInputViewHolder extends BaseViewHolder {
    /**This class will extend the BaseViewHolder class. This class will represent an Input Text
     * from the user. The private field "tvInputText" will represent the text view that will be
     * displayed to the RecyclerView in our .xml file. The "tv_input_text" is the input text in our
     * chat .xml file that we defined. The onBindView method from the RecyclerView.ViewHolder class
     * is overriden in this class. We will pass a ChatObject to this method. We could pass a
     * ChatInputObject because that is the only "type" of object that we will pass to the onBindView
     * method in this class, but since it is just a subtype of ChatObject, we will simply pass a
     * ChatObject and we will prevent it from being a ChatResponseObject in our "main" class when
     * we call this method.
     */
    //This will be the InputText for the viewholder.
    private TextView tvInputText;



    public ChatInputViewHolder(View itemView) {

        super(itemView);
        //This will find the input texts by id from "user_input_background .xml.
        this.tvInputText = (TextView) itemView.findViewById(R.id.tv_input_text);

    }



    @Override
    public void onBindView(ChatObject object) {
        //Will pass a ChatObject of the ChatInputObject type as the argument. Will bind this view
        //to the screen.
        this.tvInputText.setText(object.getText());

    }
}
