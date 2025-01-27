/* -----------------Snake----------------------*/

//IMPORTS
import java.awt.EventQueue;
import javax.swing.JFrame;


public class snake extends JFrame {
  
    public static void main(String[] args) {
      boolean inGame = true;
      
        EventQueue.invokeLater(() -> {
            JFrame ex = new snake();
            ex.setVisible(true);
        });
    }
    
    public snake() {
        go();
    }
    
    private void go() {
        Board b = new Board();
        add(b);
        pack();
        setTitle("Señor Snek");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
   
}


