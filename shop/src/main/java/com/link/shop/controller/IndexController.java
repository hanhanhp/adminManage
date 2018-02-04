package com.link.shop.controller;

import com.jfinal.core.Controller;

/**
 * Created by linkzz on 2017-06-01.
 */
public class IndexController extends Controller {
    public void index(){
        render("home/home.html");
    }
    public void login(){
        render("home/login.html");
    }
}
