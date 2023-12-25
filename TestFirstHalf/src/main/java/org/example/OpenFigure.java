package org.example;


public abstract class OpenFigure implements IShape {
    public double square() { return 0; }
    public abstract double length();
    public abstract IShape shift(Point2D a);
    public abstract IShape rot(double phi);
    public abstract IShape symAxis(int i);
    public abstract boolean cross(IShape i);
}
