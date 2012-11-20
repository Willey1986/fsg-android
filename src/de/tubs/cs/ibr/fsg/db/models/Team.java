package de.tubs.cs.ibr.fsg.db.models;

public class Team {
	
	private long id;
	private int teamId;
	private String cn;
	private String cn_short_en;
	private String city;
	private String university;
	private int carNr;
	private int pitNr;
	private boolean isWaiting;
	private EventClass eventClass;
	private String name_pits;
	
	public Team() {
		this.id = 0;
		this.teamId = 0;
		this.cn = null;
		this.cn_short_en = null;
		this.city = null;
		this.university = null;
		this.carNr = 0;
		this.pitNr = 0;
		this.isWaiting = false;
		this.eventClass = null;
		this.name_pits = null;
	}
	
	public Team(long id, int teamId, String cn, String cn_short_en,
			String city, String university, int carNr, int pitNr,
			boolean isWaiting, EventClass eventClass, String name_pits) {
		super();
		this.id = id;
		this.teamId = teamId;
		this.cn = cn;
		this.cn_short_en = cn_short_en;
		this.city = city;
		this.university = university;
		this.carNr = carNr;
		this.pitNr = pitNr;
		this.isWaiting = isWaiting;
		this.eventClass = eventClass;
		this.name_pits = name_pits;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getCn_short_en() {
		return cn_short_en;
	}

	public void setCn_short_en(String cn_short_en) {
		this.cn_short_en = cn_short_en;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public int getCarNr() {
		return carNr;
	}

	public void setCarNr(int carNr) {
		this.carNr = carNr;
	}

	public int getPitNr() {
		return pitNr;
	}

	public void setPitNr(int pitNr) {
		this.pitNr = pitNr;
	}

	public boolean isWaiting() {
		return isWaiting;
	}

	public void setWaiting(boolean isWaiting) {
		this.isWaiting = isWaiting;
	}

	public EventClass getEventClass() {
		return eventClass;
	}

	public void setEventClass(EventClass eventClass) {
		this.eventClass = eventClass;
	}

	public String getName_pits() {
		return name_pits;
	}

	public void setName_pits(String name_pits) {
		this.name_pits = name_pits;
	}

}
