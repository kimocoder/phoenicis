/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.containers;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.playonlinux.containers.entities.ContainerEntity;
import com.playonlinux.containers.entities.ContainersWindowEntity;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.EntitiesProvider;

@Scan
public final class ContainersEntitiesProvider
        extends ObservableDefaultImplementation<ContainersWindowEntity>
        implements Observer<ContainersManager, ContainersManager>,
                   EntitiesProvider<ContainerEntity, ContainersWindowEntity> {

    @Inject
    static ServiceManager serviceManager;

    private Predicate<ContainerEntity> lastFilter;

    private ContainersManager containersManager;


    @Override
    public void filter(Predicate<ContainerEntity> filter) {
        this.lastFilter = filter;
        update(containersManager, containersManager);
    }

    @Override
    public void update(ContainersManager observable, ContainersManager argument) {
        List<ContainerEntity> containerEntities = argument.getContainers().stream()
        		.map(c -> c.createEntity())
        		.filter(e -> lastFilter == null || lastFilter.test(e))
        		.collect(Collectors.toList());

        ContainersWindowEntity containersWindowEntity = new ContainersWindowEntity(containerEntities);
        this.notifyObservers(containersWindowEntity);
    }

    @Override
    public void shutdown() {
        deleteObservers();
    }

    @Override
    public void init() throws ServiceInitializationException {
        ContainersManager containersManagerService = serviceManager.getService(ContainersManager.class);
        containersManagerService.addObserver(this);
    }
}