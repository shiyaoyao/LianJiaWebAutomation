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
     * 单个邮件发送  测试用例
     *  
     * @param receiver 
     *            收件人 
     * @param subject 
     *            标题 
     * @param mailContent 
     *            邮件内容 
     * @param mimetype 
     *            内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html 
     */  
    public static void send(Properties properties, String receiver,  
            String subject, String mailContent, String mimetype) {  
        if (receiver != null && !receiver.equals("")) {  
            send(properties, new String[] { receiver }, subject, mailContent,  
                    mimetype);  
        }  
  
    }  
  
    /** 
     * 批量邮件发送 
     *  
     * @param receivers 
     *            收件人 
     * @param subject 
     *            标题 
     * @param mailContent 
     *            邮件内容 
     * @param mimetype 
     *            内容类型 默认为text/plain,如果要发0送HTML内容,应设置为text/html 
     */  
    public static void send(Properties properties, String[] receivers,  
            String subject, String mailContent, String mimetype) {  
        send(properties, receivers, subject, mailContent, null, mimetype);  
    }  
  
    /** 
     * 批量邮件发送,添加附件 
     *  
     * @param receivers 
     *            收件人 
     * @param subject 
     *            标题 
     * @param mailContent 
     *            邮件内容 
     * @param attachements 
     *            附件 
     * @param mimetype 
     *            内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html 
     */  
    public static void send(final Properties properties, String[] receivers,  
            String subject, String mailContent, File[] attachements,  
            String mimetype) {  
        String smtpHost=(String) properties.get("email.hostname");  
        Properties props = new Properties();  
        props.put("mail.smtp.host", smtpHost);// smtp服务器地址  
        props.put("mail.smtp.auth", "true");// 需要校验  
        //针对gmail加密设置  
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
                                .getProperty("email.loginpwd"));// 登录用户名/密码  
                    }  
                });  
        session.setDebug(true);  
        try {  
            MimeMessage mimeMessage = new MimeMessage(session);  
            mimeMessage.setFrom(new InternetAddress(properties  
                    .getProperty("email.personal")));// 发件人邮件  
  
            InternetAddress[] toAddress = new InternetAddress[receivers.length];  
            for (int i = 0; i < receivers.length; i++) {  
                toAddress[i] = new InternetAddress(receivers[i]);  
            }  
            mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);// 收件人邮件  
            mimeMessage.setSubject(subject, charset);  
  
            Multipart multipart = new MimeMultipart();  
            // 正文  
            MimeBodyPart body = new MimeBodyPart();  
            // body.setText(message, charset);不支持html  
            body.setContent(mailContent, (mimetype != null  
                    && !"".equals(mimetype) ? mimetype : defaultMimetype)  
                    + ";charset=" + charset);  
            multipart.addBodyPart(body);// 发件内容  
            // 附件  
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