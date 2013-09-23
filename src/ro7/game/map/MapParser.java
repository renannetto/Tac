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

import ro7.engine.sprites.SpriteSheet;
import ro7.game.exceptions.ExpectedTokenException;
import ro7.game.exceptions.InvalidTerrainException;
import ro7.game.exceptions.InvalidTokenException;
import ro7.game.exceptions.InvalidUnitException;
import ro7.game.model.GameMap;
import ro7.game.model.Terrain;
import ro7.game.model.Unit;
import ro7.game.sprites.UnitSprite;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class MapParser {

	private final float SQUARE_SIZE = 32.0f;
	private final String EMPTY_UNIT = "##";

	private BufferedReader reader;
	private Scanner scanner;

	private GameMap map;

	private Map<String, Terrain> terrains;
	private Map<String, Unit> units;

	private Map<String, SpriteSheet> sheets;

	public MapParser() {
		terrains = new HashMap<String, Terrain>();
		units = new HashMap<String, Unit>();
		sheets = new HashMap<String, SpriteSheet>();
	}

	public GameMap parseMap(File mapFile) {
		try {
			reader = new BufferedReader(new FileReader(mapFile));

			parseDimensions();
			parseSpriteSheets();
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

	private void parseSpriteSheets() throws IOException,
			ExpectedTokenException, InvalidTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("SPRITESHEETS")) {
			throw new ExpectedTokenException("SPRITESHEETS", line);
		}

		line = reader.readLine();
		while (line != null && !line.equals("END SPRITESHEETS")) {
			scanner = new Scanner(line);

			if (!scanner.hasNext()) {
				throw new ExpectedTokenException("<file>", line);
			}
			String file = scanner.next();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<frame width>", line);
			}
			int frameWidth = scanner.nextInt();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<frame height>", line);
			}
			int frameHeight = scanner.nextInt();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<padding x>", line);
			}
			int paddingX = scanner.nextInt();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<padding y>", line);
			}
			int paddingY = scanner.nextInt();

			SpriteSheet sheet = new SpriteSheet(file, new Vec2i(frameWidth,
					frameHeight), new Vec2i(paddingX, paddingY));

			sheets.put(file, sheet);
			line = reader.readLine();
		}

		if (line == null) {
			throw new ExpectedTokenException("END SPRITESHEET", line);
		}
	}

	private void parseUnitsPosition() throws IOException, InvalidUnitException,
			ExpectedTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("START")) {
			throw new ExpectedTokenException("START", line);
		}

		int width = map.getWidth();
		int height = map.getHeight();
		for (int i = 0; i < height; i++) {
			line = reader.readLine();
			scanner = new Scanner(line);
			for (int j = 0; j < width; j++) {
				String code = scanner.findInLine(Pattern.compile(".."));
				if (code == null) {
					throw new ExpectedTokenException("<code>", line);
				}

				Unit unit = units.get(code);
				if (unit == null && !code.equals(EMPTY_UNIT)
						&& terrains.get(code) == null) {
					throw new InvalidUnitException(code, line);
				}

				if (!code.equals(EMPTY_UNIT) && terrains.get(code) == null) {
					Unit newUnit;
					if (unit.isComputer()) {
						newUnit = new ComputerUnit((ComputerUnit) unit, map);
					} else {
						newUnit = new Unit(unit);
					}
					newUnit.move(new Vec2f(j * SQUARE_SIZE, i * SQUARE_SIZE));
					map.addUnit(newUnit, new Vec2i(j, i));
				}
			}
		}

		line = reader.readLine();
		if (!line.equals("END")) {
			throw new ExpectedTokenException("END", line);
		}
	}

	private void parseUnits() throws IOException, ExpectedTokenException,
			InvalidTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("UNITS")) {
			throw new ExpectedTokenException("UNITS", line);
		}

		line = reader.readLine();
		while (line != null && !line.equals("END UNITS")) {
			scanner = new Scanner(line);

			if (!scanner.hasNext(Pattern.compile(".."))) {
				throw new ExpectedTokenException("<code>", line);
			}
			String code = scanner.next(Pattern.compile(".."));
			if (code.equals(EMPTY_UNIT)) {
				throw new InvalidTokenException(
						"## is a token reserved to the empty space");
			}

			if (!scanner.hasNextBoolean()) {
				throw new ExpectedTokenException("TRUE or FALSE", line);
			}
			boolean computer = scanner.nextBoolean();

			if (!scanner.hasNext()) {
				throw new ExpectedTokenException("<sprite>", line);
			}
			String movingSprite = scanner.next();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<column>", line);
			}
			int column = scanner.nextInt();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<row>", line);
			}
			int row = scanner.nextInt();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<int>", line);
			}
			int movingFrames = scanner.nextInt();

			if (!scanner.hasNext()) {
				throw new ExpectedTokenException("<sprite>", line);
			}
			String attackSprite = scanner.next();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<int>", line);
			}
			int attackFrames = scanner.nextInt();

			SpriteSheet movingSheet = sheets.get(movingSprite);
			if (movingSheet == null) {
				throw new InvalidTokenException(movingSprite
						+ " is not a valid sprite sheet");
			}
			Vec2f dimensions = new Vec2f(SQUARE_SIZE, SQUARE_SIZE);
			Vec2i sheetPosition = new Vec2i(column, row);
			UnitSprite moving = new UnitSprite(new Vec2f(0.0f, 0.0f),
					dimensions, false, movingSheet, sheetPosition, movingFrames);
			
			SpriteSheet attackSheet = sheets.get(attackSprite);
			if (attackSheet == null) {
				throw new InvalidTokenException(attackSprite
						+ " is not a valid sprite sheet");
			}
			UnitSprite attacking = new UnitSprite(new Vec2f(0.0f, 0.0f), dimensions, false, attackSheet, new Vec2i(0, 0), attackFrames);

			Unit unit;
			if (computer) {
				unit = new ComputerUnit(new Vec2f(SQUARE_SIZE, SQUARE_SIZE),
						moving, attacking, map);
			} else {
				unit = new Unit(new Vec2f(SQUARE_SIZE, SQUARE_SIZE), moving, attacking);
			}
			units.put(code, unit);
			line = reader.readLine();
		}

		if (line == null) {
			throw new ExpectedTokenException("END UNITS", line);
		}
	}

	private void parseMapLayout() throws IOException, InvalidTerrainException,
			ExpectedTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("START")) {
			throw new ExpectedTokenException("START", line);
		}

		int width = map.getWidth();
		int height = map.getHeight();
		for (int i = 0; i < height; i++) {
			line = reader.readLine();
			scanner = new Scanner(line);
			for (int j = 0; j < width; j++) {
				String code = scanner.findInLine(Pattern.compile(".."));
				if (code == null) {
					throw new ExpectedTokenException("<code>", line);
				}

				Terrain terrain = terrains.get(code);
				if (terrain == null) {
					throw new InvalidTerrainException(code, line);
				}

				Terrain newTerrain = new Terrain(terrain);
				newTerrain.setPosition(new Vec2f(j * SQUARE_SIZE, i
						* SQUARE_SIZE));
				map.addTerrain(newTerrain, new Vec2i(j, i));
			}
		}

		line = reader.readLine();
		if (!line.equals("END")) {
			throw new ExpectedTokenException("END", line);
		}
	}

	private void parseTerrain() throws IOException, ExpectedTokenException,
			InvalidTokenException {
		String line;
		line = reader.readLine();
		if (!line.equals("TERRAIN")) {
			throw new ExpectedTokenException("TERRAIN", line);
		}

		line = reader.readLine();
		while (line != null && !line.equals("END TERRAIN")) {
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

			if (!scanner.hasNext()) {
				throw new ExpectedTokenException("<sprite>", line);
			}
			String sprite = scanner.next();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<column>", line);
			}
			int column = scanner.nextInt();

			if (!scanner.hasNextInt()) {
				throw new ExpectedTokenException("<row>", line);
			}
			int row = scanner.nextInt();

			SpriteSheet sheet = sheets.get(sprite);
			if (sheet == null) {
				throw new InvalidTokenException(sprite
						+ " is not a valid sprite sheet");
			}

			Terrain terrain = new Terrain(passable, projectiles, SQUARE_SIZE,
					sheet, new Vec2i(column, row));
			terrains.put(code, terrain);
			line = reader.readLine();
		}

		if (line == null) {
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
		Vec2f dimensions = new Vec2f(matrixDimensions.x * SQUARE_SIZE,
				matrixDimensions.y * SQUARE_SIZE);
		map = new GameMap(new Vec2f(0.0f, 0.0f), dimensions, matrixDimensions);
	}

}
