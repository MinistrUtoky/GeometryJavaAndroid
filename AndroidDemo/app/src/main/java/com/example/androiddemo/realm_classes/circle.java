package com.example.androiddemo.realm_classes;

import io.realm.RealmObject;
import org.bson.types.ObjectId;

import java.util.UUID;

import io.realm.annotations.Required;
import io.realm.annotations.PrimaryKey;

public class circle extends RealmObject {

    @PrimaryKey
    @Required
    private UUID _id;
    @Required
    private String name;
    @Required
    private String point;
    private double radius;

    public UUID get_id() { return _id; }

    public void set_id(UUID _id) { this._id = _id; }

    public String getPoint() { return point; }

    public void setPoint(String point) { this.point = point; }

    public double getRadius() { return radius; }

    public void setRadius(double radius) { this.radius = radius; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
