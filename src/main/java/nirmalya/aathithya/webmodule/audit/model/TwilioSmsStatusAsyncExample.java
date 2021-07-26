///**
// * 
// */
//package nirmalya.aathithya.webmodule.audit.model;
//
//import javax.mail.Message;
//
//import org.apache.catalina.util.ResourceSet;
//import org.springframework.util.concurrent.ListenableFuture;
//
///**
// * @author USER
// *
// */
//public class TwilioSmsStatusAsyncExample {
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
//        ListenableFuture<ResourceSet<Message>> future = Message.reader().readAsync();
//        Futures.addCallback(
//                future,
//                new FutureCallback<ResourceSet<Message>>() {
//                    public void onSuccess(ResourceSet<Message> messages) {
//                        for (Message message : messages) {
//                            System.out.println(message.getSid() + " : " + message.getStatus());
//                        }
//                    }
//
//                    public void onFailure(Throwable t) {
//                        System.out.println("Failed to get message status: " + t.getMessage());
//                    }
//                });
//    }
//}