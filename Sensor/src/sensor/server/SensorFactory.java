package sensor.server;

import java.util.ArrayList;

import sensor.dataitems.DataItem;

class SensorFactory {
	static ArrayList<Room> locationList = new ArrayList<Room>();
	static ArrayList<String> dataTypeList = new ArrayList<String>();
	static int num = 0; // 传感器数量

	public SensorFactory() {

	}

	public static void addAllRoom(ArrayList<String> rooms) {
		for (String r : rooms) {
			addRoom(r);
		}
	}

	public static void addAllDataType(String[] types) {
		for (String t : types) {
			addDataType(t);
		}
	}

	private static void addRoom(String type) {
		Room inRoom = new Room(type);
		if (locationList.contains(inRoom)) {
			locationList.get(locationList.indexOf(inRoom)).number++;
		}
		else {
			locationList.add(inRoom);
		}
	}

	private static void addDataType(String type) {
		if (!dataTypeList.contains(type)) { // 若dataTypeList中不存在
			dataTypeList.add(type);
		}
	}

	public static ArrayList<DataItem> generateSensorDataItemList() {
		ArrayList<DataItem> dl = new ArrayList<DataItem>();

		for (Room rt : locationList) {
			for (String t : dataTypeList) {
				for (int i = 0; i < rt.number; i++) {
					dl.add(new DataItem(String.format("%08d", ++num), t, rt.roomType + "_" + (i + 1)));
				}
			}
		}
		return dl;
	}
}
