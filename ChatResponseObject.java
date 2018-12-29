package technologies.chatbot.ai.simi.com.simisemanticsearches;

public class ChatResponseObject extends ChatObject {
    /**Class that represents a response object. Inherits from the parent class "ChatObject"
     * Will implement the getType() method.
     *@return the type of object which will be a RESPONSE_OBJECT.
     */
    @Override
    public int getType() {
        //Implementation of the getType() method. Will always return a Response Object (1)
        return ChatObject.RESPONSE_OBJECT;

    }
}
