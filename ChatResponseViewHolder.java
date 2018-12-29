package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.view.View;
import android.widget.TextView;


public class ChatResponseViewHolder extends BaseViewHolder {

    private TextView tvResponseText;



    public ChatResponseViewHolder(View itemView) {
        /*Constructor for the class. Will take a view as an argument. Will also initialize
        finding the TextView object from the response_background .xml file.
         */
        super(itemView);
        //Below we create a new tvResponseText object.
        this.tvResponseText = (TextView) itemView.findViewById(R.id.tv_response_text);

    }



    @Override

    public void onBindView(ChatObject object) {
        //We set the TextView equal to the text Response generated from the Bot "ChatObject".
        this.tvResponseText.setText(object.getText());

    }


}
