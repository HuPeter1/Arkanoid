import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;

// This class manages each Power Up (moving, activating, drawing)

public class PowerUp{
  private int x, y, width, height, type;
  public static final int EXTRABALLS = 1, BIGGERPADDLE = 2, ONEUP = 3; // types of power ups
  private static final Image EBImage, BPImage, OUImage;

  static {
    Image EB = null, BP = null, OU = null;
    try {
      EB = ImageIO.read(PowerUp.class.getResourceAsStream("Powers/EXTRABALLS.png"));
      BP = ImageIO.read(PowerUp.class.getResourceAsStream("Powers/BIGGERPADDLE.png"));
      OU = ImageIO.read(PowerUp.class.getResourceAsStream("Powers/ONEUP.png"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    EBImage = EB;
    BPImage = BP;
    OUImage = OU;
  }
  
  public PowerUp(int xx, int yy, int t){
    x = xx;
    y = yy;
    width = 40;
    height = 15;
    type = t;
  }
  
  public Rectangle getRect(){return new Rectangle(x, y, width, height);}
  
  public int getType(){return type;}
  
  public static void chance(ArrayList<PowerUp> powerUps, Brick b){ // method to randomly generate power ups
    Random rand = new Random();
    if (rand.nextInt(5) == 0){ // 20% chance to generate a power up
      powerUps.add(new PowerUp(b.getX() + b.getWidth() / 2 - 20, b.getY() + b.getHeight() / 2, rand.nextInt(3) + 1)); // adding a random power up to the ArrayList
    }
  }
  
  public static int move(ArrayList<PowerUp> powerUps, PowerUp powerUp, ArrayList<Ball> balls, Paddle p){ // moves a power up
    powerUp.y += 2;
    if (powerUp.getRect().intersects(p.getRect())){ // checking if it hits the paddle
      if (powerUp.getType() == PowerUp.EXTRABALLS){ // adds two balls
        balls.add(new Ball(p, 0));
        balls.add(new Ball(p, 0));
      }
      else if (powerUp.getType() == PowerUp.BIGGERPADDLE){ // makes the paddle bigger
        p.power();
      }
      powerUps.remove(powerUp); // removing from ArrayList
      return powerUp.getType(); // if the type was ONEUP it will return it and add a life in the panel
    }
    if (powerUp.y > 600){ // if it is below the screen
      powerUps.remove(powerUp); // removing from ArrayList
    }
    return 0; // nothing happened
  }
  
  public void draw(Graphics g){ // drawing the power up
    if (type == EXTRABALLS){ // checking type
      g.drawImage(EBImage, x, y, null);
    }
    else if (type == BIGGERPADDLE){
      g.drawImage(BPImage, x, y, null);
    }
    else if (type == ONEUP){
      g.drawImage(OUImage, x, y, null);
	}
    g.setColor(Color.BLACK);
    g.drawRect(x, y, width, height);
  }
}