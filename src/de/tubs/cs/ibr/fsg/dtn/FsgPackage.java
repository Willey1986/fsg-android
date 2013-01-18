package de.tubs.cs.ibr.fsg.dtn;


public class FsgPackage {
	
	
	private int packageTyp;
	
	private int version;
	
	private byte[] payload;
	
	private UpdateRequest updateRequest;
	
	
	public FsgPackage(int packageTyp, UpdateRequest updateRequest, int version, byte[] payload ) {
		this.packageTyp    = packageTyp;
		this.version       = version;
		this.payload       = payload;
		this.updateRequest = updateRequest;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPackageTyp() {
		return packageTyp;
	}

	public void setPackageTyp(int packageTyp) {
		this.packageTyp = packageTyp;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public UpdateRequest getUpdateRequest() {
		return updateRequest;
	}

	public void setUpdateRequest(UpdateRequest updateRequest) {
		this.updateRequest = updateRequest;
	}

	
}
