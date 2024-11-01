import java.awt.*;
import java.util.EventObject;

public
class ColorChangedEvent
        extends EventObject {

    private Color colorFore;
    private Color colorBack;

    public ColorChangedEvent(Component component, Color color1, Color color2) {
        super(component);
        this.colorFore = color1;
        this.colorBack = color2;
    }

    public Color getColorBack() {
        return colorBack;
    }

    public Color getColorFore() {
        return colorFore;
    }
}
