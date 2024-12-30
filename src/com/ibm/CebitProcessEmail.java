package com.ibm;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CebitProcessEmail {
	void CebitProcessEmailSend(String sub, String MailBody) throws AddressException {
		//String to ="sakya.samanta@anthem.com";
		String to = "DL-FileNetLightsOnSupport@anthem.com";
		//String cc = "DL-IBM_L2_FileNet@anthem.com";
		String from = "DL-FileNetLightsOnSupport@anthem.com";
		String host = "smtp.wellpoint.com";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);

		String[] recipientList = to.split(",");
		int counter =0;
		InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
		for(String recipient : recipientList) {
			recipientAddress[counter]=new InternetAddress(recipient.trim());
			counter++;
		}
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			// message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setRecipients(Message.RecipientType.TO, recipientAddress);
			// message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			//String sub = "HPIISplit Logs Check";
			message.setSubject(sub);
			message.setText(MailBody);
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
