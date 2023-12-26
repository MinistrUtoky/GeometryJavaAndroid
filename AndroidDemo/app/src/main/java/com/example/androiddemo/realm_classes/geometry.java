package com.example.androiddemo.realm_classes;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class geometry extends RealmObject {

    @PrimaryKey
    @Required
    private UUID _id;
    @Required
    private String name;
    @Required
    private RealmList<UUID> objectIds;

    public UUID get_id() { return _id; }

    public void set_id(UUID _id) { this._id = _id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public RealmList<UUID> getObjectIds() { return objectIds; }

    public void setObjectIds(RealmList<UUID> objectIds) { this.objectIds = objectIds; }
}
