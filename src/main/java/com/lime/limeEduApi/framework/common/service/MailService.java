package com.lime.limeEduApi.framework.common.service;

import javax.mail.internet.MimeMessage;

import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.dao.MailDAO;
import com.lime.limeEduApi.framework.common.domain.MailResultVO;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailDAO mailDAO;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public MailResultVO sendMail(MailResultVO mailResultVO) {

        mailResultVO.setSender(from);
        try{
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");
            
            mailHelper.setFrom(from);
            mailHelper.setTo(mailResultVO.getReceiver());
            mailHelper.setSubject(mailResultVO.getSubject());
            mailHelper.setText(mailResultVO.getContent(), true);
            
            mailSender.send(mail);
            mailResultVO.setResult(1);
            mailResultVO.setMessage("success fully send mail!");
        } 
        catch(Exception e) 
        {
            CommonUtil.extracePrintLog(e);
            mailResultVO.setResult(0);
            mailResultVO.setMessage(e.getMessage());
            insertMailHistory(mailResultVO);
            throw new CustomException(ResponseCode.MAIL_SEND_FAIL);
        }

        insertMailHistory(mailResultVO);


        return mailResultVO;
    }
    public int insertMailHistory(MailResultVO mailResultVO) {
        return mailDAO.insertMailHistory(mailResultVO);
    }


}