///**
// * 
// */
//package nirmalya.aathithya.webmodule.audit.model;
//
///**
// * @author USER
// *
// */
//public class AuditSmsMediaExample {
//	// Find your Account Sid and Token at twilio.com/console
//    public static final String ACCOUNT_SID = "SID";
//    public static final String AUTH_TOKEN  = "AUTH";
//
//    // Create a phone number in the Twilio console
//    public static final String TWILIO_NUMBER = "+12223334444";
//
//    public static void main(String[] args) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Message message = Message.creator(
//                new PhoneNumber("+17778889999"),
//                new PhoneNumber(TWILIO_NUMBER),
//                "Sample Twilio MMS using Java")
//                .setMediaUrl(
//                        Promoter.listOfOne(
//                                URI.create("http://www.baeldung.com/wp-content/uploads/2017/10/icon-javaseries-home.png")))
//                .create();
//    }
//}
