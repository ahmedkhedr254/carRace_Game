
package carace;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.media.opengl.GLCanvas;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ahmed
 */
public class Frame extends JFrame {
    GLCanvas canvas ;
    public  Animator animator;
    public Frame (){
        this.setSize(1300,730);
        canvas =new GLCanvas();
        JLabel panel=new JLabel();
        panel.setBackground(Color.BLACK);
        panel.setOpaque(true);
        panel.setLayout(null);
        this.add(panel);
        panel.add(canvas);
        canvas.setBounds(350, 0, 550, 730);
          animator= new FPSAnimator(30);
          animator.add(canvas);
        canvas.addGLEventListener(new glEvent(this.canvas,this));
        canvas.addKeyListener(new glEvent(this.canvas,this));
        
                this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
                setVisible(true);
                canvas.requestFocus();
                
                
    }
    
}

