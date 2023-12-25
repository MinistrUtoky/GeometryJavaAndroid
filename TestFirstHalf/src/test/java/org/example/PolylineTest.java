package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolylineTest {

    Polyline polyline;
    double startX=0, startY=1;
    int size = 6;
    Point2D[] NewEncircledPointCollection(int newSize){
        Point2D[] points = new Point2D[newSize];
        for (int i = 0; i < newSize; i++){
            double[] x = new double[2];
            x[0] = startX; x[1] = startY;
            Point2D p = new Point2D(x);
            p=p.rot(2*Math.PI/newSize*i);
            points[i] = p;
        }
        return points;
    }
    @BeforeEach
    void setUp(){
        Point2D[] points = NewEncircledPointCollection(size);
        polyline = new Polyline(points);
    }
    @Test
    void getN() {
        assertEquals(size, polyline.getN());
    }

    @Test
    void getP() {
        Point2D[] points = NewEncircledPointCollection(size);
        for (int i = 0; i < size; i++)
            assertArrayEquals(polyline.getP()[i].x, points[i].x);
    }

    @Test
    void testGetP() {
        Point2D[] points = NewEncircledPointCollection(size);
        for (int i = 0; i < size; i++)
            assertArrayEquals(polyline.getP(i).x, points[i].x);
    }

    @Test
    void setP() {
        Point2D[] points = NewEncircledPointCollection(size+1);
        polyline.setP(points);
        for (int i = 0; i < size+1; i++)
            assertArrayEquals(polyline.getP(i).x, points[i].x);
    }

    @Test
    void testSetP() {
        Point2D[] points = NewEncircledPointCollection(size);
        for (int i = 0; i < size-1; i++)
            polyline.setP(points[i] ,i);
        for (int i = 0; i < size-1; i++)
            assertArrayEquals(polyline.getP(i).x, points[i].x);
    }

    @Test
    void length() {
        double l = 0;
        for (int i = 1; i < size; i++) {
            l += Point.sub(polyline.getP(i), polyline.getP(i-1)).abs();
        }
        assertEquals(l, polyline.length());
    }

    @Test
    void shift() {
        Point2D[] points = NewEncircledPointCollection(size);
        polyline.shift(polyline.getP(0));
        for (int i = 0; i < size; i++){
            points[i].x[0] += points[0].x[0];
            points[i].x[1] += points[0].x[1];
        }
        for (int i = 0; i < size; i++)
            assertArrayEquals(polyline.getP(i).x, points[i].x);
    }

    @Test
    void rot() {
        Point2D[] points = NewEncircledPointCollection(size);
        for (int i = 0; i < size; i++)
            points[i].rot(Math.PI/2);
        polyline.rot(Math.PI/2);
        for (int i = 0; i < size; i++)
            assertArrayEquals(polyline.getP(i).x, points[i].x);
    }

    @Test
    void symAxis() {
        Point2D[] points = NewEncircledPointCollection(size);
        Polyline polyline2 = new Polyline(points);
        polyline.symAxis(0);
        polyline.symAxis(1);
        for (int i = 0; i < size; i++) {
            assertEquals(-polyline2.getP(i).x[1], polyline.getP(i).x[1]);
            assertEquals(-polyline2.getP(i).x[0], polyline.getP(i).x[0]);
        }
    }

    @Test
    void cross() {
        double[] p1 = new double[2], p2 = new double[2];
        p1[0]=0; p1[1]=0;
        p2[0]=startY; p2[1]=startX;
        Segment segment2 = new Segment(new Point2D(p1), new Point2D(p2));
        double[] x = new double[2]; x[0]=startX; x[1]=startY;
        Circle circle2 = new Circle(polyline.getP(1), Point.sub(polyline.getP(1), polyline.getP(0)).abs());
        assertAll(
                () -> assertTrue(polyline.cross(segment2)),
                () -> assertTrue(polyline.cross(circle2)),
                () -> assertFalse(
                        polyline.cross(new Segment(new Point2D(new double[2]), new Point2D(new double[2])))
                )
        );
    }

    @Test
    void square(){
        assertEquals(0, polyline.square());
    }

    @Test
    void testToString() {
        assertTrue(polyline.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(polyline.toBson() instanceof Document);
    }
}