import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Main class

class Arkanoid extends JFrame{
  ArkanoidPanel game = new ArkanoidPanel(); // panel
  
  public Arkanoid(){ // managing the window
    super("Arkanoid");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(game);
    pack();
    setVisible(true);
  }
  
  public static void main(String []a){
    Arkanoid frame = new Arkanoid();
  }
}