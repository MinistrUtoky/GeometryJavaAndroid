package org.example;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    Point point;
    int dim = 2;
    @BeforeEach
    void setUp() {
        point = new Point(dim);
    }

    @Test
    void getDim() {
        assertEquals(dim, point.getDim());
    }

    @Test
    void getX() {
        assertArrayEquals(new double[dim], point.getX());
    }

    @Test
    void setX() {
        double[] x_new = new double[3];
        point.setX(x_new);
        assertArrayEquals(x_new, point.getX());
    }

    @Test
    void testSetX() {
        double x_new = 1; int i = 0;
        point.setX(x_new, i);
        assertEquals(x_new, point.getX()[i]);
    }

    @Test
    void abs() {
        double sqSum = 0;
        for (double c : point.getX())
            sqSum+=c;
        assertEquals(Math.sqrt(sqSum), point.abs());
    }

    @Test
    void add() {
        double[] x2 = point.getX();
        for (int i = 0; i < point.getX().length; i++) x2[i] += point.getX()[i];
        point = Point.add(point, point);
        assertArrayEquals(x2, point.getX());
    }

    @Test
    void testAdd() {
        double[] x2 = point.getX();
        for (int i = 0; i < point.getX().length; i++) x2[i] += point.getX()[i];
        point.add(point);
        assertArrayEquals(x2, point.getX());
    }

    @Test
    void sub() {
        double[] x2 = point.getX();
        for (int i = 0; i < point.getX().length; i++) x2[i] -= point.getX()[i];
        point = Point.sub(point, point);
        assertArrayEquals(x2, point.getX());
    }

    @Test
    void testSub() {
        double[] x2 = point.getX();
        for (int i = 0; i < point.getX().length; i++) x2[i] -= point.getX()[i];
        point.sub(point, point);
        assertArrayEquals(x2, point.getX());
    }

    @Test
    void mult() {
        double[] x2 = point.getX(); double multiplicator = point.getX()[0];
        for (int i = 0; i < point.getX().length; i++) x2[i] *= multiplicator;
        assertArrayEquals(x2, Point.mult(point, multiplicator).getX());
    }

    @Test
    void testMult() {
        double[] x2 = point.getX(); double multiplicator = point.getX()[0];
        for (int i = 0; i < point.getX().length; i++) x2[i] *= multiplicator;
        assertArrayEquals(x2, point.mult(multiplicator).getX());
    }

    @Test
    void testMult1() {
        double x = 0;
        for (int i = 0; i < point.getX().length; i++) x += point.getX()[i]*point.getX()[i];
        assertEquals(x, Point.mult(point, point));
    }

    @Test
    void testMult2() {
        double x = 0;
        for (int i = 0; i < point.getX().length; i++) x += point.getX()[i]*point.getX()[i];
        assertEquals(x, point.mult(point));
    }

    @Test
    void symAxis() {
        double x = point.getX()[0];
        double y = point.getX()[1];
        Point.symAxis(point,0);
        assertEquals(Math.abs(-x), Math.abs(point.getX()[0]));
        Point.symAxis(point,1);
        assertEquals(Math.abs(-y), Math.abs(point.getX()[1]));
    }

    @Test
    void testSymAxis() {
        double x = point.getX()[0];
        double y = point.getX()[1];
        point.symAxis(0);
        assertEquals(Math.abs(-x), Math.abs(point.getX()[0]));
        point.symAxis(1);
        assertEquals(Math.abs(-y), Math.abs(point.getX()[1]));
    }

    @Test
    void testToString() {
        assertTrue(point.toString() instanceof String);
    }

    @Test
    void testToBson() {
        assertTrue(point.toBson() instanceof Document);
    }
}