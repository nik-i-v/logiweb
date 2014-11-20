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

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

import static org.picketlink.idm.model.basic.BasicModel.*;

/**
 * This startup bean creates a number of default users, groups and roles when the application is started.
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
public class SecurityInitializer {

    @Inject
    private PartitionManager partitionManager;

    @EJB
    private DriverService driverService;

    @Inject
    private Logger logger;
    @PostConstruct
    public void create() {

       IdentityManager identityManager = this.partitionManager.createIdentityManager();
        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
        User newUser = new User("admin");
        identityManager.add(newUser);
        identityManager.updateCredential(newUser, new Password("pass"));
        Role admin = new Role("admin");
        identityManager.add(admin);
        List<Long> ids = driverService.getAllDriverId();
        Role driver = new Role("driver");
        identityManager.add(driver);
        grantRole(relationshipManager, newUser, admin);
        for (Long l: ids){
            User driverUser = new User(l.toString());
            identityManager.add(driverUser);
            identityManager.updateCredential(driverUser, new Password("pass"));
            grantRole(relationshipManager, driverUser, driver);
        }
    }
}
