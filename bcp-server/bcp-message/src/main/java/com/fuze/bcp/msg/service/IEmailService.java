package com.fuze.bcp.msg.service;

import com.fuze.bcp.bean.ResultBean;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by fengyincai on 2017/3/1.
 */
public interface IEmailService {

    void send(String[] tos, String subject, String body) throws Exception;

    void send(String to, String subject, String body) throws Exception;

    void send(String to, String subject, String body, File[] attachments) throws Exception;

    void send(String[] tos, String subject, String body, File[] attachments) throws Exception;

    void send(List<String> tos, String subject, String body, List<String> attachments) throws Exception;

    void send(String[] tos, String subject, String body, List<File> attachments) throws Exception;

}
