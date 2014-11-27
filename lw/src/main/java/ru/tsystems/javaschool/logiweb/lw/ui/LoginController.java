/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.tsystems.javaschool.logiweb.lw.ui;

import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * This class controls the authentication process from this action bean, so that in the event of a failed authentication
 * it can add an appropriate FacesMessage to the response.
 *
 * @author Irina Nikulina
 * 
 */
@Stateless
@Named
public class LoginController {

    public static String driverLogin;

     @Inject
    private Logger logger;

    @Inject
    private Identity identity;

    @Inject
    private CheckUser checkUser;

    @Inject
    private FacesContext facesContext;

    /**
     * Validates user's login and password.
     * @param userId user login
     * @return user's role
     */
    public String login(String userId) {
        AuthenticationResult result = identity.login();
        if (AuthenticationResult.FAILED.equals(result)) {
            facesContext.addMessage(
                    null,
                    new FacesMessage("Authentication was unsuccessful.  Please check your username and password "
                            + "before trying again."));
            return "fail";
        }
        if(checkUser.hasApplicationRole("admin")){
            return "admin";
        }
        if(checkUser.hasApplicationRole("driver")){
            driverLogin = userId;
            return "driver";
        }
        return "fail";
    }

    @Produces
    @Named
    public String getDriverLogin() {
        return driverLogin;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

}
