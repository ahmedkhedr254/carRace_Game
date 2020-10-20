package carace;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class glEvent implements GLEventListener, KeyListener {
    instart in;
    
    int level=700;
    static GLCanvas canvas;
    static int count = 0;
static double EnemyMaxSpeed = 22;
    double y1 = 0;
    double meX = 150;
    double meY = 40;
     double meOldX = 150;
    double meOldY = 40;
    static boolean play = true;
    
    static boolean stop = false;
    static boolean work = false;
        double countForMove=0;
   static double enemyX = 460;
  static  double enemyY = 40;

    static boolean onexp = false;
    
    static boolean onexp1 = false;
    
    static boolean turnLeft = false;
    static boolean turnRight = true;
     static int Common[] = new int[100];
     static boolean turnLeftCommon[] = new boolean[100];
    static boolean turnRightCommon[] = new boolean[100] ;
    double one = 16;
    double two = 20;
    double three = 200;

    double speed = 0;
    double enemySpeed = 0;
    double line1X = 150;
    double line1Y = 500;

    double line2X = 150;
    double line2Y = 250;

    double line3X = 150;
    double line3Y = 50;

    double line4X = 150;
    double line4Y = 700;
 double finishY = 20000;
   
    
   static double commonX[] = new double[100];
   static double commonY[] = new double[100];
    
 
    static int car = 1;

    static int drift = 0;
    public static Frame f;
    String folderName = "carace";
    double myspeed;
    
    String[] textureName = {"background.png", "me.png", "meLeft.png", "meRight.png", "line.png", "enemy.png", "common1.png","common2.png","common3.png","finishLine.png"};
    int textureIndex[] = new int[10];
    TextureReader.Texture[] textures = new TextureReader.Texture[textureName.length];

    public glEvent(GLCanvas c, Frame fa) {
        this.canvas = c;
        this.f = fa;
    }

    @Override
    public void init(GLAutoDrawable glad) {
         boolean c=true;
        double v=700;
       for (int i=0;i<100;i++){
           turnLeftCommon[i]=true;
            turnRightCommon[i]=false;
            Random rand=new Random();
            int h=rand.nextInt(3)+6;
            Common[i]=h;
           if (c){
           commonX[i]=160;
           c=false;
           }
           else{
           commonX[i]=320;
            c=true;
           }
           commonY[i]=v;
           v=v+200;
          
       }
        GL gl = glad.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glOrtho(0, 550, 0, 730, -1, 1);
        gl.glViewport(0, 550, 0, 730);

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(10, textureIndex, 0);

        for (int i = 0; i < textureName.length; i++) {
            try {
                textures[i] = TextureReader.readTexture(folderName + "//" + textureName[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        textures[i].getWidth(), textures[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        textures[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();

            }
        }

    }

    @Override
    public void display(GLAutoDrawable glad) {
        if (meY+80>finishY+100){
        f.animator.stop();
            JOptionPane.showMessageDialog(canvas, "hurray You Won!");
            System.exit(0);
              ///ew instart().setVisible(true);
        }
        if (enemyY+80>finishY+100){
        EnemyMaxSpeed=0;
        enemySpeed=0;
        f.animator.stop();
        JOptionPane.showMessageDialog(canvas, "Good Luck Next Time");
        System.exit(0);
            //new instart().setVisible(true);
        }
        GL gl = glad.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        enemyWorking();
        explotion();
        stopping();
        backMeToNormalSpeed();
        working();
        handleKeyPress();

        //***********************************************lines
        drawLine(gl);
        //------------------------------------------------------------------

//***************************************************************************************cars
  drawFinish(finishY, gl);
        finishY-=speed;
        meSimulation();
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[car]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(meX, 40, 0);
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(30, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(30, 80, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 80, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

        //*****carenemy
        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[5]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(enemyX, enemyY, 0);
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(50, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(50, 80, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 80, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

        
        
        //***************************lineFinish
      
      
        
        
        
        
        //--------------------------------------------------------------------
        //***********************************************************************common
        //***********************************************************************
        //*******************************************************************
      for (int i=0;i<100;i++){
           drawcommon(commonX[i], commonY[i],Common[i], gl);
         commonY[i]=commonY[i]+2;
      }
      
        enemyY = enemyY + enemySpeed;
       
        commonMove();

        meGoAwayCommon();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    public static BitSet keyBits = new BitSet(4);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);

    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        
        car = 1;
        stop = true;
        work = false;
keyBits.clear(keyCode);
        System.out.print("aaa");

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void handleKeyPress() {

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if ( meX -5<370&& meX-5>160){
                meOldX=meX;
                System.out.println("yes left work");
            meX -= 8;
            }
            car = 2;
            speed -= .1;

        }
        
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
              if ( meX +5<370&& meX +5>130){
              meOldX=meX;
             meX += 8;
            }
           
            car = 3;
            speed -= .1;

        }
        
        if (isKeyPressed(KeyEvent.VK_DOWN)) {

        }
        if (isKeyPressed(KeyEvent.VK_UP)) {

            work = true;
            stop = false;
            linesMove();

        }

        if (isKeyPressed(KeyEvent.VK_W)) {
            if (speed<30){
            speed += .6;
            }

        }
    }

    public static boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);

    }

    public static double tired() {
        return ((Math.random() * (2.0 - (0.0))) + 0.0);
    }

    public void stopping() {
        if (stop && speed > 0) {
            speed -= 1;
            System.out.print("b");
            linesMove();
        }

    }

    public void working() {
        if (work && speed < 13) {
            
            speed += .9;
        }
    }

    public void enemyWorking() {
      
        
        //*********************simulation of car 
        double v = ((Math.random() * (.0005 - (-.0005))) + (-.0005));
       
       
        
        //go up speed for the top speed
        if (enemySpeed < EnemyMaxSpeed) {
            enemySpeed += .1;

        }
    
        enemyY -= speed;
    }

    public void backMeToNormalSpeed() {
        if (speed > 13) {
            speed -= .1;
        }
    }

    public void linesMove() {
        line1Y -= speed;
        if (line1Y < -150) {

            line1Y = 700;
        }

        line2Y -= speed;
        if (line2Y < -150) {

            line2Y = 700;
        }

        line3Y -= speed;
        if (line3Y < -150) {

            line3Y = 700;
        }
        line4Y -= speed;
        if (line4Y < -150) {

            line4Y = 700;
        }
    }

    public  void explotion() {
        if (onexp) {
           speed=-5;

            onexp = false;
        }
         if (onexp1) {
           speed=-5;

            onexp1 = false;
        }
    }

    public void meSimulation() {
        meX = meX + ((Math.random() * (.25 - (-.25))) + (-.25));
    }

    public void commonMove() {
        for(int i=0;i<100;i++){
        commonY[i] -= speed;
        }
        
        for (int i=0;i<100;i++){
         if (distance(commonX[i], 150)<10){
              turnLeftCommon[i]=false;
              turnRightCommon[i]=true;
       } 
       
         if (distance(commonX[i], 350)<10){
           turnRightCommon[i]=false;
           turnLeftCommon[i]=true;
       } 
         
         if (turnLeftCommon[i]){
             commonX[i]=commonX[i]-1;
         }
         if (turnRightCommon[i]){
             commonX[i]=commonX[i]+1;
            
         }
       
        }
         
       
    }
    
    

    public void meGoAwayCommon() {
        for(int i=0;i<100;i++){
      /*  if (commonY[i] - (meY + 75) < 1&&commonY[i] - (meY + 50) > 0 && Math.abs(meX - commonX[i]) < 39) {
            onexp = true;
        }*/
         if ((meY<commonY[i]+150&&meY>commonY[i] || meY+80<commonY[i]+150&&meY+80>commonY[i])&&((meX+30)<commonX[i]+50&&meX + 30>commonX[i] ||(meX)<commonX[i]+50&&meX >commonX[i] )) {
           onexp = true;
           meX+= 1.5 * (meOldX - meX);
            
           
        }
        }

    }
    
    
    public  static double distance(double x,double y){
    return Math.abs(x-y);
    }
    public void drawLine(GL gl ){
    gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[0]);	// Turn Blending On

        gl.glBegin(GL.GL_QUADS);
        // Front Face

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(550, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(550, 730f, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 730, 0f);
        gl.glEnd();

        gl.glDisable(GL.GL_BLEND);

        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[4]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(line1X, line1Y, 0);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(250, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(250, 150, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 150, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[4]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(line2X, line2Y, 0);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(250, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(250, 150, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 150, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[4]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(line3X, line3Y, 0);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(250, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(250, 150, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 150, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[4]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(line4X, line4Y, 0);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(250, 0f, 0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(250, 150, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 150, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    
    public void drawcommon(double x,double y,int c,GL gl){
        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[c]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
         
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(50, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(50, 150, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 150, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
        
        
        
         

    }
    public void drawFinish(double y,GL gl){
    
      gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex[9]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(0, y, 0);
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 0f, 0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(700, 0f, 0f);
        //*************************************
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(700, 50, 0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 50, 0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

    }
   
}
