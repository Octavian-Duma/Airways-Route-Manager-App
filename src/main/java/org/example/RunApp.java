package org.example;

import org.example.gui.RouteViewer;

import javax.swing.*;

public class RunApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Airways Route Manager");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                RouteViewer routeViewer = new RouteViewer();
                routeViewer.addTestData();

                frame.getContentPane().add(routeViewer);
                frame.setSize(1000, 800);
                frame.setVisible(true);
            }
        });
    }
}