package com.example.androiddemo.realm_classes;

import org.bson.types.ObjectId;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class segment extends RealmObject {
    @PrimaryKey
    @Required
    private UUID _id;
    @Required
    private String name;
    @Required
    private String start;
    @Required
    private String end;
    public UUID get_id() { return _id; }

    public void set_id(UUID _id) { this._id = _id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStart() { return start; }

    public void setStart(String start) { this.start = start; }
    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }
}