
/* *********************************************************************** *
 * project: org.matsim.*
 * FacilitiesWriterTest.java
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

 package org.matsim.facilities;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.testcases.MatsimTestUtils;

/**
 * @author mrieser / Senozon AG
 */
public class FacilitiesWriterTest {

    @Rule
    public MatsimTestUtils testUtil = new MatsimTestUtils();

    @Test
    public void testWriteLinkId() {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        ActivityFacilities facilities = scenario.getActivityFacilities();
        ActivityFacilitiesFactory factory = facilities.getFactory();

        ActivityFacility fac1 = factory.createActivityFacility(Id.create("1", ActivityFacility.class), new Coord(10.0, 15.0));
        ((ActivityFacilityImpl) fac1).setLinkId(Id.create("Abc", Link.class));
        ActivityFacility fac2 = factory.createActivityFacility(Id.create("2", ActivityFacility.class), new Coord(20.0, 25.0));
        ((ActivityFacilityImpl) fac2).setLinkId(Id.create("Def", Link.class));
        ActivityFacility fac3 = factory.createActivityFacility(Id.create("3", ActivityFacility.class), new Coord(30.0, 35.0));

        fac1.getAttributes().putAttribute("population", 1000);
        fac2.getAttributes().putAttribute("population", 1200);
        fac3.getAttributes().putAttribute("owner", "pepsiCo");

        facilities.addActivityFacility(fac1);
        facilities.addActivityFacility(fac2);
        facilities.addActivityFacility(fac3);

        String filename = testUtil.getOutputDirectory() + "/facilities.xml";
        new FacilitiesWriter(facilities).write(filename);

        scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        facilities = scenario.getActivityFacilities();
        MatsimFacilitiesReader reader = new MatsimFacilitiesReader(scenario);
        reader.readFile(filename);

        Assert.assertEquals(3, facilities.getFacilities().size());

        ActivityFacility fac1b = facilities.getFacilities().get(Id.create(1, ActivityFacility.class));
        Assert.assertEquals(Id.create("Abc", Link.class), fac1b.getLinkId());
        Assert.assertEquals(1000, fac1b.getAttributes().getAttribute("population"));

        ActivityFacility fac2b = facilities.getFacilities().get(Id.create(2, ActivityFacility.class));
        Assert.assertEquals(Id.create("Def", Link.class), fac2b.getLinkId());
        Assert.assertEquals(1200, fac2b.getAttributes().getAttribute("population"));

        ActivityFacility fac3b = facilities.getFacilities().get(Id.create(3, ActivityFacility.class));
        Assert.assertNull(fac3b.getLinkId());
        Assert.assertEquals("pepsiCo", fac3b.getAttributes().getAttribute("owner"));
    }
}
