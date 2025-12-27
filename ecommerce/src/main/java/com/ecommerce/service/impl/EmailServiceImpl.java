package com.ecommerce.service.impl;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ecommerce.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	
	private final JavaMailSender javaMailSender;
	
	@Override
	public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MessagingException {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
			
			mimeMessageHelper.setFrom("gsanskar42@gmail.com");
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text,true);
			mimeMessageHelper.setTo(userEmail);
			javaMailSender.send(mimeMessage);
		}catch(MailException ex) {
			System.out.println("Error------" + ex);
			throw new MailSendException("failed to send email");
		}
		
	}

}
