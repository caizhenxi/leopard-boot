package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通行证(用户)登录验证接口.
 * 
 * @author 阿海
 *
 */
public interface PassportValidator {

	/**
	 * 是否需要做登陆检查?
	 * 
	 * @param request
	 * @param handler
	 * @return null:表示交给Leopard框架做判断
	 */
	Boolean isNeedCheckLogin(HttpServletRequest request, Object handler);

	/**
	 * 获取当前登录的用户信息(Leopard会自动将返回值存入session作缓存).
	 * 
	 * session里没有用户信息才会调用此方法
	 * 
	 * @return 通行证或uid
	 */
	Object validate(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 显示登陆框.
	 * 
	 * session和cookie都没有合法的用户信息才会调用此方法
	 * 
	 * @param request
	 * @param response
	 * 
	 * @return 已实现登陆框返回true，未实现返回false
	 */
	boolean showLoginBox(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 登陆验证.
	 * 
	 * 在登录框输入账号、密码，点击提交按钮来到此方法做验证。
	 * 
	 * @param request
	 * @param response
	 * @return true:已跳转，false:未跳转
	 * @throws Exception
	 */
	boolean login(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
