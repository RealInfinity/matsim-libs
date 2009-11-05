/* *********************************************************************** *
 * project: org.matsim.*
 * PopDensityGrid.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

/**
 * 
 */
package playground.johannes.socialnetworks.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.matsim.api.basic.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.scenario.ScenarioLoaderImpl;

import playground.johannes.socialnetworks.graph.spatial.SpatialGrid;

/**
 * @author illenberger
 *
 */
public class PopulationDensity {

	private static final String MODULE_NAME = "densityGrid";
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ScenarioLoaderImpl loader = new ScenarioLoaderImpl(args[0]);
		loader.loadScenario();
		Scenario data = loader.getScenario();
		Config config = data.getConfig();
		
		double resolution = Double.parseDouble(config.getParam(MODULE_NAME, "resolution"));
		String output = config.getParam(MODULE_NAME, "output");
		
		PopulationImpl population = (PopulationImpl) data.getPopulation();//FIXME
				
		double maxX = 0;
		double maxY = 0;
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		for(PersonImpl person : population.getPersons().values()) {
			Coord homeLoc = person.getSelectedPlan().getFirstActivity().getCoord();
			maxX = Math.max(maxX, homeLoc.getX());
			maxY = Math.max(maxY, homeLoc.getY());
			minX = Math.min(minX, homeLoc.getX());
			minY = Math.min(minY, homeLoc.getY());
		}
		
		SpatialGrid<Double> grid = new SpatialGrid<Double>(minX, minY, maxX, maxY, resolution);
		
		for(PersonImpl person : population.getPersons().values()) {
			Coord homeLoc = person.getSelectedPlan().getFirstActivity().getCoord();
			
			Double count = grid.getValue(homeLoc);
			if(count == null)
				count = 0.0;
			count++;
			grid.setValue(count, homeLoc);
		}

		grid.toFile(output);
		
	}

}
