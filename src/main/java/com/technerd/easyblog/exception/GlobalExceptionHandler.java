package com.technerd.easyblog.exception;

import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常捕获
 *
 * @author technerd
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //错误显示页面

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * @Title: IMoocExceptionHandler.java
     * @Package com.imooc.exception
     * @Description: 判断是否是ajax请求
     * Copyright: Copyright (c) 2017
     * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
     * @author leechenxiang
     * @date 2017年12月3日 下午1:40:39
     * @version V1.0
     */
    public static boolean isAjax(HttpServletRequest httpRequest) {
        return (httpRequest.getHeader("X-Requested-With") != null
                && "XMLHttpRequest"
                .equals(httpRequest.getHeader("X-Requested-With").toString()));
    }


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数", e);
        String message = "【缺少请求参数】" + e.getMessage();
        return new JsonResult(400,message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败", e);
        String message = "【参数解析失败】" + e.getMessage();
        return new JsonResult(400,message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = "【参数验证失败】" + String.format("%s:%s", field, code);
        return new JsonResult(400,message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public JsonResult handleBindException(BindException e) {
        log.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = "【参数绑定失败】" + String.format("%s:%s", field, code);
        return new JsonResult(400,message);
    }


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResult handleServiceException(ConstraintViolationException e) {
        log.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = "【参数验证失败】" + violation.getMessage();
        return new JsonResult(400,message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public JsonResult handleValidationException(ValidationException e) {
        log.error("参数验证失败", e);
        String message = "【参数验证失败】" + e.getMessage();
        return new JsonResult(400,message);
    }

    /**
     * 404 - Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public JsonResult noHandlerFoundException(NoHandlerFoundException e) {
        log.error("Not Found", e);
        String message = "【页面不存在】" + e.getMessage();
        return new JsonResult(404,message);
    }


    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);
        String message = "【不支持当前请求方法】" + e.getMessage();
        return new JsonResult(405,message);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JsonResult handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型", e);
        String message = "【不支持当前媒体类型】" + e.getMessage();
        return new JsonResult(415,message);
    }

    /**
     * 统一异常处理
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(MyBlogException.class)
    @ResponseBody
    public JsonResult processApiException(HttpServletResponse response,
                                          MyBlogException e) {
        JsonResult result = new JsonResult(0, e.getMessage());
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        log.error("业务异常，提示前端操作不合法", e.getMessage(), e);
        return result;
    }

    /**
     * 获取其它异常。包括500
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public JsonResult defaultErrorHandler(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Exception e) throws IOException {
        e.printStackTrace();
        JsonResult jsonResult = new JsonResult();
        if (isAjax(request)) {
            if (e instanceof UnauthorizedException) {
                jsonResult.setMsg("没有权限");
            } else {
                jsonResult.setMsg(e.getMessage());
            }
            jsonResult.setCode(0);
            return jsonResult;
        }

        if (e instanceof UnauthorizedException) {
            //请登录
            log.error("无权访问", e);
            jsonResult.setCode(403);
            jsonResult.setMsg("无权访问");
        }
        //其他异常
        String message = e.getMessage();
        jsonResult.setCode(500);
        jsonResult.setMsg(message);
        return jsonResult;
    }
}
