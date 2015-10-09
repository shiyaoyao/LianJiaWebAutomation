package com.lianjia;
import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility; 
public class Mail {  
    private static final String charset = "utf-8";  
    private static final String defaultMimetype = "text/plain";  
  
    /** 
     * �����ʼ�����  ��������
     *  
     * @param receiver 
     *            �ռ��� 
     * @param subject 
     *            ���� 
     * @param mailContent 
     *            �ʼ����� 
     * @param mimetype 
     *            �������� Ĭ��Ϊtext/plain,���Ҫ����HTML����,Ӧ����Ϊtext/html 
     */  
    public static void send(Properties properties, String receiver,  
            String subject, String mailContent, String mimetype) {  
        if (receiver != null && !receiver.equals("")) {  
            send(properties, new String[] { receiver }, subject, mailContent,  
                    mimetype);  
        }  
  
    }  
  
    /** 
     * �����ʼ����� 
     *  
     * @param receivers 
     *            �ռ��� 
     * @param subject 
     *            ���� 
     * @param mailContent 
     *            �ʼ����� 
     * @param mimetype 
     *            �������� Ĭ��Ϊtext/plain,���Ҫ��0��HTML����,Ӧ����Ϊtext/html 
     */  
    public static void send(Properties properties, String[] receivers,  
            String subject, String mailContent, String mimetype) {  
        send(properties, receivers, subject, mailContent, null, mimetype);  
    }  
  
    /** 
     * �����ʼ�����,��Ӹ��� 
     *  
     * @param receivers 
     *            �ռ��� 
     * @param subject 
     *            ���� 
     * @param mailContent 
     *            �ʼ����� 
     * @param attachements 
     *            ���� 
     * @param mimetype 
     *            �������� Ĭ��Ϊtext/plain,���Ҫ����HTML����,Ӧ����Ϊtext/html 
     */  
    public static void send(final Properties properties, String[] receivers,  
            String subject, String mailContent, File[] attachements,  
            String mimetype) {  
        String smtpHost=(String) properties.get("email.hostname");  
        Properties props = new Properties();  
        props.put("mail.smtp.host", smtpHost);// smtp��������ַ  
        props.put("mail.smtp.auth", "true");// ��ҪУ��  
        //���gmail��������  
        if(smtpHost.indexOf("smtp.gmail.com")>=0){  
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
            props.put("mail.smtp.socketFactory.fallback", "false");   
            props.put("mail.smtp.port", "465");   
            props.put("mail.smtp.socketFactory.port", "465");   
        }  
        Session session = Session.getDefaultInstance(props,  
                new Authenticator() {  
                    @Override  
                    protected PasswordAuthentication getPasswordAuthentication() {  
  
                        return new PasswordAuthentication(properties  
                                .getProperty("email.loginname"), properties  
                                .getProperty("email.loginpwd"));// ��¼�û���/����  
                    }  
                });  
        session.setDebug(true);  
        try {  
            MimeMessage mimeMessage = new MimeMessage(session);  
            mimeMessage.setFrom(new InternetAddress(properties  
                    .getProperty("email.personal")));// �������ʼ�  
  
            InternetAddress[] toAddress = new InternetAddress[receivers.length];  
            for (int i = 0; i < receivers.length; i++) {  
                toAddress[i] = new InternetAddress(receivers[i]);  
            }  
            mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);// �ռ����ʼ�  
            mimeMessage.setSubject(subject, charset);  
  
            Multipart multipart = new MimeMultipart();  
            // ����  
            MimeBodyPart body = new MimeBodyPart();  
            // body.setText(message, charset);��֧��html  
            body.setContent(mailContent, (mimetype != null  
                    && !"".equals(mimetype) ? mimetype : defaultMimetype)  
                    + ";charset=" + charset);  
            multipart.addBodyPart(body);// ��������  
            // ����  
            if (attachements != null) {  
                for (File attachement : attachements) {  
                    MimeBodyPart attache = new MimeBodyPart();  
                    attache.setDataHandler(new DataHandler(new FileDataSource(  
                            attachement)));  
                    String fileName = getLastName(attachement.getName());  
                    attache.setFileName(MimeUtility.encodeText(fileName,  
                            charset, null));  
                    multipart.addBodyPart(attache);  
                }  
            }  
            mimeMessage.setContent(multipart);  
            mimeMessage.setSentDate(new Date());  
            Transport.send(mimeMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    private static String getLastName(String fileName) {  
        int pos = fileName.lastIndexOf("\\");  
        if (pos > -1) {  
            fileName = fileName.substring(pos + 1);  
        }  
        pos = fileName.lastIndexOf("/");  
        if (pos > -1) {  
            fileName = fileName.substring(pos + 1);  
        }  
        return fileName;  
    }  
  
    public static void main(String[] args) {  
        File[] file=new File[1];  
        file[0]=new File("C:\\ShiYuJie\\Work\\2015\\WorkStaffs\\LianJiaWebAutomation\\LianJiaWebSelenium\\logs\\current\\logViewer.html");  
          
        Properties properties = new Properties();  
        properties.put("email.hostname", "mail.lianjia.com");  
        properties.put("email.loginname", "shiyujie@lianjia.com");  
        properties.put("email.loginpwd", "fengsuiyi@lianjia");  
        properties.put("email.personal", "shiyujie@lianjia.com");  
        Mail.send(properties, new String[]{"shiyujie@lianjia.com"}, "AutoLog", "Test",file,"text/html");  
          
    }  
  
}