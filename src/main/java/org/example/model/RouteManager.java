package org.example.model;

public interface RouteManager {
    void createRoute(String routeName);

    Route loadRoute(String routeName);

    void addWaypointToRoute(String routeName, Waypoint waypoint);

    void deleteRoute(String routeName);
}
