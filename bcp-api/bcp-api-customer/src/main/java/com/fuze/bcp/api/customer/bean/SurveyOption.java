package com.fuze.bcp.api.customer.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SurveyOption implements Serializable {

        String questionCode;
        Object answerContents;

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public Object getAnswerContents() {
        return answerContents;
    }

    public void setAnswerContents(Object answerContents) {
        this.answerContents = answerContents;
    }
}