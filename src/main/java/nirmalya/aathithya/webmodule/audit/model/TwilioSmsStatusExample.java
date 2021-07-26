///**
// * 
// */
//package nirmalya.aathithya.webmodule.audit.model;
//
///**
// * @author USER
// *
// */
//public class TwilioSmsStatusExample {
//    // Find your Account Sid and Token at twilio.com/console
//    public static final String ACCOUNT_SID = "SID";
//    public static final String AUTH_TOKEN = "AUTH";
//
//    // Create a phone number in the Twilio console
//    public static final String TWILIO_NUMBER = "+12223334444";
//
//    public static void main(String[] args) {
//
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        ResourceSet<Message> messages = Message.reader().read();
//        for (Message message : messages) {
//            System.out.println(message.getSid() + " : " + message.getStatus());
//        }
//    }
//}