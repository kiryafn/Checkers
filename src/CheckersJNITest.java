import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersJNITest {
    static {
        System.load("/Users/alieksieiev/CLionProjects/Checkers/cmake-build-debug/libCheckers.dylib");
    }

    private CheckersJNI board;

    @BeforeEach
    public void setUp() {
        board = new CheckersJNI();
    }

    @Test
    void mapInitializationTest() {
        int size = board.getBoardSize();
        assertEquals(8, size);

        for (int x=0; x<size; x++) {
            for (int y=0; y<size; y++) {
                if (x<3 && (x+y)%2 == 1) {
                    assertEquals(1, board.getBoardValue(x,y));
                }
                else if (x>4 && (x+y)%2 == 1) {
                    assertEquals(2, board.getBoardValue(x,y));
                }
                else assertEquals(0, board.getBoardValue(x,y));
            }
        }

    }
}