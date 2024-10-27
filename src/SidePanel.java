import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel{
    private final char[] chars = {'A','B','C','D','E','F','G','H'};
    private final int[] ints = {8,7,6,5,4,3,2,1};

    public SidePanel(boolean horizontal, boolean text){
        setOpaque(true);
        setPreferredSize(new Dimension(20, 20));

        if (horizontal)
            setLayout(new GridLayout(1, 8));
        else
            setLayout(new GridLayout(8, 1));

        for (int x = 0; x < 8; x++) {
            JLabel label = new JLabel(text?String.valueOf(chars[x]):String.valueOf(ints[x]));
            label.setFont(new MyFont("ARCADECLASSIC.TTF").font);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBackground(new Color(51, 23, 14));
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            add(label);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(51, 23, 14));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}