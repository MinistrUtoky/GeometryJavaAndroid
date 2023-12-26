package com.example.androiddemo.realm_classes;


import io.realm.RealmObject;
import org.bson.types.ObjectId;

import java.util.UUID;

import io.realm.annotations.Required;
import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class ngon extends RealmObject {
    @PrimaryKey
    @Required
    private UUID _id;
    @Required
    private String name;
    @Required
    private RealmList<String> points;

    public UUID get_id() { return _id; }

    public void set_id(UUID _id) { this._id = _id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public RealmList<String> getPoints() { return points; }

    public void setPoints(RealmList<String> points) { this.points = points; }


}
