/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import javax.faces.application.FacesMessage;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.security.web.WebAttributes;
import ruangmotor.app.config.UserValidator;
import ruangmotor.app.model.User;
import ruangmotor.app.service.UserService;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;

/**
 *
 * @author Owner
 */
@Controller
@Scope("view")
@Data
public class UserController extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private UserService userService;
    private User newUser;

    @Autowired
    private UserValidator userValidator;

    private boolean isLoginDisabled = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        newUser = new User();
    }

    public void register() {
        System.out.println("newUser : " + newUser);

        String phoneNoPattern = "(62)[\\s\\)\\-]*(\\s|(\\d){3,})";
        
        if (!newUser.getNoHp().matches(phoneNoPattern)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid Indonesian phone number", ""));
            return;
        }

        if (userService.findByUsername(newUser.getEmail()) != null) {
            showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Username sudah pernah terdaftar");
        } else if (userService.findByEmail(newUser.getEmail()) != null) {
            showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Email sudah pernah terdaftar");
        } else {
            newUser.setNamaLengkap(newUser.getNamaDepan().concat(" ").concat(newUser.getNamaBelakang()));
            userService.save(newUser);
            newUser = new User();
            showGrowl(FacesMessage.SEVERITY_INFO, "Success", "Registrasi user berhasil");
            isLoginDisabled = false;
        }
        RequestContext.getCurrentInstance().update("growl");
    }

    public void login() {
        try {
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
