
/* *********************************************************************** *
 * project: org.matsim.*
 * AbstractMobsimModule.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

 package org.matsim.core.mobsim.framework;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.matsim.core.config.Config;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public abstract class AbstractMobsimModule extends AbstractModule {
	private Optional<Config> config = Optional.empty();
	private Optional<AbstractMobsimModule> parent = Optional.empty();

	public void setConfig(Config config) {
		this.config = Optional.of(config);
	}

	public void setParent(AbstractMobsimModule parent) {
		this.parent = Optional.of(parent);
	}

	protected Config getConfig() {
		if (config.isPresent()) {
			return config.get();
		}

		if (parent.isPresent()) {
			return parent.get().getConfig();
		}

		throw new IllegalStateException(
				"No config set. Did you try to use the module outside of the QSim initialization process?");
	}

	protected final void configure() {
		configureMobsim();
	}

	protected abstract void configureMobsim();

	public static AbstractMobsimModule overrideMobsimModules(Collection<AbstractMobsimModule> base,
			List<AbstractMobsimModule> overrides) {
		Module composite = Modules.override(base).with(overrides);

		AbstractMobsimModule wrapper = new AbstractMobsimModule() {
			@Override
			protected void configureMobsim() {
				install(composite);
			}
		};

		base.forEach(m -> m.setParent(wrapper));
		overrides.forEach(m -> m.setParent(wrapper));

		return wrapper;
	}
}
