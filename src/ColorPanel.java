import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ColorPanel extends JPanel implements ColorListener {

    public ColorPanel(){
        this.setBackground(Color.BLACK);
        setOpaque(true);
        setPreferredSize(new Dimension(20, 20));
    }

    @Override
    public void colorChanged(ColorChangedEvent event) {
       // this.setBackground(event.getColor());
    }
}
