/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.util;

import java.io.Serializable;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ruangmotor.app.model.User;
import ruangmotor.app.service.UserService;

/**
 * Extends this class for all the ManagedBean classes that handles menu view.
 * That class will be registered as 'task' with 'TopLevelMenu' parent and the
 * secure objects that been declared will also be registered as 'task' with
 * '{managed_bean_package_name}' parent.
 *
 * @author Randy
 */
public abstract class AbstractManagedBean implements Serializable {

    @Autowired
    private UserService userService;

    User currentUser;

    Principal principal;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public AbstractManagedBean() {
    }

    protected void addMessage(String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    protected void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary, detail));
    }

    protected void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    protected void addPopUpMessage(String summary) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary));
    }

    protected void addPopUpMessage(String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary, detail));
    }

    protected void addPopUpMessage(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }

    public static void showMessageInDialog(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary,
                detail);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public static void showGrowl(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary,
                detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static Object getRequestParam(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        Map<String, Object> map = component.getAttributes();

        return map.get(name);
    }

    public User getCurrentUser() {
//        System.out.println("getCurrentUser");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("auth principal : " + auth.getPrincipal());
//        System.out.println("auth name : " + auth.getName());
        User user = userService.findByUsername(auth.getName());
//        System.out.println("user : " + user);
        return user;
    }
}
