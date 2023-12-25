package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CircleTest {
    Circle circle;
    int centerX=1, centerY=1;
    int radius=1;

    @BeforeEach
    void setUp(){
        double[] x = new double[2]; x[0]=centerX; x[1]=centerY;
        circle = new Circle(new Point2D(x), radius);
    }

    @Test
    void getP() {
        assertAll("Center",
                () -> assertEquals(centerX, circle.getP().x[0]),
                () -> assertEquals(centerY, circle.getP().x[1])
                );
    }

    @Test
    void getR() {
        assertEquals(radius, circle.getR());
    }

    @Test
    void setP() {
        double[] x = new double[2];x[0]=centerX+radius; x[1]=centerY+radius;
        circle.setP(new Point2D(x));
        assertAll("Center Changed",
                () -> assertEquals(centerX+radius, circle.getP().x[0]),
                () -> assertEquals(centerY+radius, circle.getP().x[1])
        );
    }

    @Test
    void setR() {
        circle.setR(radius+radius);
        assertEquals(radius+radius, circle.getR());
    }

    @Test
    void square() {
        assertEquals(Math.PI*radius*radius, circle.square());
    }

    @Test
    void length() {
        assertEquals(2*Math.PI*radius, circle.length());
    }

    @Test
    void shift() {
        double[] shift = new double[2]; shift[0]=1;shift[1]=1;
        circle.shift(new Point2D(shift));
        assertAll("Shifted",
                () -> assertEquals(centerX+radius, circle.getP().x[0]),
                () -> assertEquals(centerY+radius, circle.getP().x[1])
        );
    }

    @Test
    void rot() {
        circle.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(-centerX, Math.round(circle.getP().x[0]*100.0)/100.0),
                () -> assertEquals(centerY, Math.round(circle.getP().x[1]*100.0)/100.0)
        );
        circle.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(-centerX, Math.round(circle.getP().x[0]*100.0)/100.0),
                () -> assertEquals(-centerY, Math.round(circle.getP().x[1]*100.0)/100.0)
        );
        circle.rot(Math.PI/2);
        assertAll("Rotated",
                () -> assertEquals(centerX, Math.round(circle.getP().x[0]*100.0)/100.0),
                () -> assertEquals(-centerY, Math.round(circle.getP().x[1]*100.0)/100.0)
        );
    }

    @Test
    void symAxis() {
        circle.symAxis(0);
        assertEquals(-centerY, circle.getP().x[1]);
        circle.symAxis(1);
        assertEquals(-centerX, circle.getP().x[0]);
    }

    @Test
    void cross() {
        double[] p1 = new double[2]; p1[0]=centerX; p1[1]=centerY;
        double[] p2 = new double[2]; p2[0]=centerX+radius; p2[1]=centerY+radius;
        Segment segment = new Segment(new Point2D(p1), new Point2D(p2));
        double[] x = new double[2]; x[0]=centerX; x[1]=centerY;
        Circle circle2 = new Circle(new Point2D(circle.getP().x), radius/2);
        x[0]=centerX+radius; x[1]=centerY+radius;
        Circle circle3 = new Circle(new Point2D(x), radius);
        Circle circle4 = new Circle(new Point2D(circle.getP().x), radius*2);
        assertAll(
                ()->assertTrue(circle.cross(segment)),
                ()->assertFalse(circle.cross(circle2)),
                ()->assertTrue(circle.cross(circle3)),
                ()->assertFalse(circle.cross(circle4))
        );
    }

    @Test
    void testToString() {
        assertTrue(circle.toString() instanceof String);
    }


    @Test
    void testToBson() {
        assertTrue(circle.toBson() instanceof Document);
    }
}