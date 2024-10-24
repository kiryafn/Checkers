import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyWindow extends JFrame{
    public MyWindow(BoardPanel board){
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        setVisible(true);
        setLayout(new BorderLayout());
        add(board, BorderLayout.CENTER);

            SidePanel northPanel = new SidePanel(true, true);
            SidePanel southPanel = new SidePanel();
            SidePanel eastPanel = new SidePanel();
            SidePanel westPanel = new SidePanel(false, false);

            add(northPanel, BorderLayout.NORTH);
            add(southPanel, BorderLayout.SOUTH);
            add(eastPanel, BorderLayout.EAST);
            add(westPanel, BorderLayout.WEST);

    }
}