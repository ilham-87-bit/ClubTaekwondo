package com.clubtaekwondo.club.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public abstract class SessionUtils {

    private SessionUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    public static Locale getLocale(HttpServletRequest request) {
        return getSessionObject("session_key_locale", Locale.class, request);
    }

    public static Locale getLocale(WebRequest request) {
        return getSessionObject("session_key_locale", Locale.class, request);
    }

    public static void setLocale(Locale locale, HttpServletRequest request) {
        setSessionObject("session_key_locale", locale, request);
    }

    public static void setLocale(Locale locale, WebRequest request) {
        setSessionObject("session_key_locale", locale, request);
    }

    // Session base getter and setter

    public static <T> T getSessionObject(String key, Class<T> type, WebRequest request) {
        return getSessionObject(key, type, ((ServletRequestAttributes) request).getRequest());
    }

    public static void setSessionObject(String key, Object value, WebRequest request) {
        setSessionObject(key, value, ((ServletRequestAttributes) request).getRequest());
    }


    @SuppressWarnings("unchecked")
    public static <T> T getSessionObject(String key, Class<T> type, HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(key);
        return obj == null ? null : (T) obj;
    }

    public static void setSessionObject(String key, Object value, HttpServletRequest request) {
        request.getSession().setAttribute(key, value);
    }

    public static void removeSessionObject(String key, HttpServletRequest request) {
        request.getSession().removeAttribute(key);
    }

    public static <T> T getAndRemoveSessionObject(String key, Class<T> type, HttpServletRequest request) {
        T obj = getSessionObject(key, type, request);
        removeSessionObject(key, request);
        return obj;
    }
}
