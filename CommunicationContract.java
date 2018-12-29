package technologies.chatbot.ai.simi.com.simisemanticsearches;
import java.util.ArrayList;
import java.io.IOException;


public interface CommunicationContract {

    interface View {



        void notifyAdapterObjectAdded(int position);



        void scrollChatDown();



    }



    interface Presenter {



        void attachView(CommunicationContract.View view);



        ArrayList<ChatObject> getChatObjects();



        void onEditTextActionDone(String inputText)throws IOException, ClassNotFoundException;

    }


}
