/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Fathan
 */
@Controller
@Scope("session")
public class MenuMBean implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Getter
    private Authentication userSession;

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    public void initialize() {
        // get user_login
        try {
            userSession = SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            this.userSession = null;
        }

    }
}

