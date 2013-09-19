package ro7.game.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import ro7.game.exceptions.ExpectedTokenException;
import ro7.game.exceptions.InvalidTerrainException;
import ro7.game.exceptions.InvalidTokenException;
import ro7.game.exceptions.InvalidUnitException;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class MapParser {

	private final float SQUARE_SIZE = 50.0f;
	private final String EMPTY_UNIT = "##";
	
	private BufferedReader reader;
	private Scanner scanner;
	
	private GameMap map;
	
	private Map<String, Terrain> terrains;
	private Map<String, Unit> units;
	
	public MapParser() {
		terrains = new HashMap<String, Terrain>();
		units = new HashMap<String, Unit>();
	}
	
	public GameMap parseMap(File mapFile) {
		try {			
			reader = new BufferedReader(new FileReader(mapFile));
			
			parseDimensions();
			parseTerrain();
			parseMapLayout();
			parseUnits();
			parseUnitsPosition();
			
			map.buildGraph();
			
			scanner.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println(mapFile + " not found");
			return null;
		} catch (IOException e) {
			System.out.println("Could not read line");
			return null;
		} catch (InvalidTokenException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (ExpectedTokenException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return map;
	}

	private void parseUnitsPosition() throws IOException, InvalidUnitException, ExpectedTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("START")) {
			throw new ExpectedTokenException("START", line);
		}
		
		int width = map.getWidth();
		int height = map.getHeight();
		for (int i=0; i<height; i++) {
			line = reader.readLine();
			scanner = new Scanner(line);
			for (int j=0; j<width; j++) {
				String code = scanner.findInLine(Pattern.compile(".."));
				if (code==null) {
					throw new ExpectedTokenException("<code>", line);
				}
				
				Unit unit = units.get(code);
				if (unit == null && !code.equals(EMPTY_UNIT) && terrains.get(code) == null) {
					throw new InvalidUnitException(code, line);
				}
				
				if (!code.equals(EMPTY_UNIT) && terrains.get(code) == null) {
					Unit newUnit = new Unit(unit);
					newUnit.setPosition(new Vec2f(j*SQUARE_SIZE, i*SQUARE_SIZE));
					map.addUnit(newUnit, new Vec2i(i, j));
				}
			}
		}
		
		line = reader.readLine();
		if (!line.equals("END")) {
			throw new ExpectedTokenException("END", line);
		}
	}

	private void parseUnits() throws IOException, ExpectedTokenException, InvalidTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("UNITS")) {
			throw new ExpectedTokenException("UNITS", line);
		}
		
		line = reader.readLine();
		while (line!=null && !line.equals("END UNITS")) {
			scanner = new Scanner(line);
			
			if (!scanner.hasNext(Pattern.compile(".."))) {
				throw new ExpectedTokenException("<code>", line);
			}
			String code = scanner.next(Pattern.compile(".."));
			if (code.equals(EMPTY_UNIT)) {
				throw new InvalidTokenException("## is a token reserved to the empty space");
			}
			
			if (!scanner.hasNextBoolean()) {
				throw new ExpectedTokenException("TRUE or FALSE", line);
			}
			boolean computer = scanner.nextBoolean();
			
			Unit unit = new Unit(computer, SQUARE_SIZE);
			units.put(code, unit);
			line = reader.readLine();
		}
		
		if (line==null) {
			throw new ExpectedTokenException("END UNITS", line);
		}
	}

	private void parseMapLayout() throws IOException, InvalidTerrainException, ExpectedTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("START")) {
			throw new ExpectedTokenException("START", line);
		}
		
		int width = map.getWidth();
		int height = map.getHeight();
		for (int i=0; i<height; i++) {
			line = reader.readLine();
			scanner = new Scanner(line);
			for (int j=0; j<width; j++) {
				String code = scanner.findInLine(Pattern.compile(".."));
				if (code==null) {
					throw new ExpectedTokenException("<code>", line);
				}
				
				Terrain terrain = terrains.get(code);
				if (terrain == null) {
					throw new InvalidTerrainException(code, line);
				}
				
				Terrain newTerrain = new Terrain(terrain);
				map.addTerrain(newTerrain, new Vec2i(i, j));
				newTerrain.setPosition(new Vec2f(j*SQUARE_SIZE, i*SQUARE_SIZE));
			}
		}
		
		line = reader.readLine();
		if (!line.equals("END")) {
			throw new ExpectedTokenException("END", line);
		}
	}

	private void parseTerrain() throws IOException, ExpectedTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("TERRAIN")) {
			throw new ExpectedTokenException("TERRAIN", line);
		}
		
		line = reader.readLine();
		while (line!=null && !line.equals("END TERRAIN")) {
			scanner = new Scanner(line);
			
			if (!scanner.hasNext(Pattern.compile(".."))) {
				throw new ExpectedTokenException("<code>", line);
			}
			String code = scanner.next(Pattern.compile(".."));
			
			if (!scanner.hasNextBoolean()) {
				throw new ExpectedTokenException("TRUE or FALSE", line);
			}
			boolean passable = scanner.nextBoolean();
			
			if (!scanner.hasNextBoolean()) {
				throw new ExpectedTokenException("TRUE or FALSE", line);
			}
			boolean projectiles = scanner.nextBoolean();
			
			//String sprite = scanner.next();
			//int column = scanner.nextInt();
			//int row = scanner.nextInt();
			
			Terrain terrain = new Terrain(passable, projectiles, SQUARE_SIZE); 
			terrains.put(code, terrain);
			line = reader.readLine();
		}
		
		if (line==null) {
			throw new ExpectedTokenException("END TERRAIN", line);
		}
	}

	private void parseDimensions() throws IOException, ExpectedTokenException {
		String line;
		line = reader.readLine();
		scanner = new Scanner(line);
		
		if (!scanner.hasNextInt()) {
			throw new ExpectedTokenException("<width>", line);
		}
		int width = scanner.nextInt();
		if (width < 1) {
			throw new ExpectedTokenException("Positive <width>", line);
		}
		
		if (!scanner.hasNextInt()) {
			throw new ExpectedTokenException("<height>", line);
		}
		int height = scanner.nextInt();
		if (height < 1) {
			throw new ExpectedTokenException("Negative <width>", line);
		}
		
		Vec2i matrixDimensions = new Vec2i(width, height);
		Vec2f dimensions = new Vec2f(matrixDimensions.x*SQUARE_SIZE, matrixDimensions.y*SQUARE_SIZE);
		map = new GameMap(new Vec2f(0.0f, 0.0f), dimensions, matrixDimensions);
	}

}
