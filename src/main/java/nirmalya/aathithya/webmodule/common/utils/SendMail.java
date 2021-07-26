package nirmalya.aathithya.webmodule.common.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SendMail {

	private static final String SMTP_HOST_NAME = "Smtp.live.com";
	private static final int SMTP_HOST_PORT = 587;

	/*
	 * private static final String SMTP_HOST_NAME = "smtp.gmail.com"; private
	 * static final int SMTP_HOST_PORT = 465;
	 */

	// private static final String SMTP_AUTH_USER =
	// "no-reply@skillodisha.gov.in";
	private static final String SMTP_AUTH_USER = "debendumohapatra@odishamining.in";

	// private static final String SMTP_AUTH_USER = "rintu.putel@gmail.com";
	// private static final String SMTP_AUTH_USER =
	// "noreply-rdwuniversity@nic.in";

	// private static final String SMTP_AUTH_PWD = "9178690702";
	private static final String SMTP_AUTH_PWD = "Rinu@2000#";

	/*
	 * static final String REST_URI = "http://rdwuniversity.nic.in"; static
	 * final String MAIL_BY_REST = "/EmailService/sendMailByRest";
	 */

	private String to;
	private String subject;
	private String mailMessage;
	// private static Logger logger =
	// Logger.getLogger(SendMail.class.getName());

	public SendMail(String to, String subject, String mailMessage) {
		this.to = to;
		this.subject = subject;
		this.mailMessage = mailMessage;
	}

	public boolean send() {
		if (to == null || to.equals("")) {

			// logger.info("Email cannot be sent, as email id is blank.");
			return false;
		}
		// logger.info("Inside sendEmail method--Utkal \n mailTo::"+mailTo+"\n
		// subject::"+subject+"\n mailMessage::"+mailMessage);
		boolean isSent = false;
		try {

			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.quitwait", "false");
			props.put("mail.smtp.starttls.enable", "false");

			Session mailSession = Session.getDefaultInstance(props);

			mailSession.setDebug(true);

			Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(SMTP_AUTH_USER));
			message.setRecipients(RecipientType.TO, to);
			message.setSubject(subject);
			Multipart mp = new MimeMultipart();
			MimeBodyPart messageBodyParthtml = new MimeBodyPart();

			mailMessage += "<br/> ";
			mailMessage += "<br/><br/>";
			mailMessage += "<br/><br/>Odisha Building and Other Construction Workers' Welfare Board ";
			mailMessage += "<br/>Email: nic-eehelpdesk@nic.in";
			mailMessage += "<br/>It is a System Generated Mail, Please don't reply.";

			messageBodyParthtml.setContent(mailMessage, "text/html");

			mp.addBodyPart(messageBodyParthtml);

			message.setContent(mp);
			// String[] mailList = mailTo.split(",");

			// InternetAddress address[]={new InternetAddress(mailTo)};

			transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

			transport.sendMessage(message, message.getAllRecipients());
			// transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));

			transport.close();
			isSent = true;
			// System.out.println("\n\n\n Mail is sent. \n\n");
		} catch (Exception e) {
			// System.out.println(" Email Error Occured"+e.toString()+"\nError
			// message::"+e.getMessage());
			// e.printStackTrace();BaseAction.sendExceptionToMail(e.toString()+"
			// <br />"+e.getStackTrace()[0].toString()+" <br
			// />"+e.getStackTrace()[1].toString()+" <br
			// />"+e.getStackTrace()[2].toString()+" <br
			// />"+e.getStackTrace()[3].toString()+" <br
			// />"+e.getStackTrace()[4].toString(),"helpdeskskillodisha@gmail.com");
			isSent = false;
			// System.out.println("\n\n\n Sending Emails faced problems. \n\n");
		}
		return isSent;

	}

	/**
	 * SSLSocketFactory which trusts all the server certificates.
	 * 
	 * @see SSLNOTES.txt of javamail.zip (Mail API from Sun)
	 *      http://java.sun.com/products/javamail/
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLSocketFactory getTrustedSSLSocketFactory()
			throws NoSuchAlgorithmException, KeyManagementException {

		// Initializing ssl connection object
		SSLContext sslCont = SSLContext.getInstance("TLS");

		sslCont.init(null, getAllTrustManager(), null);

		return sslCont.getSocketFactory();
	}

	private static TrustManager[] getAllTrustManager() {

		return new TrustManager[] { new X509TrustManager() {

			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// Auto-generated method stub
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// Auto-generated method stub
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// Auto-generated method stub
				return null;
			}
		} };
	}

	// Test method
	public static void main(String[] args) {

		try {
			String mailTo = "suprava.swain@nirmalyalabs.com";
			// String mailTo="a.pattanayak@nic.in";

			String subject = "Test Subject";
			String mailMessage = "Hello <br/> Test Message";
			// mailMessage +="<br/>--";
			// mailMessage +="<br/>Utkal University eAdmission";
			// mailMessage +="<br/>helpdesk.uueadmission@gmail.com";
			SendMail mail = new SendMail(mailTo, subject, mailMessage);
			//boolean statusFlag = mail(mailTo, subject, mailMessage);
			System.out.println("Mail status:");
			System.out.println("234567"+mail.send());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
