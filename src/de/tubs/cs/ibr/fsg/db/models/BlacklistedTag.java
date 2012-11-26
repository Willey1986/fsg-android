package de.tubs.cs.ibr.fsg.db.models;

import de.tubs.cs.ibr.fsg.db.DBHelper;
import android.content.ContentValues;

public class BlacklistedTag {

	private int tag_id;
	
	public BlacklistedTag(int tag_id) {
		this.tag_id = tag_id;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.BLACKLISTED_TAGS_COLUMN_TAG_ID, tag_id);
		return values;
	}
	
}
