package com.manager.myapplication.utils;

import com.manager.myapplication.model.Giohang;
import com.manager.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public  static final String Base_URL = "http://192.168.0.104/banhang/";
    public static List<Giohang> manggiohang;
    public  static List<Giohang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();
    public static   String ID_RECIVER;
        public static final String SENDID = "idsend";
    public static final String RECEIVEDID= "idreceived";
    public static final String MESS  = "mess";
    public static final  String DATETIME = "datetime";
    public  static final String PATH_CHAT = "chat";
}
