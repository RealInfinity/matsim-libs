package playground.wrashid.parkingSearch.planLevel.occupancy;

import java.util.ArrayList;

import playground.wrashid.lib.GeneralLib;

/**
 * Knowing, when a parking gets full, lets car drivers estimate if they could
 * reach a parking before it gets full.
 * 
 * attention: if parking has capacity 0, additional check for that is needed by
 * user (don' rely on this class).
 * 
 * using 24 hour projected time!
 * 
 * @author rashid_waraich
 * 
 */
public class ParkingCapacityFullLogger {

	ArrayList<ParkingCapacityFullItem> list;

	double firstParkingEndeTime=-1.0;
	double startTime;

	public ParkingCapacityFullLogger() {
		resetStartTime();
		list = new ArrayList<ParkingCapacityFullItem>();
	}

	private void resetStartTime() {
		startTime = -1.0;
	}

	private boolean isStartTimeUndefined() {
		if (startTime<0){
			return true;
		} else {
			return false;
		}
	}
	
	private void errorIfStartAlreadyDefined() {
		if (startTime > 0) {
			throw new Error("a call to logParkingFull must follow a call to logParkingNotFull");
		}
	}

	public void logParkingFull(double startTime) {
		GeneralLib.errorIfNot24HourProjectedTime(startTime);

		errorIfStartAlreadyDefined();

		this.startTime = startTime;
	}

	public void logParkingNotFull(double endTime) {
		GeneralLib.errorIfNot24HourProjectedTime(endTime);

		if (isStartTimeUndefined()){
			// this means, that the first parking end time.
			
			firstParkingEndeTime=endTime;
			return;
		}
		
		list.add(new ParkingCapacityFullItem(startTime, endTime));

		resetStartTime();
	}
	
	public void closeLastParking(){
		if (firstParkingEndeTime>0){
			list.add(new ParkingCapacityFullItem(startTime, firstParkingEndeTime));
		}	
	}
	

	public ArrayList<ParkingCapacityFullItem> getLog() {
		return list;
	}

	public boolean isParkingFullAtTime(double time) {
		GeneralLib.errorIfNot24HourProjectedTime(time);

		if (list.isEmpty()) {
			return false;
		}

		int i = 0;
		while (i < list.size() && !GeneralLib.isIn24HourInterval(list.get(i).getStartTime(), list.get(i).getEndTime(), time)) {
			i++;
		}

		if (i == list.size()) {
			return false;
		} else {
			return true;
		}
	}

}
