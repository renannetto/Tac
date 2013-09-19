package ro7.game.exceptions;

public class ExpectedTokenException extends Exception {

	public ExpectedTokenException(String token, String line) {
		super(token + " token was expected on line: " + line);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
