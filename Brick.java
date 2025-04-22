import java.awt.*;
import java.util.*;

// This class manages each Brick (making, damaging, drawing)

public class Brick{
  private int x, y, height, width, health;
  
  public Brick(int xx, int yy, int w, int h, int hp){
    x = xx;
    y = yy;
    width = w;
    height = h;
    health = hp;
  }
  
  public Rectangle getRect(){return new Rectangle(x, y, width, height);}
  
  public int getX(){return x;}
  
  public int getY(){return y;}
  
  public int getWidth(){return width;}
  
  public int getHeight(){return height;}
  
  public int getHealth(){return health;}
  
  public static void make(int level, ArrayList<Brick> bricks){ // methods that makes each brick and puts them into the ArrayList
    if (level == ArkanoidPanel.LEVEL1){ // checking if level is LEVEL1
      for (int i = 0; i < 3; i ++){
        for (int j = 0; j < 10; j ++){
          bricks.add(new Brick(j * 80, i * 30 + 100, 80, 30, Math.abs(i - 3)));
        }
      }
    }
    else{ // bricks for LASTLEVEL
      for (int i = 0; i < 2; i ++){
        for (int j = 0; j < 10; j ++){
          bricks.add(new Brick(j * 80, i * 30 + 80, 80, 30, Math.abs(i - 3)));
        }
      }
      for (int j = 0; j < 10; j ++){
        bricks.add(new Brick(j * 80, 190, 80, 30, 1));
      }
      for (int i = 0; i < 2; i ++){
        for (int j = 0; j < 10; j ++){
          bricks.add(new Brick(j * 80, i * -30 + 300, 80, 30, Math.abs(i - 3)));
        }
      }
    }
  }
  
  public static void damage(ArrayList<Brick> bricks, Brick b){ // method for taking health away
    b.health --;
    if (b.health == 0){ // if health reaches zero it removes it from the ArrayList
      bricks.remove(b);
    }
  }
  
  public void draw(Graphics g){ // drawing the brick
    if (health == 1){ // checking the health
      g.setColor(Color.GREEN);
    }
    else if (health == 2){
      g.setColor(Color.PINK);
    }
    else if (health == 3){
      g.setColor(Color.BLUE);
    }
    g.fillRect(x, y, width, height);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, width, height);
  }
}