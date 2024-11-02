package ui;

import data.ColorChangedEvent;
import data.ColorListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SidePanel extends JPanel implements ColorListener {
    private final char[] chars = {'A','B','C','D','E','F','G','H'};
    private final int[] ints = {8,7,6,5,4,3,2,1};
    ArrayList<JLabel> labels = new ArrayList<>();
    Color background = Color.WHITE;

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
            labels.add(label);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void colorChanged(ColorChangedEvent e) {
        for(JLabel label : labels){
            label.setForeground(e.getColorFore());
            label.setBackground(e.getColorBack());
        }
        background = e.getColorBack();

        repaint();
    }
}