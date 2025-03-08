package org.example.gui;

import org.example.model.Route;
import org.example.model.Waypoint;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RouteViewer extends JPanel {
    private JXMapViewer mapViewer;
    private org.example.model.RouteManager routeManager;
    private JComboBox<String> routeSelector;
    private DefaultComboBoxModel<String> routeModel;
    private JLabel distanceLabel;

    public RouteViewer() {
        routeManager = new org.example.model.RouteManagerImpl();
        setLayout(new BorderLayout());

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        GeoPosition cluj = new GeoPosition(46.770439, 23.591423); // Cluj-Napoca
        mapViewer.setAddressLocation(cluj);
        mapViewer.setZoom(7);

        JPanel controlPanel = new JPanel();

        routeModel = new DefaultComboBoxModel<>();
        routeSelector = new JComboBox<>(routeModel);

        JButton createRouteBtn = new JButton("Create Route");
        JButton addWaypointBtn = new JButton("Add Waypoint");
        JButton showRouteBtn = new JButton("View Route");
        JButton deleteRouteBtn = new JButton("Delete Route");
        JButton calculateDistanceBtn = new JButton("Calculate Distance");

        controlPanel.add(routeSelector);
        controlPanel.add(createRouteBtn);
        controlPanel.add(addWaypointBtn);
        controlPanel.add(showRouteBtn);
        controlPanel.add(deleteRouteBtn);
        controlPanel.add(calculateDistanceBtn);

        createRouteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routeName = JOptionPane.showInputDialog("Route Name:");
                if (routeName != null && !routeName.isEmpty()) {
                    routeManager.createRoute(routeName);
                    routeModel.addElement(routeName);
                    JOptionPane.showMessageDialog(RouteViewer.this, "Route created!");
                }
            }
        });

        addWaypointBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (routeSelector.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(RouteViewer.this, "Select a route!");
                    return;
                }

                String routeName = (String) routeSelector.getSelectedItem();
                String waypointName = JOptionPane.showInputDialog("Waypoint name:");

                try {
                    double latitude = Double.parseDouble(JOptionPane.showInputDialog("Latitude (ex: 46.770439):"));
                    double longitude = Double.parseDouble(JOptionPane.showInputDialog("Longitude(ex: 23.591423):"));

                    Waypoint waypoint = new Waypoint(waypointName, latitude, longitude);
                    routeManager.addWaypointToRoute(routeName, waypoint);
                    JOptionPane.showMessageDialog(RouteViewer.this, "Waypoint added to route!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(RouteViewer.this, "Invalid values for coordinates!");
                }
            }
        });

        showRouteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (routeSelector.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(RouteViewer.this, "Select a route!");
                    return;
                }

                String routeName = (String) routeSelector.getSelectedItem();
                Route route = routeManager.loadRoute(routeName);

                if (route != null && !route.getWaypoints().isEmpty()) {
                    displayRouteOnMap(route);
                    updateDistanceLabel(route);
                } else {
                    JOptionPane.showMessageDialog(RouteViewer.this, "The route contains no waypoints!");
                }
            }
        });

        deleteRouteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (routeSelector.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(RouteViewer.this, "Select a route to delete!");
                    return;
                }

                String routeName = (String) routeSelector.getSelectedItem();

                int confirm = JOptionPane.showConfirmDialog(
                        RouteViewer.this,
                        "Are you sure you want to delete the route: " + routeName + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    routeManager.deleteRoute(routeName);
                    routeModel.removeElement(routeName);
                    JOptionPane.showMessageDialog(RouteViewer.this, "Route deleted successfully!");
                    mapViewer.setOverlayPainter(null);
                    mapViewer.repaint();
                    distanceLabel.setText("Total Distance: - km");
                }
            }
        });

        calculateDistanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (routeSelector.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(RouteViewer.this, "Select a route!");
                    return;
                }

                String routeName = (String) routeSelector.getSelectedItem();
                Route route = routeManager.loadRoute(routeName);

                if (route != null && route.getWaypoints().size() > 1) {
                    double distance = route.calculateTotalDistance();
                    JOptionPane.showMessageDialog(
                            RouteViewer.this,
                            "Total distance for route " + routeName + " is: " +
                                    String.format("%.2f", distance) + " km"
                    );
                    updateDistanceLabel(route);
                } else {
                    JOptionPane.showMessageDialog(
                            RouteViewer.this,
                            "Route needs at least 2 waypoints to calculate distance!"
                    );
                }
            }
        });

        add(controlPanel, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        JLabel statusLabel = new JLabel(" Airways Route Manager");
        distanceLabel = new JLabel("Total Distance: - km");
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(distanceLabel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void updateDistanceLabel(Route route) {
        if (route != null && route.getWaypoints().size() > 1) {
            double distance = route.calculateTotalDistance();
            distanceLabel.setText("Total Distance: " + String.format("%.2f", distance) + " km");
        } else {
            distanceLabel.setText("Total Distance: - km");
        }
    }

    private void displayRouteOnMap(Route route) {
        Set<org.jxmapviewer.viewer.Waypoint> mapWaypoints = new HashSet<>();
        List<GeoPosition> routePositions = new ArrayList<>();

        for (Waypoint waypoint : route.getWaypoints()) {
            GeoPosition pos = new GeoPosition(waypoint.getLatitude(), waypoint.getLongitude());
            mapWaypoints.add(new DefaultWaypoint(pos));
            routePositions.add(pos);
        }

        if (!routePositions.isEmpty()) {
            mapViewer.setAddressLocation(routePositions.get(0));
        }

        WaypointPainter<org.jxmapviewer.viewer.Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(mapWaypoints);

        Painter<JXMapViewer> routePainter = new Painter<JXMapViewer>() {
            @Override
            public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
                g = (Graphics2D) g.create();

                Rectangle rect = map.getViewportBounds();
                g.translate(-rect.x, -rect.y);

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(3));
                g.setColor(Color.RED);

                for (int i = 0; i < routePositions.size() - 1; i++) {
                    Point2D p1 = map.getTileFactory().geoToPixel(routePositions.get(i), map.getZoom());
                    Point2D p2 = map.getTileFactory().geoToPixel(routePositions.get(i + 1), map.getZoom());

                    g.draw(new Line2D.Double(p1, p2));
                }

                g.dispose();
            }
        };

        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(compoundPainter);

        if (routePositions.size() > 1) {
            mapViewer.zoomToBestFit(new HashSet<>(routePositions), 0.7);
        }
    }

    public void addTestData() {
        // Route Cluj-București
        routeManager.createRoute("Cluj-București");
        routeModel.addElement("Cluj-București");
        routeManager.addWaypointToRoute("Cluj-București", new Waypoint("Cluj-Napoca", 46.770439, 23.591423));
        routeManager.addWaypointToRoute("Cluj-București", new Waypoint("Sibiu", 45.792784, 24.152069));
        routeManager.addWaypointToRoute("Cluj-București", new Waypoint("București", 44.4268, 26.1025));

        // Route Cluj-Timișoara
        routeManager.createRoute("Cluj-Timișoara");
        routeModel.addElement("Cluj-Timișoara");
        routeManager.addWaypointToRoute("Cluj-Timișoara", new Waypoint("Cluj-Napoca", 46.770439, 23.591423));
        routeManager.addWaypointToRoute("Cluj-Timișoara", new Waypoint("Timișoara", 45.760696, 21.226788));

        // Route Cluj-Sibiu-Suceava
        routeManager.createRoute("Cluj-Sibiu-Suceava");
        routeModel.addElement("Cluj-Sibiu-Suceava");
        routeManager.addWaypointToRoute("Cluj-Sibiu-Suceava", new Waypoint("Cluj-Napoca", 46.770439, 23.591423));
        routeManager.addWaypointToRoute("Cluj-Sibiu-Suceava", new Waypoint("Sibiu", 45.792784, 24.152069));
        routeManager.addWaypointToRoute("Cluj-Sibiu-Suceava", new Waypoint("Suceava", 47.663433, 26.273416));

        // Route Craiova-Constanța
        routeManager.createRoute("Craiova-Constanța");
        routeModel.addElement("Craiova-Constanța");
        routeManager.addWaypointToRoute("Craiova-Constanța", new Waypoint("Craiova", 44.319305, 23.800678));
        routeManager.addWaypointToRoute("Craiova-Constanța", new Waypoint("Constanța", 44.172392, 28.638179));

        // Route Cluj-Londra
        routeManager.createRoute("Cluj-Londra");
        routeModel.addElement("Cluj-Londra");
        routeManager.addWaypointToRoute("Cluj-Londra", new Waypoint("Cluj-Napoca", 46.770439, 23.591423));
        routeManager.addWaypointToRoute("Cluj-Londra", new Waypoint("Londra", 51.5074, -0.1278));

        // Route Sibiu-Madrid
        routeManager.createRoute("Sibiu-Madrid");
        routeModel.addElement("Sibiu-Madrid");
        routeManager.addWaypointToRoute("Sibiu-Madrid", new Waypoint("Sibiu", 45.792784, 24.152069));
        routeManager.addWaypointToRoute("Sibiu-Madrid", new Waypoint("Madrid", 40.4168, -3.7038));

        // Route București-Dubai
        routeManager.createRoute("București-Dubai");
        routeModel.addElement("București-Dubai");
        routeManager.addWaypointToRoute("București-Dubai", new Waypoint("București", 44.4268, 26.1025));
        routeManager.addWaypointToRoute("București-Dubai", new Waypoint("Dubai", 25.276987, 55.296249));

    }
}