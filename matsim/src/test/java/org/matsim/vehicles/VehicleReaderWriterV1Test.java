/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.vehicles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.vehicles.VehicleType.DoorOperationMode;

/**
 * @author dgrether
 */
public class VehicleReaderWriterV1Test extends MatsimTestCase {

  private static final String TESTXML  = "testVehicles.xml";

  private final Id id23 = new IdImpl("23");
  private final Id id42 = new IdImpl("42");
  private final Id id42_23 = new IdImpl(" 42  23"); //indeed this should be double blank in the middle but due to collapse this is only one blank
  
	public void testBasicParser() {
		Vehicles vehicles = new VehiclesImpl();
		VehicleReaderV1 reader = new VehicleReaderV1(vehicles);
		reader.readFile(this.getPackageInputDirectory() + TESTXML);
		
		checkContent(vehicles);
	}
	
	

	public void testWriter() throws FileNotFoundException, IOException {
		
		String outfileName = this.getOutputDirectory() + "testOutputVehicles.xml";
		
		//read it
		Vehicles vehicles = new VehiclesImpl();
		VehicleReaderV1 reader = new VehicleReaderV1(vehicles);
		reader.readFile(this.getPackageInputDirectory() + TESTXML);
		//write it
		VehicleWriterV1 writer = new VehicleWriterV1(vehicles);
		writer.writeFile(outfileName);
		assertTrue(new File(outfileName).exists()); 
		//read it again
		vehicles = new VehiclesImpl();
		reader = new VehicleReaderV1(vehicles);
		reader.readFile(this.getOutputDirectory() + "testOutputVehicles.xml");
		
		//check it, check it, check it now!
		this.checkContent(vehicles);
	}

	private void checkContent(Vehicles vehdef) {
		Map<Id, VehicleType> vehicleTypes = vehdef.getVehicleTypes();
		Map<Id, Vehicle> vehicles = vehdef.getVehicles();
			
		assertNotNull(vehicleTypes);
		assertEquals(2, vehicleTypes.size());
		VehicleType vehType = vehicleTypes.get(new IdImpl("normalCar"));
		assertNotNull(vehType);
		assertEquals(9.5, vehType.getLength(), EPSILON);
		assertEquals(3.0, vehType.getWidth(), EPSILON);
		assertEquals(42.0, vehType.getMaximumVelocity(), EPSILON);
		assertNotNull(vehType.getCapacity());
		assertEquals(Integer.valueOf(5), vehType.getCapacity().getSeats());
		assertEquals(Integer.valueOf(20), vehType.getCapacity().getStandingRoom());
		assertNotNull(vehType.getCapacity().getFreightCapacity());
		assertEquals(23.23, vehType.getCapacity().getFreightCapacity().getVolume(), EPSILON);
		assertNotNull(vehType.getEngineInformation());
		assertEquals(EngineInformation.FuelType.diesel, vehType.getEngineInformation().getFuelType());
		assertEquals(0.23, vehType.getEngineInformation().getGasConsumption(), EPSILON);
		assertEquals(23.23, vehType.getAccessTime(), EPSILON);
		assertEquals(42.42, vehType.getEgressTime(), EPSILON);
		assertEquals(DoorOperationMode.parallel, vehType.getDoorOperationMode());
		
		vehType = vehicleTypes.get(new IdImpl("defaultValueCar"));
		assertNotNull(vehType);
		assertEquals(7.5, vehType.getLength(), EPSILON);
		assertEquals(1.0, vehType.getWidth(), EPSILON);
		assertEquals(1.0, vehType.getMaximumVelocity(), EPSILON);
		assertNull(vehType.getCapacity());
		assertEquals(DoorOperationMode.serial, vehType.getDoorOperationMode());
		
		assertNotNull(vehicles);
		assertEquals(3, vehicles.size());
	
		assertNotNull(vehicles.get(id23));
		assertEquals(id23, vehicles.get(id23).getId());
		assertEquals(new IdImpl("normalCar"), vehicles.get(id23).getType().getId());

		assertNotNull(vehicles.get(id42));
		assertEquals(id42, vehicles.get(id42).getId());
		assertEquals(new IdImpl("defaultValueCar"), vehicles.get(id42).getType().getId());
	
		assertNotNull(vehicles.get(id42_23));
		assertEquals(id42_23, vehicles.get(id42_23).getId());
		
		
	}

}
