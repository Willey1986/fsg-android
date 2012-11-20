package de.tubs.cs.ibr.fsg.db.models;

public class EventClass {
	
	private long id;
	private int event_id;
	private String name;
	
	public EventClass(long id, int event_id, String name) {
		super();
		this.id = id;
		this.event_id = event_id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
