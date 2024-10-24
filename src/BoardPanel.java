import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class BoardPanel extends JPanel{

    private static final int BOARD_SIZE = 8;
    private static final int DEFAULT_TILE_SIZE = 100;

    public BoardPanel() {
        this.setFocusable(true);
        //this.setPreferredSize(new Dimension(BOARD_SIZE * DEFAULT_TILE_SIZE, BOARD_SIZE * DEFAULT_TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int squareSizeX = getWidth() / 8;
        int squareSizeY = getHeight() / 8;


        for(int i=0, x=0; i<BOARD_SIZE; i++, x+=squareSizeX){
            for(int j=0, y=0; j<BOARD_SIZE; j++, y+=squareSizeY){
                if((i+j)%2==0) {
                    g.setColor(new Color(236, 219, 185));
                } else {
                    g.setColor(new Color(174,137,104));
                }
                g.fillRect(x, y, squareSizeX, squareSizeY);
            }
        }
    }
}