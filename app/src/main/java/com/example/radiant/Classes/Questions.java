package com.example.radiant.Classes;

public class Questions {

    private String Question;
    private String Answer;


    public Questions() {
        //empty consructor need
    }

    public Questions(String question, String answer) {
        this.Question = question;
        this.Answer = answer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String questions) {
        Question = questions;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
