import java.awt.*;
import java.util.EventObject;

public
class ColorChangedEvent
        extends EventObject {

    private Color color;

    public ColorChangedEvent(Component component, Color color) {
        super(component);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
