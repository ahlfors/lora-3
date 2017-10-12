package yaruliy.security;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoraFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        switch(httpRequest.getRequestURL().toString()){
            case "http://localhost:7001/devices": {
                if(httpRequest.getRemoteUser() != null) {
                    if(httpRequest.getHeader("token") == null){
                        httpResponse.addHeader("token","yaroslav2");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
