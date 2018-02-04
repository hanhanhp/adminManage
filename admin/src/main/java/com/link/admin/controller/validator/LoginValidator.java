package com.link.admin.controller.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by linkzz on 2017-06-01.
 */
public class LoginValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateRequiredString("userName","userNameMsg","请输入用户名！");
        validateRequiredString("password","password","请输入密码！");
    }

    @Override
    protected void handleError(Controller controller) {
        controller.keepPara("userName");
        controller.render("login.html");
    }
}
