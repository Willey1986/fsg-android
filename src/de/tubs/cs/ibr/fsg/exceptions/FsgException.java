package de.tubs.cs.ibr.fsg.exceptions;

public class FsgException extends Exception{
	private static final long serialVersionUID = -5809846041471649169L;
	
	public static final int GENERIC_EXCEPTION = 0;
	public static final int CARD_MEMORY_FULL  = 1;

	/**
	 * Typ der Exception. Dieses Feld ist dafür da, um mit seiner Hilfe auf der GUI-Ebene entscheiden zu können,
	 * ob man einen "generischen Dialog" einblendet (so in etwa: "Ein Fehler ist aufgetreten, bitte die Entwickler kontaktieren!"),
	 * oder ob man einen Dialog einblendet, der auf die jeweiligen Exception angepasst ist (z.B. wenn eine Exception
	 * beim Speichervorgang ausgeworfen wurde, weil der Speicher voll ist, dann können wir einen Dialog einblenden, der
	 * genau dies dem User mitteilt und ihn dazu auffordert, er möge eine neue Speicherkarte besorgen...)
	 */
	private int type;
	
	/**
	 * Verständlicher und kurzer Text, wo steht, was passiert ist(-wenn möglich- steht hier auch, was zu tun ist).
	 */
	private String detailMessage;
	
	/**
	 * Klasse, wo die Exception ausgeworfen wurde.
	 */
	private String originClass;
	
	/**
	 * Exception, die ursprünglich ausgeworfen wurde.
	 */
	private Exception originException;

	

	public FsgException(String detailMessage, Exception originException, String originClass, int type ) {
		super(detailMessage);
		this.type            = type;
		this.originClass     = FsgException.formatClassString(originClass);
		this.detailMessage   = detailMessage;
		this.originException = originException;
	}


	/**
	 * Der String fängt normalerweise mit einem für uns unnötigen "class " an, den wir hier in dieser Methode entfernen.
	 * 
	 * @param originClass String mit dem kompletten Klassennamen.
	 * @return String mit dem Klassennamen, aber ohne den sonst davor stehenden "class ".
	 */
	private static String formatClassString(String originClass) {
		String result = originClass;
		int index = result.indexOf(" ");
		if (index!=-1){
			result = result.substring(index+1, result.length());
		}
		return result.trim();
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getDetailMessage() {
		return detailMessage;
	}


	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}


	public String getOriginClass() {
		return originClass;
	}


	public void setOriginClass(String originClass) {
		this.originClass = originClass;
	}


	public Exception getOriginException() {
		return originException;
	}


	public void setOriginException(Exception originException) {
		this.originException = originException;
	}

	
	

}
