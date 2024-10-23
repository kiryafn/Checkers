import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyWindow extends JFrame{
    BoardPanel board = new BoardPanel();

    public MyWindow(){
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        setVisible(true);
        setLayout(new BorderLayout());
        add(board, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                board.repaint();
            }
        });
    }
}
