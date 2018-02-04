package com.link.api.controller;

import com.jfinal.core.Controller;

/**
 * Api主页
 * @author: linkzz
 *         2017-06-21 16:07
 */
public class ApiController extends Controller {
    public void index(){
        render("api.html");
    }
}
