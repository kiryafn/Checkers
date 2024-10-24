import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyFont{
    Font font;
    MyFont(String name){
        super();
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(name)).deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
