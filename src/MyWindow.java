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

        SidePanel northPanel = new SidePanel(true, true);
        SidePanel southPanel = new SidePanel(true, true);
        SidePanel eastPanel = new SidePanel(false, false);
        SidePanel westPanel = new SidePanel(false, false);
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(eastPanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);

    }
}
