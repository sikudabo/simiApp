package technologies.chatbot.ai.simi.com.simisemanticsearches;

public class ChatInputObject extends ChatObject{

   /*Class that represents an input object. Will extend the ChatObject class and override the
   abstract method "getType()". Since this is a child class we do not have to code the non-abstract
   methods here. We can simply implement them on this child class.
    */


        @Override
        public int getType() {
            //Overriden getType() method. Will automatically return "INPUT_OBJECT"
            return ChatObject.INPUT_OBJECT;

        }

}
