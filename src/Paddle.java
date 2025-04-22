import java.awt.*;

// This class manages the Paddle (moving, the activating BIGGERPADDLE power up, drawing)

public class Paddle{
  private int x, y, width, height, left, right, powerTime;
  
  public Paddle(int l, int r){
    x = 350;
    y = 540;
    width = 100;
    height = 15;
    left = l;
    right = r;
    powerTime = 0;
  }
  
  public Rectangle getRect(){return new Rectangle(x, y, width, height);}
  
  public int getX(){return x;}
  
  public int getY(){return y;}
  
  public int getWidth(){return width;}
  
  public int getHeight(){return height;}
  
  public void move(boolean []keys){
    if (powerTime > -1){ // if the BIGGERPADDLE power up is active it will decrease the time (goes to -1 so that the width isn't set to 100 each time)
      powerTime --;
    }
    if (powerTime == 0){ // BIGGERPADDLE power up ran out so width goes back to 100
      width = 100;
			x += 25;
    }
    if (keys[left]){ // left arrow pressed
      x = Math.max(0, x - 5);
    }
    if (keys[right]){ // right arrow pressed
      x = Math.min(800 - width, x + 5);
    }
  }
  
  public void power(){ // method that activates the power up
		if (width != 150){ // checking if width isn't already 150
      width = 150; // changing the width
		  x -= 25; // centering the paddle
		}
	  if (x < 0){ // making sure the paddle isn't off the screen when expanded
			x = 0;
		}
		else if (x > 650){ // making sure the paddle isn't off the screen when expanded
			x = 650;
		}
		if (powerTime == -1){ // adding time
      powerTime += 501;
		}
		else{ // adding time
			powerTime += 500;
		}
  }
  
  public void draw(Graphics g){ // drawing the paddle
    g.setColor(Color.RED);
    g.fillRect(x, y, (int)(width * 0.15), 15);
    g.fillRect(x + (int)(width * 0.85), y, (int)(width * 0.15), 15);
    g.setColor(Color.GRAY);
    g.fillRect(x + (int)(width * 0.15), y, (int)(width * 0.70), 15);
  }
}