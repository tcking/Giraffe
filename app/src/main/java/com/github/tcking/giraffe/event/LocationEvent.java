package com.github.tcking.giraffe.event;

import android.location.Location;

/**
 * Created by tc(mytcking@gmail.com) on 15/7/31.
 */
public class LocationEvent extends BaseEvent {
    private Location location;
    public Location getLocation() {
        return location;
    }

    public LocationEvent(Location location) {

        this.location = location;
    }
}
