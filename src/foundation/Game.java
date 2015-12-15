package foundation;

//HOW TO add new Cell / Block Types, Make the class and update the following:
//Cell CONSTANT / Make sure the new class returns that constant in toInt()
//Cell.typeToCell
//CellButton.draw
//CellMenu.NUM_OF_CELL_TYPES / NUM_OF_BLOCK_TYPES
//Game.loadAllImages
//Game.colorAllCells

//HOW TO add a new Enemy, Make the class and update the following:
//Enemy.NUM_OF_ENEMY_TYPES
//Enemy.CONSTANT / Make sure the new class returns that constant in toInt()
//Add the icon to EnemyMenu.loadImages
//Game.loadAllImages
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cells.*;
import cells.blocktypes.*;
import cells.celltypes.*;
import cellsmenu.*;
import entities.*;
import entities.enemies.BlockDisguise;

public class Game implements Runnable {
	// STATIC VARIABLES
	public static final int FPS = 60;
	public static final double DRAW_NANO_DELAY = 1000000000 / 60.0;// =
																	// 1000000000.0
																	// /
																	// FPS;
	public static final int GRID_SIZE = 32;
	public final static int SECT_LOAD_BORDER = 0;
	public final static int SECT_COUNT_X = 12;
	public final static int SECT_COUNT_Y = 12;
	public final static long RECOLOR_DELAY = 1000000000;
	public static BufferedImage timerSheet;
	public static BufferedImage timerSheetRC;
	public boolean jarMode = false;

	public static Game singleton;

	// CORE VARIABLES
	GameCore gc;
	BufferedImage build;
	BufferedImage show;
	boolean gameLoop = true;
	InputBlock ib;
	long lastDraw;
	public boolean loading = false;
	Thread drawThread;
	LoadingThread ld;

	// GAME VARIABLES
	public static final int TIMER_SIZE = 64;
	public static final int SCREEN_SCROLL_BORDER = 256;
	public boolean showMenus = true;
	double camX = 0;
	double camY = 0;
	public boolean freeCamera = false;
	public boolean autoSave = false;
	private MobileEntity cameraFocus;
	public Sector[][] sectors;
	public int sectIndexX = 0;
	public int sectIndexY = 0;
	Color themeColor;
	CellMenu menu;
	EnemyMenu emenu;
	long flipDelay;
	long nextFlip;
	Rectangle2D.Double dragZone;
	boolean freshDrag = false;
	Player player;
	Point2D.Double checkpointLocation;
	long nextRecolor;

	// Backgrounds
	public static final int BACKGROUND_COUNT = 2;
	public static final int BG_SKY = 0;
	public static final int BG_DIRT = 1;
	public static final double PARALLAX_SCALE = 0.1;
	public static final BufferedImage[] backgrounds = new BufferedImage[BACKGROUND_COUNT];

	// Entity Lists
	ArrayList<MobileEntity> killList;
	ArrayList<Enemy> enemies;
	ArrayList<Shape> playerHitboxes;
	ArrayList<Shape> enemyHitboxes;

	public Game(GameCore applet) {
		this.gc = applet;
		singleton = this;
		init();
		killList = new ArrayList<MobileEntity>();
		enemies = new ArrayList<Enemy>();
		playerHitboxes = new ArrayList<Shape>();
		enemyHitboxes = new ArrayList<Shape>();
	}

	public void init() {
		loadAllImages();
		build = new BufferedImage(gc.getSize().width, gc.getSize().height,
				BufferedImage.TYPE_INT_RGB);
		show = new BufferedImage(gc.getSize().width, gc.getSize().height,
				BufferedImage.TYPE_INT_RGB);

		flipDelay = (long) (2.0 * 1000000000);
		nextFlip = System.nanoTime() + flipDelay;
		nextRecolor = System.nanoTime();

		drawThread = new Thread(new DrawThread(this));

		sectors = new Sector[SECT_COUNT_X][SECT_COUNT_Y];
		loadAllSectors();
	}

	public BufferedImage robustLoadImage(String filename, String errorMessage) {
		BufferedImage ret = null;

		if (jarMode) {
			String filename2 = filename.replaceFirst("rec\\/", "");
			InputStream s = this.getClass().getClassLoader()
					.getResourceAsStream(filename2);
			try {
				ret = ImageIO.read(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (ret == null)
			try {
				jarMode = false;
				ret = ImageIO.read(new File(filename));
			} catch (IOException e2) {
				System.out.println("ERROR - Failed to load image: "
						+ errorMessage);
			}
		return ret;
	}

	public void loadGameImages() {
		timerSheet = robustLoadImage("rec/img/hud/timerNumbers.png",
				"Timer Numbers");
		backgrounds[BG_SKY] = robustLoadImage(
				"rec/img/backgrounds/skyBackground.png", "Sky Background");
		backgrounds[BG_DIRT] = robustLoadImage(
				"rec/img/backgrounds/dirtbackground.png", "Dirt Background");
	}

	public void loadAllImages() {
		loadGameImages();
		Cell.loadImages();
		PoliteSolidBlock.loadImages();
		SolidBlock.loadImages();
		CellButton.loadImage();
		CopyLeftCell.loadImage();
		CopyRightCell.loadImage();
		CopyDownCell.loadImage();
		CopyUpCell.loadImage();
		IsolatedCell.loadImage();
		LifeCell.loadImage();
		ToggleLeftCell.loadImage();
		ToggleDownCell.loadImage();
		ToggleRightCell.loadImage();
		ToggleUpCell.loadImage();
		AlwaysSolidBlock.loadImages();
		NeverSolidBlock.loadImages();
		CheckpointBlock.loadImages();
		DeathBlock.loadImages();
		TimeControlBlock.loadImages();
		TimeControlBlockRed.loadImages();
		TimeControlBlockOrange.loadImages();
		TimeControlBlockYellow.loadImages();
		TimeControlBlockGreen.loadImages();
		TimeControlBlockBlue.loadImages();
		TimeControlBlockPurple.loadImages();
		HatchBlock.loadImages();
		SplitBlock.loadImages();
		RudeSolidBlock.loadImages();
		DiceCell_1.loadImages();
		DiceCell_2.loadImages();
		DiceCell_3.loadImages();
		DiceCell_4.loadImages();
		DiceCell_5.loadImages();
		DiceCell_6.loadImages();
		ButtonCell.loadImage();
		WaitButtonCell.loadImage();
		NotLeftCell.loadImage();
		NotDownCell.loadImage();
		NotRightCell.loadImage();
		NotUpCell.loadImage();
		OrVertCell.loadImage();
		OrHorzCell.loadImage();
		AndVertCell.loadImage();
		AndHorzCell.loadImage();
		PlayerTouchCell.loadImage();
		PlayerTouchNotCell.loadImage();
		OnceOnCell.loadImage();
		OnceOffCell.loadImage();
		Player.loadImages();
		PlayerDroplet.loadImages();
		BlockDisguise.loadImages();
		EnemyMenu.loadImages();
	}

	public void colorAllCells(Color c) {
		Color pc = new Color(56, 234, 145);
		// Color the timer
		timerSheetRC = recolorImage(timerSheet, c);

		// Color cells
		for (int i = 0; i < Cell.PREDICTION_FRAME_COUNT; i++) {
			Cell.glowOnImgRC[i] = Cell.glowOnImg[i];
			Cell.glowOffImgRC[i] = Cell.glowOffImg[i];
			Cell.shimmerOnImgRC[i] = Cell.shimmerOnImg[i];
			Cell.shimmerOffImgRC[i] = Cell.shimmerOffImg[i];
		}
		IsolatedCell.imgRC = recolorImage(IsolatedCell.img, c.darker().darker());
		CopyLeftCell.imgRC = recolorImage(CopyLeftCell.img, c);
		CopyDownCell.imgRC = recolorImage(CopyDownCell.img, c);
		CopyRightCell.imgRC = recolorImage(CopyRightCell.img, c);
		CopyUpCell.imgRC = recolorImage(CopyUpCell.img, c);
		LifeCell.imgRC = recolorImage(LifeCell.img, c);
		ToggleLeftCell.imgRC = recolorImage(ToggleLeftCell.img, c);
		ToggleDownCell.imgRC = recolorImage(ToggleDownCell.img, c);
		ToggleRightCell.imgRC = recolorImage(ToggleRightCell.img, c);
		ToggleUpCell.imgRC = recolorImage(ToggleUpCell.img, c);
		NotLeftCell.imgRC = recolorImage(NotLeftCell.img, c);
		NotDownCell.imgRC = recolorImage(NotDownCell.img, c);
		NotRightCell.imgRC = recolorImage(NotRightCell.img, c);
		NotUpCell.imgRC = recolorImage(NotUpCell.img, c);
		OrVertCell.imgRC = recolorImage(OrVertCell.img, c);
		OrHorzCell.imgRC = recolorImage(OrHorzCell.img, c);
		AndVertCell.imgRC = recolorImage(AndVertCell.img, c);
		AndHorzCell.imgRC = recolorImage(AndHorzCell.img, c);
		PlayerTouchCell.imgRC = recolorImage(PlayerTouchCell.img, pc);
		PlayerTouchNotCell.imgRC = recolorImage(PlayerTouchNotCell.img, pc);
		OnceOnCell.imgRC = recolorImage(OnceOnCell.img, c);
		OnceOffCell.imgRC = recolorImage(OnceOffCell.img, c);

		// Color blocks
		for (int i = 0; i < CheckpointBlock.FRAME_COUNT; i++) {
			CheckpointBlock.activeImgRC[i] = recolorImage(
					CheckpointBlock.activeImg[i], pc);
			CheckpointBlock.inactiveImgRC[i] = recolorImage(
					CheckpointBlock.inactiveImg[i], pc);
		}
	}

	public void updateAllFrames(long currentTime) {
		CheckpointBlock.updateFrame(currentTime);
		SplitBlock.updateFrame(currentTime);
		Cell.updateFrame(currentTime);
		PoliteSolidBlock.updateFrame(currentTime);
		RudeSolidBlock.updateFrame(currentTime);
		HatchBlock.updateFrame(currentTime);
		BlockDisguise.updateFrame(currentTime);
		if (player != null)
			player.updateFrame(currentTime);
	}

	public static BufferedImage recolorImage(BufferedImage bi, Color c) {
		float[] f = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		float hue = f[0];
		float sat = f[1];
		BufferedImage ret = new BufferedImage(bi.getWidth(), bi.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < ret.getWidth(); x++)
			for (int y = 0; y < ret.getHeight(); y++) {
				Color c2 = new Color(bi.getRGB(x, y), true);
				int alpha = c2.getAlpha();
				f = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(),
						null);
				c2 = Color.getHSBColor(hue, sat, f[2]);
				c2 = new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), alpha);
				ret.setRGB(x, y, c2.getRGB());
			}
		return ret;
	}

	public long getFlipDelay() {
		return flipDelay;
	}

	public void loadAllSectors() {
		for (int i = 0; i < SECT_COUNT_X; i++)
			for (int j = 0; j < SECT_COUNT_Y; j++) {
				sectors[i][j] = new Sector();
				loadSect(sectors[i][j], sectIndexX + i, sectIndexY + j);
			}
	}

	public void stop() {
		gameLoop = false;
	}

	public void placeCell(Point2D.Double pos) {
		System.out.println("PLACING CELL");
		System.out.println("XPOS: " + pos.x);
		System.out.println("YPOS: " + pos.y);
		Point2D.Double cellIndex;
		cellIndex = worldToCell(screenToWorld(pos));
		Cell tempCell = getCell((int) cellIndex.x, (int) cellIndex.y);
		int cType = menu.getCellType();
		int bType = menu.getBlockType();
		short tempS = menu.getState();
		if (cType == -1) {
			cType = tempCell.getCellTypeInt();
		}
		if (bType == -1)
			bType = tempCell.getBlockTypeInt();
		if (tempS == -1)
			tempS = tempCell.getState();
		Cell c = Cell.typeToCell(cType, bType, tempS);
		if (c.getBlockType() instanceof HatchBlock) {
			((HatchBlock) c.getBlockType()).setEType(emenu.getEnemyInt());
			System.out.println("ETYPE: "
					+ ((HatchBlock) c.getBlockType()).eType);
		}
		changeCell(pos, c);
	}

	public void setFlipDelay(long delay) {
		flipDelay = delay;
	}

	public void incrementFlipDelay() {
		if (flipDelay < (long) 1000000000 * 8 && flipDelay != -1) {
			flipDelay = flipDelay * 2;
			resetFlip();
		} else if (flipDelay == -1) {
			flipDelay = 250000000;
			resetFlip();
		}
	}

	public void decrementFlipDelay() {
		if (flipDelay > 250000000) {
			flipDelay = (long) (flipDelay / 2.0);
			resetFlip();
		} else if (flipDelay == 250000000) {
			flipDelay = -1;
			resetFlip();
		}
	}

	public void resetFlip() {
		nextFlip = System.nanoTime() + flipDelay;
	}

	public void stopFlipDelay() {
		flipDelay = -1;
		resetFlip();
	}

	public void findCheckpoint() {
		int xIndex, yIndex;

		for (int x = 0; x < sectors.length; x++)
			for (int y = 0; y < sectors[x].length; y++) {
				for (int i = 0; i < Sector.NUMBER_OF_COLS; i++)
					for (int j = 0; j < Sector.NUMBER_OF_ROWS; j++)
						if (sectors[x][y].getCell(i, j).getBlockTypeInt() == 3) {
							xIndex = sectIndexX * SECT_COUNT_X + x
									* Sector.NUMBER_OF_COLS + i;
							yIndex = sectIndexY * SECT_COUNT_Y + y
									* Sector.NUMBER_OF_ROWS + j;
							checkpointLocation = new Point2D.Double(xIndex,
									yIndex);
							return;
						}
			}
	}

	public void respawn() {

		if (checkpointLocation == null)
			findCheckpoint();
		if (checkpointLocation == null)
			return;
		System.out.println("CPX: " + checkpointLocation.x + " CPY: "
				+ checkpointLocation.y);
		System.out.println("DEBUG - RESPAWNING");
		camX = checkpointLocation.x * Cell.WIDTH;
		camY = checkpointLocation.y * Cell.HEIGHT;
		System.out.println("CAMX: " + camX + "  CAMY: " + camY);
		player = new Player(camX, camY);
		camX -= GameCore.NATIVE_X_RES / 2;
		camY = GameCore.NATIVE_Y_RES / 2;
		enemies.clear();
		reloadCells();
	}

	public void setRespawnPoint(Point2D.Double d) {
		checkpointLocation = d;
	}

	public Point2D.Double getRespawnPoint() {
		return checkpointLocation;
	}

	public Player getPlayer() {
		return player;
	}

	public void addEnemy(Enemy e) {
		enemies.add(e);
	}

	public void updateEntities() {

		cameraFocus = player;
		// Update
		if (player != null)
			player.update(ib, this);

		for (Enemy enemy : enemies)
			enemy.update(ib, this);

		// Check hitbox collisions
		if (player != null)
			player.hitboxCheck(this, enemyHitboxes);

		for (Enemy enemy : enemies)
			enemy.hitboxCheck(this, playerHitboxes);

		if (player == null)
			respawn();
	}

	public void reloadCells() {
		if (autoSave)
			saveAll();
		sectIndexX = (int) (camX / (Sector.NUMBER_OF_COLS * Cell.WIDTH));
		sectIndexY = (int) (camY / (Sector.NUMBER_OF_ROWS * Cell.HEIGHT));
		loadAllSectors();
	}

	public void moveCamera(InputBlock ib) {
		if (cameraFocus != null) {
			double xp = cameraFocus.getXPos();
			double xw = cameraFocus.getWidth();
			double yp = cameraFocus.getYPos();
			double yh = cameraFocus.getHeight();
			if (cameraFocus instanceof Player) {
				Point2D.Double p = ((Player) cameraFocus).getDropletCenter();
				xp = p.x;
				yp = p.y;
				xw = 0;
				yh = 0;
			}
			int rate = 1;
			while (xp - rate - SCREEN_SCROLL_BORDER < camX)
				camX = camX - rate;
			while (xp + xw + rate + SCREEN_SCROLL_BORDER > camX
					+ build.getWidth())
				camX = camX + rate;
			while (yp - rate - SCREEN_SCROLL_BORDER < camY)
				camY = camY - rate;
			while (yp + yh + rate + SCREEN_SCROLL_BORDER > camY
					+ build.getHeight())
				camY = camY + rate;
		}
	}

	public void clearHitboxes() {
		playerHitboxes.clear();
		enemyHitboxes.clear();
	}

	public void addPlayerHitbox(Shape s) {
		playerHitboxes.add(s);
	}

	public void addEnemyHitbox(Shape s) {
		enemyHitboxes.add(s);
	}

	public ArrayList<Shape> getPlayerHitboxes() {
		return playerHitboxes;
	}

	public ArrayList<Shape> getEnemyHitboxes() {
		return enemyHitboxes;
	}

	public void tickUpdateCells() {
		for (int x = 0; x < sectors.length; x++)
			for (int y = 0; y < sectors[x].length; y++) {
				for (int i = 0; i < Sector.NUMBER_OF_COLS; i++)
					for (int j = 0; j < Sector.NUMBER_OF_ROWS; j++) {
						double xp = getSectCorner(x, y).x + Cell.WIDTH * i;
						double yp = getSectCorner(x, y).y + Cell.HEIGHT * j;
						sectors[x][y].getCell(i, j).tickUpdate(this, xp, yp);
					}
			}
	}

	public void killEntity(MobileEntity me) {
		killList.add(me);
	}

	public void run() {
		colorAllCells(Color.green);
		if (player == null)
			respawn();
		menu = new CellMenu(new Rectangle2D.Double(
				(int) (build.getWidth() * 0.05),
				(int) (build.getHeight() * -0.05),
				(int) (build.getWidth() * 0.9), (int) (build.getHeight() * 1)));
		emenu = new EnemyMenu(new Rectangle2D.Double(
				(int) (build.getWidth() * 0.05),
				(int) (build.getHeight() * 0.05), (int) (build.getWidth() * 1),
				(int) (build.getHeight() * 0.9)));
		while (gameLoop) {
			for (MobileEntity me : killList) {
				if (me instanceof Player)
					player = null;
				if (me instanceof Enemy) {
					((Enemy) me).getOwner().killEntity(me);
					enemies.remove(me);
				}
			}
			killList.clear();
			ib = gc.getInput();
			clearHitboxes();

			// Update Menu
			if (showMenus)
				menu.update(ib, this);
			emenu.update(ib, this);

			// Place Cells
			if (ib.mouseLCPrime && !menu.menuContains(ib.mousePos)
					&& !emenu.menuContains(ib.mousePos)) {
				placeCell(ib.mousePos);
			}
			// MASS PLACE CELLS
			freshDrag = false;
			if (ib.mouseRCPrime && !menu.menuContains(ib.mousePos)
					&& dragZone == null) {
				dragZone = new Rectangle2D.Double(ib.mouseX, ib.mouseY, 0, 0);
				freshDrag = true;
			}
			if (dragZone != null) {
				dragZone.width = ib.mouseX - dragZone.x;
				dragZone.height = ib.mouseY - dragZone.y;
				Rectangle2D.Double dragZone2 = new Rectangle2D.Double();
				dragZone2.setRect(dragZone);
				if (dragZone2.width < 0) {
					dragZone2.x += dragZone2.width;
					dragZone2.width = dragZone2.width * -1;
				}
				if (dragZone2.height < 0) {
					dragZone2.y += dragZone2.height;
					dragZone2.height = dragZone2.height * -1;
				}

				if (ib.mouseRCPrime && !freshDrag) {
					int xChange = Cell.WIDTH;
					int yChange = Cell.HEIGHT;
					if (dragZone2.width < 0)
						xChange = xChange * -1;
					if (dragZone2.height < 0)
						yChange = yChange * -1;
					for (double x = dragZone2.x; x < dragZone2.x
							+ dragZone2.width; x += Cell.WIDTH)
						for (double y = dragZone2.y; y < dragZone2.y
								+ dragZone2.height; y += Cell.HEIGHT) {
							placeCell(new Point2D.Double(x, y));
						}
					for (double x = dragZone2.x; x < dragZone2.x
							+ dragZone2.width; x += Cell.WIDTH)
						placeCell(new Point2D.Double(x, dragZone2.y
								+ dragZone2.height));
					for (double y = dragZone2.y; y < dragZone2.y
							+ dragZone2.height; y += Cell.HEIGHT)
						placeCell(new Point2D.Double(dragZone2.x
								+ dragZone2.width, y));
					placeCell(new Point2D.Double(dragZone2.x + dragZone2.width,
							dragZone2.y + dragZone2.height));
					dragZone = null;
				}
			}

			// Camera Controls (For development only)
			if (freeCamera) {
				if (ib.keyUp)
					camY -= 400 * ib.delta;
				else if (ib.keyDown)
					camY += 400 * ib.delta;
				if (ib.keyRight)
					camX += 400 * ib.delta;
				else if (ib.keyLeft)
					camX -= 400 * ib.delta;
			} else
				moveCamera(ib);

			tickUpdateCells();
			updateEntities();

			// Destroy polite solid blocks (dirt)
			for (Shape s : playerHitboxes) {
				if (s instanceof Rectangle2D) {
					breakCells((Rectangle2D.Double) s);
				}
			}

			updateCamera();
			updateTimer();
			// Determine if enough time has passed to draw the next frame
			if (ib.currentTime > lastDraw + DRAW_NANO_DELAY) {
				lastDraw = ib.currentTime;
				updateGraphics(gc.getGraphics());
			}
		}

	}

	public void drawBackground(BufferedImage build, BufferedImage src,
			double alpha) {
		Graphics g = build.getGraphics();

		double srcX1 = (camX * PARALLAX_SCALE) % src.getWidth();
		double srcY1 = (camY * PARALLAX_SCALE) % src.getHeight();
		if (srcX1 < 0)
			srcX1 = src.getWidth() - (srcX1 * -1);
		if (srcY1 < 0)
			srcY1 = src.getHeight() - (srcY1 * -1);

		Rectangle2D.Double srcR = new Rectangle2D.Double(srcX1, srcY1,
				build.getWidth(), build.getHeight());
		Rectangle2D.Double srcRx = null;
		Rectangle2D.Double srcRy = null;
		Rectangle2D.Double srcRxy = null;
		Rectangle2D.Double dstR = new Rectangle2D.Double();
		Rectangle2D.Double dstRx = null;
		Rectangle2D.Double dstRy = null;
		Rectangle2D.Double dstRxy = null;
		dstR.x = 0;
		dstR.y = 0;
		dstR.width = build.getWidth();
		dstR.height = build.getHeight();

		if (srcR.x + srcR.width > src.getWidth()
				&& srcR.y + srcR.height > src.getHeight()) {
			double overX = (srcR.x + srcR.width) - src.getWidth();
			double overY = (srcR.y + srcR.height) - src.getHeight();
			srcR.width -= overX;
			srcR.height -= overY;
			dstR.width -= overX;
			dstR.height -= overY;

			srcRxy = new Rectangle2D.Double(0, 0, overX, overY);
			srcRx = new Rectangle2D.Double(0, srcR.y, overX, srcR.height);
			srcRy = new Rectangle2D.Double(srcR.x, 0, srcR.width, overY);

			dstRxy = new Rectangle2D.Double(dstR.x + dstR.width, dstR.y
					+ dstR.height, overX, overY);
			dstRx = new Rectangle2D.Double(dstR.x + dstR.width, 0, overX,
					dstR.height);
			dstRy = new Rectangle2D.Double(0, dstR.y + dstR.height, dstR.width,
					overY);
		} else if (srcR.x + srcR.width > src.getWidth()) {
			double overX = (srcR.x + srcR.width) - src.getWidth();
			srcR.width -= overX;
			dstR.width -= overX;

			srcRx = new Rectangle2D.Double(0, srcR.y, overX, srcR.height);

			dstRx = new Rectangle2D.Double(dstR.x + dstR.width, 0, overX,
					dstR.height);
		} else if (srcR.y + srcR.height > src.getHeight()) {
			double overY = (srcR.y + srcR.height) - src.getHeight();
			srcR.height -= overY;
			dstR.height -= overY;

			srcRy = new Rectangle2D.Double(srcR.x, 0, srcR.width, overY);

			dstRy = new Rectangle2D.Double(0, dstR.y + dstR.height, dstR.width,
					overY);

		}

		g.setColor(Color.RED);
		g.fillRect((int) dstR.x, (int) dstR.y, (int) dstR.width,
				(int) dstR.height);
		g.setColor(Color.BLUE);
		if (dstRx != null)
			g.fillRect((int) dstRx.x, (int) dstRx.y, (int) dstRx.width,
					(int) dstRx.height);
		g.setColor(Color.YELLOW);
		if (dstRy != null)
			g.fillRect((int) dstRy.x, (int) dstRy.y, (int) dstRy.width,
					(int) dstRy.height);
		g.setColor(Color.GREEN);
		if (dstRxy != null)
			g.fillRect((int) dstRxy.x, (int) dstRxy.y, (int) dstRxy.width,
					(int) dstRxy.height);

		g.drawImage(src, (int) dstR.x, (int) dstR.y,
				(int) (dstR.x + dstR.width), (int) (dstR.x + dstR.height),
				(int) srcR.x, (int) srcR.y, (int) (srcR.x + srcR.width),
				(int) (srcR.y + srcR.height), null);
		if (dstRx != null)
			g.drawImage(src, (int) dstRx.x, (int) dstRx.y,
					(int) (dstRx.x + dstRx.width),
					(int) (dstRx.y + dstRx.height), (int) srcRx.x,
					(int) srcRx.y, (int) (srcRx.x + srcRx.width),
					(int) (srcRx.y + srcRx.height), null);
		if (dstRy != null)
			g.drawImage(src, (int) dstRy.x, (int) dstRy.y,
					(int) (dstRy.x + dstRy.width),
					(int) (dstRy.y + dstRy.height), (int) srcRy.x,
					(int) srcRy.y, (int) (srcRy.x + srcRy.width),
					(int) (srcRy.y + srcRy.height), null);
		if (dstRxy != null)
			g.drawImage(src, (int) dstRxy.x, (int) dstRxy.y,
					(int) (dstRxy.x + dstRxy.width),
					(int) (dstRxy.y + dstRxy.height), (int) srcRxy.x,
					(int) srcRxy.y, (int) (srcRxy.x + srcRxy.width),
					(int) (srcRxy.y + srcRxy.height), null);
	}

	public void drawBackground(BufferedImage build) {
		BufferedImage src2 = backgrounds[BG_SKY];
		BufferedImage src1 = backgrounds[BG_DIRT];
		// TODO - Decide how I will determine which background is displayed.
		// And make it display more than one background.
		drawBackground(build, src2, 1);
	}

	public void updateGraphics(Graphics g) {
		themeColor = Color.green;
		updateAllFrames(ib.currentTime);
		// TODO - This feature slows the game down a bit.
		// For now, it is disabled. Get around to either
		// making it more efficient
		// or deleting it altogether.

		/*
		 * if (ib.currentTime > nextRecolor) { // colorAllCells(themeColor);
		 * nextRecolor += RECOLOR_DELAY; }
		 */

		Graphics bg = build.getGraphics();
		bg.setColor(Color.BLACK);
		bg.fillRect(0, 0, build.getWidth(), build.getHeight());
		bg.setColor(Color.BLUE);
		bg.setColor(Color.BLACK);
		drawBackground(build);
		drawCells(build, themeColor);
		if (showMenus) {
			menu.draw(build);
			emenu.draw(build);
		}

		// Draw Entities
		if (player != null)
			player.updateGraphics(build, this);
		for (Enemy e : enemies)
			e.updateGraphics(build, this);

		// Draw HUD INFO
		drawTimer(bg, themeColor, 120, 120, TIMER_SIZE);

		if (dragZone != null) {
			Rectangle2D.Double dragZone2 = new Rectangle2D.Double();
			dragZone2.setRect(dragZone);
			if (dragZone2.width < 0) {
				dragZone2.x += dragZone2.width;
				dragZone2.width = dragZone2.width * -1;
			}
			if (dragZone2.height < 0) {
				dragZone2.y += dragZone2.height;
				dragZone2.height = dragZone2.height * -1;
			}
			bg.setColor(Color.BLUE);
			bg.drawRect((int) dragZone2.x, (int) dragZone2.y,
					(int) dragZone2.width, (int) dragZone2.height);
		}
		// TODO - This code is for testing purposes only. It should be commented
		// out when not testing.
		/*
		 * bg.setColor(Color.red); for(Shape s : playerHitboxes) { if(s
		 * instanceof Rectangle2D) bg.drawRect((int)(((Rectangle2D.Double)s).x -
		 * camX), (int)(((Rectangle2D.Double)s).y - camY),
		 * ((int)((Rectangle2D.Double)s).width),
		 * ((int)((Rectangle2D.Double)s).height)); } bg.setColor(Color.orange);
		 * for(Shape s: enemyHitboxes) {
		 * 
		 * if(s instanceof Rectangle2D) { Rectangle2D.Double s2 =
		 * ((Rectangle2D.Double)s); bg.drawRect((int)(s2.x - camX), (int)(s2.y -
		 * camY), (int)s2.width, (int)s2.height);
		 * 
		 * } }
		 */

		// Draw Text Overlay
		if (showMenus) {
			String s = "CamX: " + (int) camX + "  |  CamY: " + (int) camY;
			char[] c = s.toCharArray();
			bg.setColor(Color.ORANGE);
			bg.drawChars(c, 0, c.length, 32, 32);
			s = "FPS: " + (int) (1000000000.0 / (ib.currentTime - ib.lastTime));
			if ((int) (1000000000.0 / (ib.currentTime - ib.lastTime)) < 15)
				System.out.println("LOW FPS POINT: " + System.nanoTime());
			c = s.toCharArray();
			bg.drawChars(c, 0, c.length, 32, 64);
		}

		flip(g);

	}

	public void flip(Graphics g) {
		BufferedImage tmp;
		tmp = show;
		show = build;
		build = tmp;
		g.drawImage(show, 0, 0, null);
	}

	public void updateCells() {

		// Set last state
		for (int x = 0; x < sectors.length; x++)
			for (int y = 0; y < sectors[x].length; y++) {
				for (int i = 0; i < Sector.NUMBER_OF_COLS; i++)
					for (int j = 0; j < Sector.NUMBER_OF_ROWS; j++)
						sectors[x][y].getCell(i, j).updateLastState();
			}

		Point2D.Double p = worldToCell(getSectCorner());

		// Change to new state
		for (int x = 0; x < sectors.length; x++)
			for (int y = 0; y < sectors[x].length; y++) {
				for (int i = 0; i < Sector.NUMBER_OF_COLS; i++)
					for (int j = 0; j < Sector.NUMBER_OF_ROWS; j++)
						sectors[x][y].getCell(i, j).update(
								(int) p.x + x * Sector.NUMBER_OF_COLS + i,
								(int) p.y + y * Sector.NUMBER_OF_COLS + j,
								this, player);

			}

		// Set last state again (because entity calculations, handled elsewhere,
		// require this to be done before & after setting the new state).
		for (int x = 0; x < sectors.length; x++)
			for (int y = 0; y < sectors[x].length; y++) {
				for (int i = 0; i < Sector.NUMBER_OF_COLS; i++)
					for (int j = 0; j < Sector.NUMBER_OF_ROWS; j++)
						sectors[x][y].getCell(i, j).updateLastState();
			}

		// Predict next state (used for glow effects)
		for (int x = 0; x < sectors.length; x++)
			for (int y = 0; y < sectors[x].length; y++) {
				for (int i = 0; i < Sector.NUMBER_OF_COLS; i++)
					for (int j = 0; j < Sector.NUMBER_OF_ROWS; j++)
						sectors[x][y].getCell(i, j)
								.predict(
										(int) p.x + x * Sector.NUMBER_OF_COLS
												+ i,
										(int) p.y + y * Sector.NUMBER_OF_COLS
												+ j, this);

			}

	}

	public void updateTimer() {
		if (flipDelay == -1)
			return;
		if (ib.currentTime >= nextFlip) {
			nextFlip += flipDelay;
			updateCells();
		}
	}

	public double getTimerPercent() {
		return Math.max(
				Math.min((nextFlip - ib.currentTime) / (double) flipDelay, 1),
				0);
	}

	public double getTimerSecondPercent() {
		return Math.max(
				Math.min(((nextFlip - ib.currentTime) % 1000000000)
						/ (double) Math.min(flipDelay, 1000000000), 1), 0);
	}

	public boolean timerOnLastSecond() {
		return (ib.currentTime + 1000000000 > nextFlip);
	}

	public void drawTimer(Graphics g, Color c, int xPos, int yPos, int size) {
		size = size / 2;
		int radius = size;
		Color c2 = new Color((float) (c.getRed() / 255.0),
				(float) (c.getGreen() / 255.0), (float) (c.getBlue() / 255.0),
				(float) 0.25);
		g.setColor(c2);
		g.fillOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
		g.setColor(c);
		g.drawOval(xPos - radius, yPos - radius, radius * 2, radius * 2);

		// Draw growing circles
		radius = (int) (size * (1 - getTimerSecondPercent()));
		if (timerOnLastSecond()) {
			c2 = new Color((float) (c.getRed() / 255.0),
					(float) (c.getGreen() / 255.0),
					(float) (c.getBlue() / 255.0), (float) 0.5);
			g.setColor(c2);
			g.fillOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
			g.setColor(c);
		} else {
			c2 = new Color((float) (c.getRed() / 255.0),
					(float) (c.getGreen() / 255.0),
					(float) (c.getBlue() / 255.0), (float) 0.1);
			g.setColor(c2);
			g.fillOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
			g.setColor(c.darker());
		}

		g.drawOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
		if (timerOnLastSecond()) {

			radius -= 1;
			g.drawOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
			g.setColor(c.darker());
			radius -= 1;
			g.drawOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
			radius -= 1;
			g.drawOval(xPos - radius, yPos - radius, radius * 2, radius * 2);

		}

		int timerImageYOffset = 0;
		if (flipDelay == -1)
			timerImageYOffset = 64 * 1;
		else if (flipDelay == 250000000)
			timerImageYOffset = 64 * 2;
		else if (flipDelay == 500000000)
			timerImageYOffset = 64 * 3;
		else if (flipDelay == 1000000000)
			timerImageYOffset = 64 * 4;
		else if (flipDelay == (long) (1000000000) * 2)
			timerImageYOffset = 64 * 5;
		else if (flipDelay == (long) (1000000000) * 4)
			timerImageYOffset = 64 * 6;
		else if (flipDelay == (long) (1000000000) * 8)
			timerImageYOffset = 64 * 7;
		else
			timerImageYOffset = 0;

		g.drawImage(timerSheetRC, xPos - TIMER_SIZE / 2, yPos - TIMER_SIZE / 2,
				xPos + TIMER_SIZE / 2, yPos + TIMER_SIZE / 2, 0,
				timerImageYOffset, timerSheet.getWidth(), timerImageYOffset
						+ timerSheet.getWidth(), null);
	}

	// TODO - This effect is not currently being used. Consider deleting.
	public void drawGridLines(Graphics g, Color c) {
		g.setColor(c);
		for (int x = (int) ((GRID_SIZE) - (camX % GRID_SIZE)) - GRID_SIZE; x < build
				.getWidth(); x = x + GRID_SIZE)
			g.drawLine((int) x, 0, (int) x, build.getHeight());
		for (int y = (int) ((GRID_SIZE) - (camY % GRID_SIZE)) - GRID_SIZE; y < build
				.getHeight(); y = y + GRID_SIZE)
			g.drawLine(0, (int) y, build.getWidth(), (int) y);
		g.setColor(c.darker().darker());
		for (int x = (int) ((GRID_SIZE) - (camX % GRID_SIZE)) - GRID_SIZE; x < build
				.getWidth(); x = x + GRID_SIZE)
			g.drawLine((int) x + 1, 0, (int) x + 1, build.getHeight());
		for (int y = (int) ((GRID_SIZE) - (camY % GRID_SIZE)) - GRID_SIZE; y < build
				.getHeight(); y = y + GRID_SIZE)
			g.drawLine(0, (int) y + 1, build.getWidth(), (int) y + 1);

		for (int x = (int) ((GRID_SIZE) - (camX % GRID_SIZE)) - GRID_SIZE; x < build
				.getWidth(); x = x + GRID_SIZE)
			g.drawLine((int) x - 1, 0, (int) x - 1, build.getHeight());
		for (int y = (int) ((GRID_SIZE) - (camY % GRID_SIZE)) - GRID_SIZE; y < build
				.getHeight(); y = y + GRID_SIZE)
			g.drawLine(0, (int) y - 1, build.getWidth(), (int) y - 1);

	}

	public void drawCells(BufferedImage build, Color themeColor) {
		for (int i = 0; i < SECT_COUNT_X; i++)
			for (int j = 0; j < SECT_COUNT_Y; j++) {
				sectors[i][j].drawCells(build,
						worldToScreen(getSectCorner(i, j)), null, themeColor,
						this);
			}
	}

	public static int sectWidth() {
		return Sector.NUMBER_OF_COLS * Cell.WIDTH;
	}

	public static int sectHeight() {
		return Sector.NUMBER_OF_ROWS * Cell.HEIGHT;
	}

	// Returns the world coordinate of the top left corner of the top left
	// sector.
	public Point2D.Double getSectCorner() {
		return new Point2D.Double((sectIndexX) * sectWidth(), (sectIndexY)
				* sectHeight());
	}

	// Returns the world coordinate of the top left corner of the specified
	// sector.
	private Point2D.Double getSectCorner(int i, int j) {
		Point2D.Double rValue = new Point2D.Double();
		rValue.x = ((sectIndexX + i) * sectWidth());
		rValue.y = ((sectIndexY + j) * sectHeight());
		return rValue;
	}

	public void updateCamera() {
		if (!loading) {
			if (camX < getSectCorner().x + SECT_LOAD_BORDER)
				shiftSectorsLeft();
			else if (camX + gc.getWidth() + SECT_LOAD_BORDER > (getSectCorner().x + SECT_COUNT_X
					* sectWidth()) - 32)
				shiftSectorsRight();
			if (camY < getSectCorner().y + SECT_LOAD_BORDER)
				shiftSectorsUp();
			else if (camY + gc.getHeight() + SECT_LOAD_BORDER > (getSectCorner().y + SECT_COUNT_Y
					* sectHeight()) - 32)
				shiftSectorsDown();
		}
	}

	// TODO - Determine which method of shifting/loading is better: with or
	// without
	// threads. shiftSectorsLeft() currently uses threads. The other three
	// shifts
	// do not use threads at current.

	private void shiftSectorsLeft() {
		loading = true;

		ld = new LoadingThread(null, 0, 0, this, 1);
		ld.setPriority(ld.getPriority() + 1);
		new Thread(ld).start();
		// This is the code for loading without threads, kept in a comment
		// incase I want to change it back (because threads don't appear to
		// help).
		/*
		 * if (autoSave) for (int j = 0; j < SECT_COUNT_Y; j++) {
		 * saveSect(sectors[SECT_COUNT_X - 1][j], sectIndexX + SECT_COUNT_X - 1,
		 * sectIndexY + j); }
		 * 
		 * for (int i = SECT_COUNT_X - 1; i > 0; i--) { for (int j = 0; j <
		 * SECT_COUNT_Y; j++) sectors[i][j] = sectors[i - 1][j]; }
		 * 
		 * sectIndexX--;
		 * 
		 * for (int j = 0; j < SECT_COUNT_Y; j++) { sectors[0][j] = new
		 * Sector(); loadSect(sectors[0][j], sectIndexX, sectIndexY + j); }
		 */
	}

	private void shiftSectorsRight() {
		loading = true;

		if (autoSave)
			for (int j = 0; j < SECT_COUNT_Y; j++) {
				saveSect(sectors[0][j], sectIndexX, sectIndexY + j);
			}
		for (int i = 0; i < SECT_COUNT_X - 1; i++) {
			for (int j = 0; j < SECT_COUNT_Y; j++)
				sectors[i][j] = sectors[i + 1][j];
		}

		sectIndexX++;

		for (int j = 0; j < SECT_COUNT_Y; j++) {
			sectors[SECT_COUNT_X - 1][j] = new Sector();
			loadSect(sectors[SECT_COUNT_X - 1][j], sectIndexX + SECT_COUNT_X
					- 1, sectIndexY + j);
		}
		loading = false;
	}

	private void shiftSectorsUp() {
		loading = true;

		if (autoSave)
			for (int i = 0; i < SECT_COUNT_X; i++) {
				saveSect(sectors[i][SECT_COUNT_Y - 1], sectIndexX + i,
						sectIndexY + SECT_COUNT_Y - 1);
			}
		for (int j = SECT_COUNT_Y - 1; j > 0; j--) {
			for (int i = 0; i < SECT_COUNT_X; i++)
				sectors[i][j] = sectors[i][j - 1];
		}

		sectIndexY--;

		for (int i = 0; i < SECT_COUNT_X; i++) {
			sectors[i][0] = new Sector();
			loadSect(sectors[i][0], sectIndexX + i, sectIndexY);
		}
		loading = false;
	}

	private void shiftSectorsDown() {
		loading = true;

		if (autoSave)
			for (int i = 0; i < SECT_COUNT_X; i++) {
				saveSect(sectors[i][0], sectIndexX + i, sectIndexY);
			}
		for (int j = 0; j < SECT_COUNT_Y - 1; j++) {
			for (int i = 0; i < SECT_COUNT_X; i++)
				sectors[i][j] = sectors[i][j + 1];
		}

		sectIndexY++;

		for (int i = 0; i < SECT_COUNT_X; i++) {
			sectors[i][SECT_COUNT_Y - 1] = new Sector();
			loadSect(sectors[i][SECT_COUNT_Y - 1], sectIndexX + i, sectIndexY
					+ SECT_COUNT_Y - 1);
		}
		loading = false;
	}

	public void loadSect(Sector s, int indexX, int indexY) {

		try {
			String fn = "rec/data/worlds/" + indexX + "x" + indexY + "y.sect";
			LoadingHelper lh = new LoadingHelper(fn);
			s.load(lh);
		} catch (IOException e) {
			System.out.println("DEBUG - LOAD FAILED - CREATING NEW SECTOR");

			s = new Sector();
			s.clear();
			String filen = "rec/data/worlds/" + indexX + "x" + indexY
					+ "y.sect";
			try {
				LoadingHelper lh = new LoadingHelper(filen);
				s.save(lh);
			} catch (Exception e1) {
				System.out.println("ERROR - You can't even make a new Sector?");
				// TODO - What should a jar do when it needs to make a new
				// sector? Crash? Ignore? Make a default sector, but not save
				// it?
			}
		}

	}

	public void saveSect(Sector s, int indexX, int indexY) {
		String filename = "rec/data/worlds/" + indexX + "x" + indexY + "y.sect";
		File dir = new File("rec/data/worlds/");
		if (!dir.exists())
			dir.mkdirs();
		try {
			LoadingHelper lh = new LoadingHelper(filename);
			System.out.println("DEBUG - Saving Sect: " + indexX + "X, "
					+ indexY + "Y");
			s.save(lh);
			lh.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveAll() {
		System.out.println("SAVING ALL");
		for (int i = 0; i < SECT_COUNT_X; i++)
			for (int j = 0; j < SECT_COUNT_Y; j++) {
				saveSect(sectors[i][j], sectIndexX + i, sectIndexY + j);
			}
	}

	public void breakCell(Point2D.Double blockID) {
		int blockIDX = (int) blockID.x;
		int blockIDY = (int) blockID.y;
		int quadIDX;
		int quadIDY;
		quadIDX = blockIDX / Sector.NUMBER_OF_COLS;
		blockIDX = blockIDX % Sector.NUMBER_OF_COLS;
		quadIDY = blockIDY / Sector.NUMBER_OF_ROWS;
		blockIDY = blockIDY % Sector.NUMBER_OF_ROWS;
		try {
			Cell tempCell = sectors[quadIDX][quadIDY].getCell(blockIDX,
					blockIDY);
			if (tempCell.getBlockTypeInt() == 0)
				sectors[quadIDX][quadIDY].setState(blockIDX, blockIDY,
						(short) 0);
		} catch (ArrayIndexOutOfBoundsException e) {
			//This is expecting to happen sometimes.  The correct response
			//is to ignore it.  This catch is intentionally left blank.
		}

	}

	public void healCell(Point2D.Double blockID) {
		int blockIDX = (int) blockID.x;
		int blockIDY = (int) blockID.y;
		int quadIDX;
		int quadIDY;
		quadIDX = blockIDX / Sector.NUMBER_OF_COLS;
		blockIDX = blockIDX % Sector.NUMBER_OF_COLS;
		quadIDY = blockIDY / Sector.NUMBER_OF_ROWS;
		blockIDY = blockIDY % Sector.NUMBER_OF_ROWS;
		try {
			Cell tempCell = sectors[quadIDX][quadIDY].getCell(blockIDX,
					blockIDY);
			if (tempCell.getBlockTypeInt() == 0)
				sectors[quadIDX][quadIDY].setState(blockIDX, blockIDY,
						(short) 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			//This is expecting to happen sometimes.  The correct response
			//is to ignore it.  This catch is intentionally left blank.
		}

	}

	public void breakCells(Rectangle2D.Double rect) {
		ArrayList<Point2D.Double> ar = getCollidingCells(rect);
		for (Point2D.Double p : ar)
			breakCell(p);
	}

	public void changeCell(Point2D.Double mousePos, Cell newCell) {
		Point2D.Double blockID = worldToCell(screenToWorld(mousePos));
		System.out.println("DEBUG - Changing Block: " + blockID.x + "x, "
				+ blockID.y + "y");
		int blockIDX = (int) blockID.x;
		int blockIDY = (int) blockID.y;
		int quadIDX;
		int quadIDY;
		quadIDX = blockIDX / Sector.NUMBER_OF_COLS;
		blockIDX = blockIDX % Sector.NUMBER_OF_COLS;
		quadIDY = blockIDY / Sector.NUMBER_OF_ROWS;
		blockIDY = blockIDY % Sector.NUMBER_OF_ROWS;
		System.out.println("CAMX: " + camX + ", CAMY: " + camY);
		sectors[quadIDX][quadIDY].changeCell(blockIDX, blockIDY, newCell);
	}

	public Point2D.Double screenToWorld(Point2D.Double screenCoordinate) {
		return new Point2D.Double(camX + screenCoordinate.x, camY
				+ screenCoordinate.y);
	}

	public Point2D.Double worldToScreen(Point2D.Double worldCoordinate) {
		return new Point2D.Double(worldCoordinate.x - camX, worldCoordinate.y
				- camY);
	}

	public Point2D.Double worldToCell(Point2D.Double worldCoordinate) {
		Point2D.Double localCoord = new Point2D.Double(worldCoordinate.x
				- getSectCorner().x, worldCoordinate.y - getSectCorner().y);
		localCoord.x = localCoord.x / Cell.WIDTH;
		localCoord.y = localCoord.y / Cell.HEIGHT;
		return localCoord;
	}

	public Point2D.Double cellToWorld(Point2D.Double cellID) {

		return new Point2D.Double(getSectCorner().x + Cell.WIDTH * cellID.x,
				getSectCorner().y + Cell.HEIGHT * cellID.y);

	}

	public Cell getCell(int cellIDX, int cellIDY) {
		int quadIDX;
		int quadIDY;
		quadIDX = cellIDX / Sector.NUMBER_OF_COLS;
		cellIDX = cellIDX % Sector.NUMBER_OF_COLS;
		quadIDY = cellIDY / Sector.NUMBER_OF_ROWS;
		cellIDY = cellIDY % Sector.NUMBER_OF_ROWS;
		try {
			return sectors[quadIDX][quadIDY].getCell(cellIDX, cellIDY);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public short getCellLastState(int cellIDX, int cellIDY) {
		int quadIDX;
		int quadIDY;
		quadIDX = cellIDX / Sector.NUMBER_OF_COLS;
		cellIDX = cellIDX % Sector.NUMBER_OF_COLS;
		quadIDY = cellIDY / Sector.NUMBER_OF_ROWS;
		cellIDY = cellIDY % Sector.NUMBER_OF_ROWS;
		if (quadIDX > sectors.length)
			return -1;
		if (quadIDY > sectors[0].length)
			return -1;
		if (quadIDX < 0)
			return -1;
		if (quadIDY < 0)
			return -1;
		try {
			return sectors[quadIDX][quadIDY].getCell(cellIDX, cellIDY)
					.getLastState();
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}

	public ArrayList<Point2D.Double> getCollidingCells(MobileEntity me) {
		Rectangle2D.Double r = (Rectangle2D.Double) me.getHitbox2D();
		return getCollidingCells(r);

	}

	public ArrayList<Point2D.Double> getCollidingCells(Rectangle2D.Double rect) {
		ArrayList<Point2D.Double> ret = new ArrayList<Point2D.Double>();
		double xmin = rect.x;
		double xmax = xmin + rect.width;
		double ymin = rect.y;
		double ymax = ymin + rect.height;
		for (double x = xmin; x <= xmax; x += Cell.WIDTH) {
			for (double y = ymin; y <= ymax; y += Cell.HEIGHT) {
				Point2D.Double blockID = worldToCell(new Point2D.Double(x, y));
				ret.add(blockID);
			}

		}

		double x = xmax;
		for (double y = ymin; y <= ymax; y += Cell.HEIGHT) {
			Point2D.Double blockID = worldToCell(new Point2D.Double(x, y));
			ret.add(blockID);
		}
		double y = ymax;
		for (double x2 = xmin; x2 <= xmax; x2 += Cell.WIDTH) {
			Point2D.Double blockID = worldToCell(new Point2D.Double(x2, y));
			ret.add(blockID);
		}

		Point2D.Double blockID = worldToCell(new Point2D.Double(x, y));
		ret.add(blockID);
		return ret;

	}

	public int getEnemyType() {
		return emenu.getEnemyInt();
	}

	public Enemy makeEnemy(double xPos, double yPos, HatchBlock hb, int eType) {
		return emenu.makeEnemy(xPos, yPos, hb, eType);
	}

	public static Game getGame() {
		return singleton;
	}

}
