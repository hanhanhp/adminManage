package com.link.common.util;

/**
 * @ClassName: Constant
 * @Description: 全局常量
 * @author: linkzz
 * @data: 2017-05-08 16:08
*/
public class Constant {
    //页面返回结果
    public static String RESULT_SUCCESS = "0000";           //页面结果返回成功
    public static String RESULT_SQL_ERROR = "0001";         //页面结果返回数据库操作异常！
    public static String RESULT_OUT_TIME = "0002";          //请求超时
    public static String RESULT_FORBIDDEN= "0003";          //禁止访问，未授权

    //登录返回码
    public static String RESULT_LOGIN_ACC_NOT_EXISTENT = "1000";      //账号不存在
    public static String RESULT_LOGIN_DISABLE = "1001";               //账号未启用
    public static String RESULT_LOGIN_ERROR_PWD = "1003";               //密码错误
    public static String RESULT_LOGIN_NUKOWN = "1004";                  //未知错误
    //错误页面常量
    public static String error401path = "/401.html";        //未授权
    public static String error404path = "/404.html";        //找不到
    public static String error500path = "/500.html";        //服务器异常
    public static String error403path = "/403.html";        //禁止访问

    //jqGrid 页面参数常量
    public static String EDIT = "edit";                     //编辑
    public static String ADD = "add";                       //添加
    public static String DEL = "del";                       //删除

    //组合查询
    public static String GROUPOP_OR = "OR"; 		        //组合查询 OR
    public static String GROUPOP_ADD = "ADD"; 		        //组合查询ADD

    //查询条件
    public static String OP_EQUAL = "eq";				    //相等		=
    public static String OP_NOT_EQUAL = "ne"; 			    //不相等		<>
    public static String OP_LESS = "lt";				    //小于 		<
    public static String OP_LESS_OR_EQUAL = "le"; 		    //小于或等于	<=
    public static String OP_GREATER = "gt";				    //大于		>
    public static String OP_GREATER_OR_EQUAL = "ge";	    //大于或等于	>=
    public static String OP_BEGINS_WITH = "bw";			    //以什么开始	like %xxx
    public static String OP_DOES_NOT_BEGIN_WITH = "bn";	    //不以什么开始	not like %xxx
    public static String OP_IS_IN = "in";				    //in()
    public static String OP_IS_NOT_IN = "ni";			    //not in()
    public static String OP_ENDS_WITH = "ew";			    //以什么结束	like xxx%
    public static String OP_DOES_NOT_ENDS_WITH = "en";	    //不以什么结束	not like xxx%
    public static String OP_CONTAINS = "cn";			    //包含		like %xxx%
    public static String OP_DOES_NOT_CONTAINS = "nc";	    //不包含		not like %xxx%

}
