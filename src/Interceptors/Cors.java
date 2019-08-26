package Interceptors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletResponse;

public class Cors extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        final ActionContext ac = actionInvocation.getInvocationContext();
        HttpServletResponse response = (HttpServletResponse)ac.get(StrutsStatics.HTTP_RESPONSE);
        response.setHeader("X-Frame-Options", "ALLOWALL");
        response.addHeader("Access-Control-Allow-Origin","*");
        return actionInvocation.invoke();
    }
    public String interscept(ActionInvocation actionInvocation) throws Exception {

        ActionContext actionContext = actionInvocation.getInvocationContext();
        HttpServletResponse servletResponse = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
        servletResponse.addHeader("Access-Control-Allow-Origin","*");
        return actionContext.getActionInvocation().invoke();
    }
}
