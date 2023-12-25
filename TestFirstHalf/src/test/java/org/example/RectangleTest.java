package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    Rectangle rectangle;
    double startX=0, startY=1;
    @BeforeEach
    void setUp(){
        Point2D[] points = new Point2D[4];
        for (int i = 0; i < 4; i++){
            double[] x = new double[2];
            x[0] = startX; x[1] = startY;
            Point2D p = new Point2D(x);
            p=p.rot(2*Math.PI/4*i);
            points[i] = p;
        }
        rectangle = new Rectangle(points);
    }

    @Test
    void square() {
        assertEquals(Point.sub(rectangle.p[0], rectangle.p[1]).abs()*Point.sub(rectangle.p[1],rectangle.p[2]).abs(),
                rectangle.square());
    }

    @Test
    void testToString() {
        assertTrue(rectangle.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(rectangle.toBson() instanceof Document);
    }
}