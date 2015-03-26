package sensor.server;

class Room {
	String roomType;
	int number;

	public Room(String type) {
		this.roomType = type;
		this.number = 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj != null && obj.getClass() == Room.class) {
			Room r = (Room) obj;
			if (this.roomType.equalsIgnoreCase(r.roomType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return roomType.hashCode();
	}
}
