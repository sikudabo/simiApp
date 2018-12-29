package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    /*The ChatAdapter will extend the RecyclerView.Adapter class. The RecyclerView is a newer and better version of the
    former android studio "ListView". I will pass an ArrayList<ChatObjects> of chat objects to the constructor of this
    adapter. If the object passed is an input object from the user, it will be rendered to the screen. If the object passed
    is a bot response object, it will be rendered to the screen. I will need to monitor which "type" of object is being passed
    within this class.
     */

    private ArrayList<ChatObject> chatObjects;//We will pass an ArrayList of ChatObjects.



    public ChatAdapter(ArrayList<ChatObject> chatObjects) {

        this.chatObjects = chatObjects; //Pass an ArrayList of ChatObjects to the constructor.

    }



    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Create the ViewHolder based on the viewType, set up two switch statements with a default statement.
        View itemView;

        switch (viewType) {

            case ChatObject.INPUT_OBJECT:

                itemView = inflater.inflate(R.layout.user_input_background, parent, false);
                return new ChatInputViewHolder(itemView);

            case ChatObject.RESPONSE_OBJECT:

                itemView = inflater.inflate(R.layout.bot_response_background, parent, false);
                return new ChatResponseViewHolder(itemView);

            default:

                itemView = inflater.inflate(R.layout.bot_response_background, parent, false);
                return new ChatResponseViewHolder(itemView);

        }

    }



    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        //Will bind the chat object to the screen.
        holder.onBindView(chatObjects.get(position));
    }



    @Override
    public int getItemViewType(int position) {

        //Return whether this is an input object or bot response in the position that is passed as the argument.
        if(chatObjects.get(position).getType() == 0){
            System.out.println("User Input Object Added!");
        }
        else{
            System.out.println("Bot Response Object Added!");
        }
        ArrayList<String> conversations = new ArrayList<String>();
        for(ChatObject c: chatObjects){
            conversations.add(c.getText());
        }
        System.out.println(conversations.toString());
        return chatObjects.get(position).getType();


    }



    @Override
    public int getItemCount() {

        System.out.println("The count is " + chatObjects.size() + " objects");
        //This simply returns the size of the ArrayList<ChatObject> of objects we have in our RecyclerView list.
        return chatObjects.size();

    }
}
