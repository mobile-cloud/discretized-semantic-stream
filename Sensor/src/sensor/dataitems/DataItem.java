package sensor.dataitems;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DataItem {
	protected String sensorID;
	protected String dataType; // Temp, Humidity, PM2.5, etc
	protected String location;
	private double max;
	private double min;

	public DataItem(String id, String type, String loca) {
		this.sensorID = id;
		this.dataType = type;
		this.location = loca;

		DataRange dr = DataRangeList.getItem(type);

		this.max = dr.max;
		this.min = dr.min;
	}

	public String getSensorID() {
		return sensorID;
	}

	public String getName() {
		return dataType;
	}

	public String getLocation() {
		return location;
	}

	public float generateValue() {
		double d = Math.random() * (max - min) + min;

		int p = 0; // 要保留的小数位数

		switch (dataType) {
		case "volume":
		case "illumination":
		case "CO2":
		case "PM10":
		case "PM2p5":
		case "O3":
		case "SO2":
		case "NO2":
			p = 0;
			break;
		case "temp":
		case "humidity":
			p = 1;
			break;
		case "HCHO":
		case "benzene":
		case "NH3":
		case "TVOC":
			p = 2;
			break;
		case "CO":
			p = 3;
			break;
		default:
			p = 0;
			break;
		}

		//return new BigDecimal(String.format("%." + p + "f", d)).setScale(p, BigDecimal.ROUND_HALF_UP);
		return new BigDecimal(d).setScale(p, BigDecimal.ROUND_HALF_UP).floatValue();
	}
}

class DataRangeList {
	static ArrayList<DataRange> list = new ArrayList<DataRange>();

	public DataRangeList() {
		
	}

	public static DataRange getItem(String type) {
		list.add(new DataRange("temp", 10.0, 36.0));
		list.add(new DataRange("humidity", 20.0, 100.0));
		list.add(new DataRange("volume", 5, 100)); // dB
		list.add(new DataRange("illumination", 10, 1000)); // lx

		list.add(new DataRange("HCHO", 0.00, 0.10));
		list.add(new DataRange("benzene", 0.00, 0.11)); // mg/m3
		list.add(new DataRange("NH3", 0.00, 0.20)); // mg/m3
		list.add(new DataRange("TVOC", 0.00, 0.60)); // mg/m3
		list.add(new DataRange("CO2", 300, 2000)); // ppm

		list.add(new DataRange("PM10", 10, 100));
		list.add(new DataRange("PM2p5", 10, 100));
		list.add(new DataRange("O3", 10, 150));
		list.add(new DataRange("CO", 0.300, 12.000));
		list.add(new DataRange("SO2", 1, 100));
		list.add(new DataRange("NO2", 1, 100));
		
		return list.get(list.indexOf(new DataRange(type)));
	}
}

class DataRange {
	String dataType;
	double max;
	double min;

	public DataRange(String t) {
		this.dataType = t;
	}

	public DataRange(String t, double max, double min) {
		this.dataType = t;
		this.max = max;
		this.min = min;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj != null && obj.getClass() == DataRange.class) {
			DataRange r = (DataRange) obj;
			if (this.dataType.equalsIgnoreCase(r.dataType)) {
				return true;
			}
		}
		return false;
	}
}
