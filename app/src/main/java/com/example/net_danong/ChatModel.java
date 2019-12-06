package com.example.net_danong;

import java.util.HashMap;
import java.util.Map;

class ChatModel {


    public Map<String,Boolean> users = new HashMap<>(); //채팅방의 유저들
    public Map<String,Comment> comments = new HashMap<>();//채팅방의 대화내용


    public static class Comment {

        public String uid;
        public String message;
        public Object timestamp;
        public Map<String,Object> readUsers = new HashMap<>();

        public Comment(String uid, String message, Object timestamp) {
            this.uid = uid;
            this.message = message;
            this.timestamp = timestamp;
        }
    }

}