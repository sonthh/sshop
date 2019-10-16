package com.itsontran.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.itsontran.model.bean.Cart;

@Service
public class SendMailService {

	@Autowired
	private JavaMailSender mailSender;

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			File file = ResourceUtils.getFile("classpath:content/javasendmail.properties");
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8"); 

			properties.load(isr);
			
			System.out.println(properties.getProperty("content"));
			
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getContent(String name) {
		Properties properties = new Properties();
		InputStream is = null;
		InputStreamReader isr = null;
		try {
			File file = ResourceUtils.getFile("classpath:content/javasendmail.properties");
			is = new FileInputStream(file);
			isr = new InputStreamReader(is, "UTF-8"); 
			
			properties.load(isr);
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(name);
	}

	private void sendMail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		boolean multipart = true;
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, multipart, "utf-8");
			message.setContent(content, "text/html");
			helper.setTo(to);
			helper.setSubject(subject);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
	public void sendMailSignUp(String to, String link) {
		StringBuilder content = new StringBuilder();
		content.append(getContent("header2"));
		String html = "<a href='" + link + "'>Click link to confirm password.</a>";
		content.append(html);
		content.append(getContent("footer"));
		String subject = "SSHOP CONFIRM ACCOUNT.";
		sendMail(to, subject, content.toString());
	}
	
	public void sendMailOrder(String to, Cart cart) {
		StringBuilder content = new StringBuilder();
		content.append(getContent("header"));
		content.append(getContent("footer"));
		content.append("<h1>Total: " + cart.getTotalCart() + "VND</h1>");
		System.out.println(content.toString());
		String subject = "SSHOP ORDER";
		sendMail(to, subject, content.toString());
	}

}
