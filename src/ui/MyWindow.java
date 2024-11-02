package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyWindow extends JFrame {
    public MyWindow(BoardPanel board) {

        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
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
        SidePanel westPanel  = new SidePanel(false, false);
        SidePanel eastPanel  = new SidePanel(false, false);

        board.addColorListener(northPanel);
        board.addColorListener(westPanel);
        board.addColorListener(eastPanel);
        board.addColorListener(southPanel);

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(eastPanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
    }
}
