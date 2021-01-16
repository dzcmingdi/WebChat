// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SpringMailServiceImpl.java

package ssm.zmh.webchat.utils.spring;

import javax.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SpringMailServiceImpl
{

    public SpringMailServiceImpl()
    {
    }

    public void sendHtmlMail(String to, String subject, String content)
        throws MessagingException
    {
        javax.mail.internet.MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("postmaster@hq205.top");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    JavaMailSender javaMailSender;
}
