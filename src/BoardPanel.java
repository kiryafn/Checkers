import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class BoardPanel extends JPanel{

    private static final int BOARD_SIZE = 8;
    private int[][] boardState = new int[BOARD_SIZE][BOARD_SIZE];
    private JLabel[][] labelsArray = new JLabel[BOARD_SIZE][BOARD_SIZE];

    public BoardPanel() {
        setVisible(true);
        setFocusable(true);
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        paint();
    }

    public void paint(){
        for(int x=0; x<BOARD_SIZE; x++){
            for(int y=0; y<BOARD_SIZE; y++){
                JLabel l = new JLabel();
                l.setOpaque(true);
                l.setVerticalAlignment(SwingConstants.CENTER);
                l.setHorizontalAlignment(SwingConstants.CENTER);

                if((x+y)%2==0)
                    l.setBackground(new Color(236, 219, 185));
                else
                    l.setBackground(new Color(174,137,104));

                add(l);
                labelsArray[x][y] = l;
            }
        }
    }

    public native int buba();
}