import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

// This class manages the game itself (screen, making levels, lives, moving, actions, deciding what to draw)

class ArkanoidPanel extends JPanel implements KeyListener, ActionListener{
  private boolean []keys; // keyboard keys
  private Paddle p;
  private ArrayList<Ball> balls;
  private ArrayList<Integer> removeBalls; // ArrayList to tell which balls should be removed
  private Timer timer;
  private ArrayList<Brick> bricks;
  private ArrayList<PowerUp> powerUps;
  public static final int GAMEOVER = -1, INTRO = 0, LEVEL1 = 1, CONTINUE1 = 2, LASTLEVEL = 3, VICTORY = 4; // all the different screens
  private int screen, lives, score, highScore, flash; // integers for simple game things (flash is for text flashing)
  private boolean pause; // variable to tell when the game is paused
  private static final Image title, back;

  static {
    Image tImg = null, bGif = null;
    try {
      tImg = ImageIO.read(ArkanoidPanel.class.getResourceAsStream("title.png"));
	    bGif = ImageIO.read(ArkanoidPanel.class.getResourceAsStream("back.gif"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    title = tImg;
    back = bGif;
  }
  
  public ArkanoidPanel(){
    keys = new boolean[KeyEvent.KEY_LAST + 1];
    setPreferredSize(new Dimension(800, 600));
    
    screen = INTRO;
	  highScore = 0;
    
    setFocusable(true);
    requestFocus();
    addKeyListener(this);
    timer = new Timer(10, this);
    timer.start();
  }
  
  public void start(){ // method for starting each level
    pause = false;
    p = new Paddle(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
    balls = new ArrayList<Ball>();
    balls.add(new Ball(p, 200));
    removeBalls = new ArrayList<Integer>();
    bricks = new ArrayList<Brick>();
    powerUps = new ArrayList<PowerUp>();
    Brick.make(screen, bricks);
    if (screen == LEVEL1){ // checking if this start is for LEVEL1
      lives = 3;
	    score = 0;
    }
  }
  
  public void addLife(){ // method to add a life
    lives ++;
  }
  
  public void removeLife(){ // method to remove a life
    lives --;
  }
  
  public void move(){ // method to mvoe everything
    p.move(keys);
    for (int i = 0; i < balls.size(); i ++){ // going through each ball
      balls.get(i).move(balls, p);
      if (balls.get(i).isUnder()){ // checking if the ball is under the bottom side of the screen
        removeBalls.add(i); // adding the index to removeBalls
      }
      for (int j = 0; j < bricks.size(); j ++){ // going through each brick
        if (balls.get(i).hit(bricks.get(j))){ // checking if any of the balls are hitting any of the bricks
		      score += 10; // adding ten to the score
          if (bricks.get(j).getHealth() == 1){ // checking if the health of the brick was one before taking damage
            PowerUp.chance(powerUps, bricks.get(j)); // randomly generating a power up
          }
          Brick.damage(bricks, bricks.get(j)); // damaging the brick and possibly removing it
          break; // breaking the loop so that none of the balls hit two bricks at the same time
        }
      }
    }
    if (removeBalls.isEmpty() == false){ // checking if there are balls that need to be removed (doing this outside of the normal loop for balls to prevent crashes)
      for (int i = removeBalls.size(); i > 0; i --){ // i starts at the end to prevent crashes
        Ball.delete(balls, balls.get(removeBalls.get(i - 1))); // removing balls starting from the last index of removeBalls
        if (Ball.isEmpty(balls, p)){ // checking if there aren't any balls
          removeLife();
        }
      }
      removeBalls.clear(); // clearing the removeBalls ArrayList
    }
    for (int i = 0; i < powerUps.size(); i ++){ // going through each power up
      if (PowerUp.move(powerUps, powerUps.get(i), balls, p) == PowerUp.ONEUP){ // moving each power up and also checking if a ONEUP power up was hit
        addLife();
      }
    }
  }
  
  @Override
  public void actionPerformed(ActionEvent e){
    if (keys[KeyEvent.VK_SPACE]){
      if (pause){ // if paused
        pause = false; // unpausing
      }
      else if (screen == INTRO || screen == CONTINUE1){ // starts the next level
        screen ++;
        start();
      }
    }
    if (keys[KeyEvent.VK_ENTER]){
      if (screen == LEVEL1 || screen == LASTLEVEL){ // checking if the screen is on a level
        pause = true; // pausing
      }
    }
    if (keys[KeyEvent.VK_ESCAPE]){
      if ((screen != LEVEL1 && screen != LASTLEVEL) || pause == true){ // checking if the screen isn't on a level or if it's paused
        pause = false; // unpausing
		    if (screen == CONTINUE1 || screen == VICTORY){ // checking if the screen is on CONTINUE1 or VICTORY
		      if (score > highScore){ // checking if the score is higher than the high score
		        highScore = score; // replacing the high score
		      }
		    }
      screen = INTRO; // going back to main menu
      }
    }
    if ((screen == LEVEL1 || screen == LASTLEVEL) && pause == false){ // checking if the screen is on a level and it's not paused
      move();
      if (bricks.isEmpty()){ // checking if there are no more bricks
        screen ++; // going to the next screen
      }
      if (lives == 0){ // if there are no more lives left
		    if (score > highScore){ // checking if the score is higher than the high score
		      highScore = score; // replacing the high score
		    }
        screen = GAMEOVER; // going to the game over screen
      }
    }
    repaint();
  }
  
  @Override
  public void keyReleased(KeyEvent ke){
    int key = ke.getKeyCode();
    keys[key] = false;
  }
  
  @Override
  public void keyPressed(KeyEvent ke){
    int key = ke.getKeyCode();
    keys[key] = true;
  }
  
  @Override
  public void keyTyped(KeyEvent ke){}
  
  @Override
  public void paint(Graphics g){ // drawing everything
		flash ++; // increasing flash to make text appear and disappear
    if (screen == GAMEOVER){ // checking each screen
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 800, 600);
      g.setColor(Color.GRAY);
      g.setFont(new Font("Comic sans MS", Font.BOLD, 40));
      g.drawString("GAME OVER", 275, 40);
			if (flash % 150 < 75){
        g.drawString("Press ESCAPE to Return to Menu", 30, 500);
			}
    }
    else if (screen == INTRO){
      g.drawImage(back, 0, 0, 800, 600, null);
      g.setColor(Color.GRAY);
			if (flash % 150 < 75){
        g.setFont(new Font("Comic sans MS", Font.BOLD, 40));
        g.drawString("Press SPACE to Start", 175, 500);
			}
	    g.setFont(new Font("Comic sans MS", Font.BOLD, 15));
	    g.drawString("High Score: " + highScore, 350, 15);
	    g.drawImage(title, 81, 150, null);
    }
    else if (screen == LEVEL1 || screen == LASTLEVEL){
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 800, 600);
      for (int i = 0; i < balls.size(); i ++){
        balls.get(i).draw(g);
      }
      p.draw(g);
      for (int i = 0; i < bricks.size(); i ++){
        bricks.get(i).draw(g);
      }
      for (int i = 0; i < powerUps.size(); i ++){
        powerUps.get(i).draw(g);
      }
      g.setColor(Color.WHITE);
      g.setFont(new Font("Comic sans MS", Font.BOLD, 15));
			if (flash % 150 < 75){
        g.drawString("Press ENTER to Pause", 0, 15);
			}
	    g.drawString("Score: " + score, 400, 15);
      g.drawString("Lives: " + lives, 730, 15);
    }
	  else if (screen == VICTORY){
      g.drawImage(back, 0, 0, 800, 600, null);
      g.setColor(Color.GRAY);
      g.setFont(new Font("Comic sans MS", Font.BOLD, 40));
      g.drawString("VICTORY", 275, 100);
			if (flash % 150 < 75){
        g.drawString("Press ESCAPE to Return to Menu", 30, 500);
			}
    }
    else{
      g.drawImage(back, 0, 0, 800, 600, null);
      g.setColor(Color.GRAY);
      g.setFont(new Font("Comic sans MS", Font.BOLD, 40));
      g.drawString("You Beat Level 1!", 210, 100);
			if (flash % 150 < 75){
        g.drawString("Press SPACE to Continue", 130, 500);
        g.setFont(new Font("Comic sans MS", Font.BOLD, 15));
        g.drawString("Press ESCAPE to Return to Menu", 0, 15);
			}
    }
    if (pause){ // paused
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 200, 25); // covering the "Press ENTER to Pause" message
      for (int i = 0; i < balls.size(); i ++){
        balls.get(i).draw(g); // drawing each ball again
      }
      g.setColor(Color.WHITE);
      g.setFont(new Font("Comic sans MS", Font.BOLD, 50));
      g.drawString("Paused", 325, 355);
      g.setColor(Color.GRAY);
			if (flash % 150 < 75){
        g.setFont(new Font("Comic sans MS", Font.BOLD, 40));
        g.drawString("Press ESCAPE to Quit", 200, 200);
        g.drawString("Press SPACE to Continue", 180, 500);
			}
    }
  }
}