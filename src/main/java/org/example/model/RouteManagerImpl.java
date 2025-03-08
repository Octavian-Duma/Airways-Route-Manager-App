package org.example.model;

import java.util.HashMap;
import java.util.Map;

public class RouteManagerImpl implements RouteManager {
    private Map<String, Route> routes = new HashMap<>();

    @Override
    public void createRoute(String routeName) {
        if (routeName != null && !routeName.isEmpty()) {
            routes.put(routeName, new Route(routeName));
        }
    }

    @Override
    public Route loadRoute(String routeName) {
        return routes.get(routeName);
    }

    @Override
    public void addWaypointToRoute(String routeName, Waypoint waypoint) {
        Route route = routes.get(routeName);
        if (route != null && waypoint != null) {
            route.addWaypoint(waypoint);
        }
    }

    @Override
    public void deleteRoute(String routeName) {
        routes.remove(routeName);
    }


    public String[] getAllRouteNames() {
        return routes.keySet().toArray(new String[0]);
    }
}