import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;

public class Main extends Application
{
   FlowPane fp;
   Canvas theCanvas = new Canvas(600,600);
   AnimationTimer ta;
   boolean up,down,left,right;
   float xForce=0,yForce=0,mX=1,mY=1;
   Random rand = new Random();
   private List<Mine> mines = new ArrayList<>();
   float scoreX =0, scoreY=0;
   int score=0,highscore=0;
   int lastgridX=3,lastgridY=3;
   boolean alive = true;

   
   public Main()
   {
      try(Scanner read = new Scanner(new File("highscore.txt")))
      {
         if(read.hasNextInt())
         {
            highscore = read.nextInt();
         }
      }
      catch(FileNotFoundException fnfe)
      {
      }
   }
   public void start(Stage stage)
   {   
      fp = new FlowPane();
      fp.getChildren().add(theCanvas);
      fp.setOnKeyPressed(new KeyListenerDown());
      fp.setOnKeyReleased(new KeyListenerUp());
      
      gc = theCanvas.getGraphicsContext2D();
      
      drawBackground(300,300,gc);
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :(");
      fp.requestFocus();
      stage.show();
      
      ta = new AnimationHandler();
      ta.start();
   }
   
   GraphicsContext gc;
   ThePlayer thePlayer = new ThePlayer(300,300);
   Orgin orgin = new Orgin(300,300);
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	  //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	//figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }         
   
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);
         
         //USE THIS CALL ONCE YOU HAVE A PLAYER
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 
         
         if(alive)
         {
            //example calls of draw - this should be the player's call for draw
            thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter.
         }
         
         if(!up && !down)
         {
            if(yForce <.25 && yForce > -.25)
               yForce = 0;
            else if(yForce <= -.25)
               yForce += .025;
            else if(yForce >= -.25)
               yForce -= .025;
         }
            
         if(!left && !right)
         {
            if(xForce <.25 && xForce > -.25)
               xForce = 0;
            else if(xForce <= -.25)
               xForce += .025;
            else if(xForce >= -.25)
               xForce -= .025;
         }         
            
         if(up)
         {
            if(yForce < -5)
               yForce = -5;
            else
               yForce-=0.1;
         }
         if(down)
         {
            if(yForce > 5)
               yForce = 5;
            else
               yForce+=0.1;
         }
         if(left)
         {
            if(xForce < -5)
               xForce = -5;
            else
               xForce-=0.1;
         }
         if(right)
         {  
            if(xForce > 5)
               xForce = 5;
            else
               xForce+=0.1;
         }
                  
         score = (int)thePlayer.distance(orgin);
         
         if(score>highscore)
         {
            highscore=score;

            try
            {
               FileOutputStream fos = new FileOutputStream("highscore.txt", false); 
               PrintWriter pw = new PrintWriter(fos);
               pw.println(highscore);
               pw.println();
               pw.close();
            }
            catch(FileNotFoundException fnfe)
            {
            }
         }

         thePlayer.setY(thePlayer.getY()+yForce);
         thePlayer.setX(thePlayer.getX()+xForce);
         
         gc.setFill(Color.WHITE);
         gc.fillText("Score: "+score, 10,18);
         gc.fillText("Highscore: "+highscore, 10,30);
         
         boolean gridChanged = false;
          
         int cgridx = ((int) thePlayer.getX())/100;
         int cgridy= ((int) thePlayer.getY())/100;
         
         if(lastgridX != cgridx)
         {
            lastgridX = cgridx;
            gridChanged = true;
         }
         if(lastgridY != cgridy)
         {
            lastgridY = cgridy;
            gridChanged = true;
         }
         
         float pX=0,pY=0;

         if(gridChanged)
         {
            for(int i = 0; i < 9; i++)
            {
               pX = cgridx - 5 +i ;
               pY = cgridy - 5;
               
               createMines(pX,pY);
            }
            for(int i = 0; i < 9; i++)
            {
               pX = cgridx - 5 ;
               pY = cgridy - 5 + i;
               
               createMines(pX,pY);
            }        
            for(int i = 0; i < 9; i++)
            {
               pX = cgridx - 5 + i;
               pY = cgridy + 4;
               
               createMines(pX,pY);
            }
            for(int i = 0; i < 9; i++)
            {
               pX = cgridx + 4;
               pY = cgridy - 5 + i;
               
               createMines(pX,pY);
            }
         }
      
      if(!alive)
      {
         ta.stop();
      }
      
      for(int i = 0; i<mines.size();i++)
      {
         mines.get(i).draw(thePlayer.getX(),thePlayer.getY(),gc,false);
         if(thePlayer.distance(mines.get(i)) <= 20)
         {
            mines.remove(i);
            alive = false;
         }
         else if(thePlayer.distance(mines.get(i)) >= 800)
         {
            mines.remove(i);
         }
      }
   }
}
   
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         if (event.getCode() == KeyCode.W)
            up = true;
         if (event.getCode() == KeyCode.A) 
            left = true;
         if (event.getCode() == KeyCode.S) 
            down = true;
         if (event.getCode() == KeyCode.D)
            right = true;
      }
   }
   
   public class KeyListenerUp implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         if (event.getCode() == KeyCode.W) 
            up = false;
         if (event.getCode() == KeyCode.A) 
            left = false;
         if (event.getCode() == KeyCode.S) 
            down = false;
         if (event.getCode() == KeyCode.D)
            right = false;
      }
   }
   public void createMines(float pX, float pY)
   {
      int n = score/1000;
      
      while(n != 0)
      {
         int chance = rand.nextInt(100)+1;
         if(chance <= 30)
         {
            int randx = rand.nextInt(100);
            int randy = rand.nextInt(100);
            
            Mine m = new Mine((pX*100)+randx,(pY*100)+randy);
            mines.add(m);
         }
         n--;
      }
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}