import java.awt.*;
import java.util.*;

/*This class manages each Ball (moving, checking if it's under the bottom side, deleting, checking if there are any balls, checking if it hits a brick, drawing)*/

public class Ball{
  Random rand = new Random();
  private int x, y, vx, vy, delay;
  private boolean paddleBounce = false;
  
  public Ball(Paddle p, int d){
    x = p.getX() + p.getWidth() / 2 - 5;
    y = p.getY() - 10;
    vx = rand.nextInt(9) - 4;
    vy = rand.nextInt(2) - 4;
    delay = d;
  }
  
  public Rectangle getRect(){return new Rectangle(x, y, 10, 10);}
  
  public void move(ArrayList<Ball> balls, Paddle p){
	if (delay > 0){ //if there is a delay then the ball will follow the paddle without moving vertically
	  x = p.getX() + p.getWidth() / 2 - 5;
	  delay --; //reducing the delay
	}
	else{ //no delay
      x += vx;
      y += vy;
      
      if (paddleBounce == true){ //checking if the ball can bounce off the paddle
        if (getRect().intersects(p.getRect())){
          vx = (int)((p.getX() + p.getWidth() / 2 - x - 5) / (p.getWidth() / -10)); //equation to determine vx so that it's more when it's further away from centre of paddle
          if (vx == 0){ //if vx is 0
            vx = rand.nextInt(3) - 1; //-1, 0, 1
		      }
          vy *= -1;
          paddleBounce = false; //ball can't hit paddle anymore
        }
      }
    
      if (x < 0 || x > 790){ //left and right sides
        x = vx < 0 ? 0 : 790;
        vx *= -1;
        paddleBounce = true; //ball can hit paddle now
      }
      
      if (y < 0){ //top side
        y = 0;
        vy *= -1;
        paddleBounce = true; //ball can hit paddle now
	  }
    }
  }
  
  public boolean isUnder(){ //method to check if the ball is under the bottom side
    if (y > 600){
      return true;
    }
    return false;
  }
  
  public static void delete(ArrayList<Ball> balls, Ball b){ //method to remove the ball from the ArrayList
    balls.remove(b);
  }
  
  public static boolean isEmpty(ArrayList<Ball> balls, Paddle p){ //method to check if there are any balls
    if (balls.isEmpty()){
      balls.add(new Ball(p, 150)); //adds a new ball with a delay
      return true;
    }
    return false;
  }
  
  public boolean hit(Brick b){ //method to check if the ball hit a brick
    if (getRect().intersects(b.getRect())){
      if (x - vx >= b.getX() + b.getWidth() || x + 10 - vx <= b.getX()){ //checking the vertical sides
        vx *= -1;
      }
      if (y - vy >= b.getY() + b.getHeight() || y + 10 - vy <= b.getY()){ //checking the horizontal sides
        vy *= -1;
      }
      paddleBounce = true; //ball can hit paddle now
      return true;
    }
    return false;
  }
  
  public void draw(Graphics g){ //drawing the ball
    g.setColor(Color.WHITE);
    g.fillOval(x, y, 10, 10);
  }
}