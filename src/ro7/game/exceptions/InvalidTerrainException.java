package ro7.game.exceptions;

public class InvalidTerrainException extends InvalidTokenException {

	public InvalidTerrainException(String terrain, String line) {
		super(terrain + " is not a valid terrain on line: " + line);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
