
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TriangleClassificationTest {

    @Test
    public void testEquilateralTriangle() {
        assertEquals("Equilateral", TriangleClassification.classify(2, 2, 2));
    }

    @Test
    public void testIsoscelesTriangle() {
        assertEquals("Isosceles", TriangleClassification.classify(2, 2, 3));
    }

    @Test
    public void testScaleneTriangle() {
        assertEquals("Scalene", TriangleClassification.classify(3, 4, 5));
    }
}
