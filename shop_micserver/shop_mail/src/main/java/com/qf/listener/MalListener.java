package com.qf.listener;

import com.qf.entity.Email;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MalListener {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from;

    @RabbitListener(queuesToDeclare = @Queue(name = "mail_queue"))
    public void msgHandle(Email email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        //发送人
        mimeMessageHelper.setFrom(from);
        //抄送
        mimeMessageHelper.setCc(from);
        //发送去
        mimeMessageHelper.setTo(email.getTo());
        //标题
        mimeMessageHelper.setSubject(email.getSubject());
        //内容
        mimeMessageHelper.setText(email.getContext(),true);
        //时间
        mimeMessageHelper.setSentDate(email.getSendTime());
        javaMailSender.send(mimeMessage);
        System.out.println("发送成功");
    }

}
