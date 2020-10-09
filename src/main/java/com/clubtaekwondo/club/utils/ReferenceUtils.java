package com.clubtaekwondo.club.utils;

import org.owasp.esapi.reference.RandomAccessReferenceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import org.owasp.esapi.AccessReferenceMap;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;

public abstract class ReferenceUtils {

    private static final Logger log = LoggerFactory.getLogger(ReferenceUtils.class);

    public static final String ACCOUNT_CHAR_FORBIDDEN_REGEXP = "[^a-zA-Z0-9]";

    private ReferenceUtils()
    {
        throw new UnsupportedOperationException();
    }

    public static AccessReferenceMap<?> getReferenceMap(HttpServletRequest request)
    {
        AccessReferenceMap<?> referenceMap = SessionUtils.getSessionObject("referenceMap", AccessReferenceMap.class, request);
        if (referenceMap == null)
        {
            referenceMap = new RandomAccessReferenceMap();
            SessionUtils.setSessionObject("referenceMap", referenceMap, request);
        }
        return referenceMap;
    }

    public static AccessReferenceMap<?> getReferenceMap(ITemplateContext context)
    {
        return getReferenceMap(((IWebContext) context).getRequest());
    }

    public static String getReference(Object directReference, HttpServletRequest request)
    {
        return (String) getReferenceMap(request).addDirectReference(directReference);
    }

    public static String getReference(Object directReference, WebRequest request)
    {
        return getReference(directReference, ((ServletRequestAttributes) request).getRequest());
    }
}
