import javafx.scene.paint.*;
import javafx.scene.canvas.*;

//this is an example object
public class ThePlayer extends DrawableObject
{
	//takes in its position
   public ThePlayer(float x, float y)
   {
      super(x,y);
   }
   //draws itself at the passed in x and y.
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      gc.setFill(Color.BLACK);
      gc.fillOval(x-14,y-14,27,27);
      gc.setFill(Color.GRAY);
      gc.fillOval(x-13,y-13,25,25);
      gc.setFill(Color.GREEN);
      gc.fillOval(x-4,y-4,8,8);
   }
}
