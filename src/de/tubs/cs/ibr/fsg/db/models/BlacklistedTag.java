package de.tubs.cs.ibr.fsg.db.models;

public class BlacklistedTag {

	private long id;
	private int tag_id;
	
	public BlacklistedTag(long id, int tag_id) {
		super();
		this.id = id;
		this.tag_id = tag_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}
	
}
