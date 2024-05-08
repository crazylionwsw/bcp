package com.fuze.bcp.msg.service.impl;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.msg.service.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 邮件服务
 */
@Service
public class EmailServiceImpl implements IEmailService {

    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final String DEFAULT_ENCODING = "utf-8";

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发件人
     */
    @Value("${email.from}")
    private String mailFrom;

    /**
     * 发件人名称
     */
    @Value("${email.fromName}")
    private String mailFromName;


    public void send(String to, String subject, String body, File[] attachments) throws Exception {
        send(new String[]{to}, subject, body, attachments);
    }


    public void send(String[] tos, String subject, String body, File[] attachments) throws Exception {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        helper.setTo(tos);
        helper.setFrom(mailFrom, mailFromName);
        helper.setSubject(subject);
        helper.setText(body, true);

        if (attachments != null && attachments.length > 0) {
            for (File file : attachments) {
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                helper.addAttachment(file.getName(), fileSystemResource);
            }
        }
        javaMailSender.send(msg);
    }

    public void send(String[] tos, String subject, String body, List<File> attachments) throws Exception {
        File[] atts = new File[attachments.size()];
        atts = attachments.toArray(atts);
        send(tos, subject, body, atts);
    }

    public void send(String[] tos, String subject, String body) throws Exception {
        send(tos, subject, body, new File[0]);
    }

    public void send(String to, String subject, String body) throws Exception {
        send(new String[]{to}, subject, body);
    }

    @Autowired
    IFileBizService iFileBizService;

    public void send(List<String> tos, String subject, String body, List<String> attachments) throws Exception {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        helper.setTo(tos.toArray(new String[tos.size()]));
        helper.setFrom(mailFrom, mailFromName);
        helper.setSubject(subject);
        helper.setText(body, true);
        if (attachments != null && attachments.size() > 0) {
            for (String fileId : attachments) {
                FileBean fileBean = iFileBizService.actGetFile(fileId).getD();
                InputStream inputStream = FileBeanUtils.downloadFile(fileBean);
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int rc = 0;
                while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                byte[] in2b = swapStream.toByteArray();
                helper.addAttachment(fileBean.getFileName(), new ByteArrayResource(in2b));
            }
        }
        javaMailSender.send(msg);
    }

}
