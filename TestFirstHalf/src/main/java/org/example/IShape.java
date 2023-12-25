package org.example;

import org.bson.Document;

public interface IShape
{
    public double square();
    public double length();
    public IShape shift(Point2D a);
    public IShape rot(double phi);
    public IShape symAxis(int i);
    public boolean cross(IShape i);
    public String toString();
    public Document toBson();
}
