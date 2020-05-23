/*
*	Purpose:	A twist on the classic Helicopter Game
*	Filename:	Game.java
*	Author:		Siddharth Kapoor
*	Date:		April 18, 2018
*/

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.applet.Applet;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;


import javax.swing.JPanel;

public class Game {
// just the main method that creates the jframe and that's it
    public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				try {
					createAndShowGUI();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private static void createAndShowGUI() throws IOException {
		System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
		final JFrame f = new JFrame("Copter Crash");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel());
		f.pack();
		f.setVisible(true);
	}

}

class MyPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
	// initating all variables needed
	private static final Component MyPanel = null;
	BufferedImage blockPicture, background, lose, wallPicture, starPicture, laserPicture, titleScreen,
			instructionsScreen;
	ImageIcon heliPic;
	ArrayList<Block> blocks = new ArrayList(120);
	ArrayList<Wall> walls = new ArrayList(10);
	ArrayList<Star> stars = new ArrayList(10);
	ArrayList<Laser> lasers = new ArrayList(10);
	String screen = "title";
	int prevRandom = 3, randomIndex, yCord, maxDistance, score = 0;
	Helicopter helicopter;
	Timer timer;
	int timerTick = 0;
	int laserDelay = 200;
	boolean goingUp = false, collision = false, drawHighScore = false, start = false;
	int shiftValue = 3;
	Rectangle helicopterRect, blockRect, wallRect, starRect, laserRect;
	Random generator = new Random();
	Button startOver;

	public MyPanel() {
		// adding necessary listeners, and creating the playing field on initialization
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		this.addMouseListener(this);
		timer = new Timer(15, this);
		setBorder(BorderFactory.createLineBorder(Color.black));
		generateTopBlocks();
		generateBotBlocks();
		// generateWall();
		load(new File("best.txt"));
		// loading files
		try {
			blockPicture = ImageIO.read(new File("images/block.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			background = ImageIO.read(new File("images/bg-dark.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lose = ImageIO.read(new File("images/game-over.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			wallPicture = ImageIO.read(new File("images/wall.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			starPicture = ImageIO.read(new File("images/star.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			laserPicture = ImageIO.read(new File("images/laser.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			titleScreen = ImageIO.read(new File("images/copter-home.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			instructionsScreen = ImageIO.read(new File("images/copter-rules.png"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if (timerTick%1 == 0){ generateNewTopBlock(); generateNewBotBlock(); }
		 */
		heliPic = new ImageIcon("images/helicopter.gif");

		helicopter = new Helicopter(100, 300);
	}

	public Dimension getPreferredSize() {
		return new Dimension(1000, 600);
	}

	public void generateTopBlocks() {
		// method for generating terrain at the top
		for (int i = 0; i <= 20; i++) {

			randomIndex = generator.nextInt(9) + 1;
			while ((randomIndex - prevRandom >= 2) || (prevRandom - randomIndex >= 2)) {
				randomIndex = generator.nextInt(9) + 1;
			}
			for (int a = 0; a < randomIndex; a++) {
				final Block block = new Block(i * 50, a * 10);
				blocks.add(block);

			}
			prevRandom = randomIndex;
		}

	}

	public void generateNewTopBlock() {
		// method for generating terrain at the top after the initial generate
		randomIndex = generator.nextInt(9) + 1;
		while ((randomIndex - prevRandom >= 2) || (prevRandom - randomIndex >= 2)) {
			randomIndex = generator.nextInt(9) + 1;
		}
		for (int a = 0; a < randomIndex; a++) {
			final Block block = new Block(1000, a * 10);
			blocks.add(block);

		}
		prevRandom = randomIndex;

	}

	public void generateBotBlocks() {

		for (int i = 0; i <= 20; i++) {
			// method for generating terrain at the bot
			randomIndex = generator.nextInt(9) + 1;
			while ((randomIndex - prevRandom >= 2) || (prevRandom - randomIndex >= 2)) {
				randomIndex = generator.nextInt(9) + 1;
			}
			for (int a = 0; a < randomIndex; a++) {
				final Block block = new Block(i * 50, ((600 - a * 10) - 10));
				blocks.add(block);

			}
			prevRandom = randomIndex;
		}

	}

	public void generateNewBotBlock() {
		// method for generating terrain at the bot after the initial generate
		randomIndex = generator.nextInt(9) + 1;
		while ((randomIndex - prevRandom >= 2) || (prevRandom - randomIndex >= 2)) {
			randomIndex = generator.nextInt(9) + 1;
		}
		for (int a = 0; a < randomIndex; a++) {
			final Block block = new Block(1000, (600 - a * 10) - 10);
			blocks.add(block);

		}
		prevRandom = randomIndex;

	}

	public void generateWall() {
		// method for randomly generating walls
		yCord = generator.nextInt(200) + 100;
		final Wall bob = new Wall(1000, yCord);
		walls.add(bob);
	}

	public void generateStar() {
		// method for randomly generating stars
		yCord = generator.nextInt(400) + 100;
		final Star star = new Star(1000, yCord);
		stars.add(star);

	}

	public void generateLaser() {
		// method for randomly generating laser beams
		yCord = generator.nextInt(400) + 100;
		final Laser laser = new Laser(1000, yCord);
		lasers.add(laser);

	}

	public void moveBlocks(final ArrayList blocks) {
		// moves the blocks to the left.. so it seems like the heli is moving
		for (int i = 0; i < blocks.size(); i++) {
			((Block) blocks.get(i)).shiftX();
		}

	}

	public void movewalls(final ArrayList walls) {
		// same for the walls
		for (int i = 0; i < walls.size(); i++) {
			((Wall) walls.get(i)).shiftX();
		}
	}

	public void movestars(final ArrayList stars) {
		// same for the stars
		for (int i = 0; i < stars.size(); i++) {
			((Star) stars.get(i)).shiftX();
		}
	}

	public void moveLasers(final ArrayList lasers) {
		// same for the lasers
		for (int i = 0; i < lasers.size(); i++) {
			((Laser) lasers.get(i)).shiftX();
		}
	}

	public void checkCollision() {
		// checks collision with the terrain by assigning a rect to the helicopter and
		// terrain and checking if they intersect
		helicopterRect = new Rectangle(helicopter.getX(), helicopter.getY(), 144, 53);
		for (int i = 0; i < blocks.size(); i++) {

			if ((blocks.get(i).getX() >= 77) && (blocks.get(i).getX() <= 277)) {
				blockRect = new Rectangle(blocks.get(i).getX(), blocks.get(i).getY(), 50, 10);

				if (helicopterRect.intersects(blockRect) == true) {

					collision = true;
				}
			}
		}

	}

	public void checkWallCollision() {
		// checking collision with walls
		for (int a = 0; a < walls.size(); a++) {
			if (((walls.get(a)).getX() >= 77) && ((walls.get(a)).getX() <= 150))
				;
			wallRect = new Rectangle((walls.get(a)).getX(), (walls.get(a)).getY(), 50, 200);
			if (helicopterRect.intersects(wallRect) == true) {
				collision = true;
			}
		}
	}

	public void checkStarCollision() {
		// checks collision with stars
		for (int a = 0; a < stars.size(); a++) {
			if ((((Star) stars.get(a)).getX() >= 77) && (((Star) stars.get(a)).getX() <= 150))
				;
			starRect = new Rectangle(((Star) stars.get(a)).getX(), ((Star) stars.get(a)).getY(), 50, 50);
			if (helicopterRect.intersects(starRect) == true) {
				score = score + 100;
				stars.remove(a);
				System.out.println("Got a star");

			}
		}
	}

	public void checkLaserCollision() {
		// checks if the laser hit
		for (int a = 0; a < lasers.size(); a++) {
			if ((((Laser) lasers.get(a)).getX() >= 77) && (((Laser) lasers.get(a)).getX() <= 150))
				;
			laserRect = new Rectangle(((Laser) lasers.get(a)).getX(), ((Laser) lasers.get(a)).getY(), 50, 10);
			if (helicopterRect.intersects(laserRect) == true) {
				collision = true;

			}
		}
	}

	public void keyPressed(final KeyEvent e) {
		if (screen.equals("play")) {
			// if space is pressed it goes up
			System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
			if (KeyEvent.getKeyText(e.getKeyCode()) == "Space") {
				goingUp = true;
				System.out.println("going up!");
			}
		}
	}

	public void keyReleased(final KeyEvent e) {
		if (screen.equals("play")) {
			// when released it goes down
			System.out.println("Key released: " + KeyEvent.getKeyText(e.getKeyCode()));
			if (KeyEvent.getKeyText(e.getKeyCode()) == "Space") {
				goingUp = false;
				System.out.println("going down!");
				shiftValue = 3;
			}
		}
	}

	public void mousePressed(final MouseEvent e) {
		// same idea for mouse
		if (screen.equals("play")) {
			System.out.println("Key pressed: Mouse ");
			goingUp = true;
			System.out.println("going up!");
		}
	}

	public void mouseReleased(final MouseEvent e) {
		if (screen.equals("play")) {
			System.out.println("Key released: Mouse ");
			goingUp = false;
			System.out.println("going down!");
			shiftValue = 3;
		}
	}

	public void paintComponent(final Graphics g) {
		// drawing section

		if (screen.equals("title")) {
			g.drawImage(titleScreen, 0, 0, null);
		}
		// draws whats on the screen based on what 'screen' or phase of the program is
		// in
		if (screen.equals("instructions")) {
			g.drawImage(instructionsScreen, 0, 0, null);
		}
		if (screen.equals("play")) {

			g.drawImage(background, 0, 0, null);
			g.setColor(Color.black);
			if (collision == true) { // what happens when the user loses
				heliPic = new ImageIcon("images/explo.gif");
				// g.clearRect(0,0, 1000, 600);
				timer.stop();

				g.setColor(Color.white);
				g.drawImage(lose, 0, 0, null);

				g.setFont(new Font(null, 1, 28));
				// if the users score is higher than the current highscore it writes it
				if (drawHighScore == true) {
					g.drawString("NEW HIGH SCORE: " + score, 325, 450);
				} else {
					g.drawString("SCORE: " + score, 415, 450);
				}

				if (score > maxDistance) {
					// writes new high score if applicable

					drawHighScore = true;
					maxDistance = score;
					save();

				}

			}

			heliPic.paintIcon(this, g, helicopter.getX(), helicopter.getY());
			// draws the blocks and removes unused ones
			for (int i = 0; i < blocks.size(); i++) {
				if (blocks.get(i).getX() < (-200)) {
					blocks.remove(i);
				}
				g.drawImage(blockPicture, blocks.get(i).getX(), blocks.get(i).getY(), null);
			}
			if (collision == false) {
				// draws the walls and removes unused ones
				for (int a = 0; a < walls.size(); a++) {
					if ((walls.get(a)).getX() < (-200)) {
						walls.remove(a);
					}
					g.drawImage(wallPicture, (walls.get(a)).getX(), (walls.get(a)).getY(), null);

				}
				for (int a = 0; a < stars.size(); a++) {
					// draws the stars and then removes them

					g.drawImage(starPicture, ((Star) stars.get(a)).getX(), ((Star) stars.get(a)).getY(), null);
					if (((Star) stars.get(a)).getX() < (-200)) {
						stars.remove(a);
					}
				}
				for (int a = 0; a < lasers.size(); a++) {
					if (lasers.get(a).getX() >= 0) {
						g.drawImage(laserPicture, ((Laser) lasers.get(a)).getX(), ((Laser) lasers.get(a)).getY(), null);
					}
				}
				g.setColor(Color.white);
				g.setFont(new Font(null, 1, 14));
				g.drawString("Current Score: " + score, 800, 100);
				g.drawString("High Score: " + maxDistance, 800, 150);
			}
		}

	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		if (evt.getSource() == timer) { // if timer is firing do the following (should only fire per your set delay)
			moveBlocks(blocks);
			movewalls(walls);
			movestars(stars);
			moveLasers(lasers);
			// moves everything every timer tick
			// if the helicopter is supposed to be going up , it moves it a couple of pixels
			// each timer tick, likewise with going down
			if (goingUp == false) {
				helicopter.shiftYUp();
			} else if (goingUp == true) {
				helicopter.shiftYDown(shiftValue);
			}
			timerTick++;
			score++;
			if (timerTick % 5 == 0) {
				// makes new terrain and checks collision when needed
				generateNewTopBlock();
				generateNewBotBlock();
				checkCollision();
				checkWallCollision();
				checkStarCollision();
				checkLaserCollision();
			}
			// so what the next block of code does that instead of making going up linear,
			// the value of pixels which the helicopter goes up by every tick goes up the
			// longer you hold down the key, so its kinda more realistic i guess
			if (timerTick % 20 == 0) {
				if (goingUp == true) {
					if (shiftValue < 6) {
						shiftValue++;
					}
				}
			}
			// generates stars
			if ((timerTick + 50) % 100 == 0) {
				if (timerTick != 0) {
					generateStar();
				}

			}
			if (timerTick % 100 == 0) {
				// makes new walls as needed,
				generateWall();

			}
			// generates lasers, laserDelay decreases as the game progresses
			if (timerTick % laserDelay == 0) {
				generateLaser();
			}
			// increases difficulty after a while
			if (timerTick % 500 == 0) {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				laserDelay = laserDelay - 5;
			}

		}
	}

	@Override
	public void keyTyped(final KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	// file loading method
	public void load(final File file) {
		try {
			final Scanner reader = new Scanner(file);
			while (reader.hasNext()) {
				final int value = reader.nextInt();
				if (value > maxDistance)
					maxDistance = value;
			}
		} catch (final IOException i) {
			System.out.println("Error. " + i);
		}
	}

	// file saving method
	public void save() {
		FileWriter out;
		try {
			out = new FileWriter("best.txt");
			out.write("" + maxDistance);
			out.close();
		} catch (final IOException i) {
			System.out.println("Error: " + i.getMessage());
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		System.out.println("Click at" + e.getX() + " , " + e.getY());
		if (collision == true) {
			if ((e.getX() > 694) && (e.getX() < 898) && (e.getY() > 63) && (e.getY() < 204)) {
				reset();
				screen = "title";
				// play again button
			}
		}
		if (screen.equals("title")) {
			// start game method
			if ((e.getX() > 172) && (e.getX() < 421) && (e.getY() > 394) && (e.getY() < 492)) {
				System.out.println("STARTING");
				timer.start();
				try {
					background = ImageIO.read(new File("images/bg-dark.png"));
				} catch (final IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				screen = "play";
				repaint();
			}
			// just some buttons
			if ((e.getX() > 547) && (e.getX() < 847) && (e.getY() > 394) && (e.getY() < 492)) {
				screen = "instructions";
				System.out.println("going to instructions");
				repaint();
			}

		} else if (screen.equals("instructions")) {
			if ((e.getX() > 117) && (e.getX() < 422) && (e.getY() > 412) && (e.getY() < 506)) {
				screen = "title";
				repaint();
			} else if ((e.getX() > 665) && (e.getX() < 878) && (e.getY() > 417) && (e.getY() < 508)) {
				System.out.println("STARTING");
				timer.start();
				try {
					background = ImageIO.read(new File("images/bg-dark.png"));
				} catch (final IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				screen = "play";
				repaint();
			}
		}

	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void reset() {
		// reset just reinitializes everything
		blocks.clear();
		walls.clear();
		prevRandom = 3;
		timerTick = 0;
		goingUp = false;
		collision = false;
		drawHighScore = false;
		start = false;
		stars.clear();
		blocks.clear();
		walls.clear();
		lasers.clear();
		screen = "title";
		generateTopBlocks();
		generateBotBlocks();
		helicopter.reset();
		laserDelay = 200;
		score = 0;
		heliPic = new ImageIcon("images/helicopter.gif");
		try {
			background = ImageIO.read(new File("images/bg-dark.png"));
		} catch (final IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
