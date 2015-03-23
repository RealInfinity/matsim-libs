/* *********************************************************************** *
 * project: org.matsim.*
 * WithinDayDuringLegReplannerFactory.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
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

package org.matsim.withinday.replanning.replanners.interfaces;

import org.matsim.withinday.mobsim.WithinDayEngine;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringLegAgentSelector;

public abstract class WithinDayDuringLegReplannerFactory extends WithinDayReplannerFactory<DuringLegAgentSelector> {

	public WithinDayDuringLegReplannerFactory(WithinDayEngine withinDayEngine) {
		super(withinDayEngine);
	}
	
	@Override
	public abstract WithinDayDuringLegReplanner createReplanner();
}
