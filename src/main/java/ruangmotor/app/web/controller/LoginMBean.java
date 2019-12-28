/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import java.io.IOException;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import ruangmotor.app.model.User;

/**
 *
 * @author Randy
 */
@Controller
@Scope("session")
public class LoginMBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public void login() {
        try {
            // do any job with the associated values that you've got from the user, 
            // like persisting attempted login, etc.
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            RequestDispatcher dispatcher = ((ServletRequest) externalContext.getRequest())
                    .getRequestDispatcher("/j_spring_security_check");
            dispatcher.forward((ServletRequest) externalContext.getRequest(),
                    (ServletResponse) externalContext.getResponse());
            facesContext.responseComplete();

            // check for AuthenticationException in the session
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            Exception e = (Exception) sessionMap.get(WebAttributes.AUTHENTICATION_EXCEPTION);


        } catch (ServletException | IOException ex) {
        }
    }

}
