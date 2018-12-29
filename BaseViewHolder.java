package technologies.chatbot.ai.simi.com.simisemanticsearches;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    /*Abstract class that our ChatInputViewHolder and ChatResponseViewHolder will inherit from.
    The constructor of these objects will take a view as the argument and we will call the super
    method in the constructor on the itemView. We will also override the onBindView method from
    the RecyclerView.ViewHolder parent class, and we will pass one of our ChatObjects (either an
    input object or a response object) to this method.
     */
    BaseViewHolder(View itemView) {

        super(itemView);

    }



    public abstract void onBindView(ChatObject object);

}

