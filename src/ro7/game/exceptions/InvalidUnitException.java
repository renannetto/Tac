package ro7.game.exceptions;

public class InvalidUnitException extends InvalidTokenException {

	public InvalidUnitException(String unit, String line) {
		super(unit + " is not a valid unit on line: " + line);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
