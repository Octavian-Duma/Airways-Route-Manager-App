package org.example.gui;

import org.example.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RouteManagerGUI extends JFrame {
    private RouteManager routeManager;

    public RouteManagerGUI() {
        this.routeManager = new RouteManagerImpl();
        setTitle("Route Manager");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton createButton = new JButton("Create Route");
        JButton loadButton = new JButton("Load Route");
        JButton addWaypointButton = new JButton("Add Waypoint");
        JButton deleteButton = new JButton("Delete Route");
        JTextArea displayArea = new JTextArea(20, 40);

        panel.add(createButton);
        panel.add(loadButton);
        panel.add(addWaypointButton);
        panel.add(deleteButton);
        panel.add(new JScrollPane(displayArea));

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routeName = JOptionPane.showInputDialog("Enter route name:");
                routeManager.createRoute(routeName);
                displayArea.append("Route " + routeName + " created.\n");
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routeName = JOptionPane.showInputDialog("Enter route name to load:");
                Route route = routeManager.loadRoute(routeName);
                displayArea.append("Route " + route.getName() + " loaded.\n");
                for (Waypoint wp : route.getWaypoints()) {
                    displayArea.append(wp.getName() + "\n");
                }
                displayArea.append("Total Distance: " + route.calculateTotalDistance() + " km\n");
            }
        });

        addWaypointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routeName = JOptionPane.showInputDialog("Enter route name to add waypoint to:");
                String waypointName = JOptionPane.showInputDialog("Enter waypoint name:");
                double latitude = Double.parseDouble(JOptionPane.showInputDialog("Enter latitude:"));
                double longitude = Double.parseDouble(JOptionPane.showInputDialog("Enter longitude:"));
                Waypoint waypoint = new Waypoint(waypointName, latitude, longitude);
                routeManager.addWaypointToRoute(routeName, waypoint);
                displayArea.append("Waypoint " + waypoint.getName() + " added to " + routeName + ".\n");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routeName = JOptionPane.showInputDialog("Enter route name to delete:");
                routeManager.deleteRoute(routeName);
                displayArea.append("Route " + routeName + " deleted.\n");
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new RouteManagerGUI();
    }
}
