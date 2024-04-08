import javafx.scene.paint.*;
import javafx.scene.canvas.*;

//this is an example object
public class Mine extends DrawableObject
{
   double colorValue=Math.random();
   int way =1;
   int randomColor = (int)(Math.random()*2)+1;
	//takes in its position
   public Mine(float x, float y)
   {
      super(x,y);
   }
   //draws itself at the passed in x and y.
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      advanceColor();
      gc.setFill(Color.WHITE.interpolate(Color.RED,colorValue));
      gc.fillOval(x-14,y-14,12,12);
   }
   
   public void advanceColor()
   {
      colorValue += 0.1f * way;
      
      if(colorValue > 1)
      {
         colorValue = 1;
         way = - 1;
      }
      if(colorValue < 0)
      {
         colorValue = 0;
         way = 1;
      }
   }
}
