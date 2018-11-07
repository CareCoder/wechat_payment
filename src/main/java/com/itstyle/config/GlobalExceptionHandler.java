package com.itstyle.config;

import com.itstyle.domain.park.resp.Response;
import com.itstyle.utils.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public Response defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Error in {}: {}", e.getClass().getSimpleName(), e.getMessage());
        e.printStackTrace();
        req.setAttribute("error", e.getMessage());
        return Response.build(Status.ERROR, e.getMessage(), null);
    }
}
