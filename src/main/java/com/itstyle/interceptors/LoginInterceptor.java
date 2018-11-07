package com.itstyle.interceptors;

import com.itstyle.common.CustomContext;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        if (account == null) {
            request.getRequestDispatcher("/backend/login.html").forward(request, response);
            return false;
        } else {
            CustomContext.setAccount(account);
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CustomContext.remove();
        Long startTime = (Long) request.getAttribute("startTime");
        Long endTime = System.currentTimeMillis();
        String path = request.getServletPath();
        log.info("[LoginInterceptor] request path [{}] execute time {}ms", path, (endTime - startTime));
    }
}
