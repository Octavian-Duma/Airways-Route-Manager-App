Airways Route Manager App

Description
Airways Route Manager is a Java-based application that allows users to create, view, and manage geographical routes, including adding waypoints, calculating distances between points, and visualizing the routes on a map.
It provides an intuitive user interface built using Swing for route management, integrated with JXMapViewer for map visualization.

This project offers users the ability to:

Create new routes and manage waypoints along them.
View and visualize routes on a map, including their waypoints.
Calculate the total distance of a route based on the distance between consecutive waypoints.
Delete routes and update the map view accordingly.
The application includes a graphical user interface (GUI) and is designed for managing and displaying routes with geographical data.

Features
1. Route Management
Create Route: Users can create new routes by providing a route name.
Load Route: Load a route and display its waypoints along with the total distance.
Add Waypoint: Add a waypoint (location) to an existing route, specifying its name, latitude, and longitude.
Delete Route: Users can delete routes by name, and all associated waypoints are removed as well.
2. Route Visualization on Map
Routes and waypoints are displayed on a map using the JXMapViewer library.
Routes are drawn as a path between waypoints, and the map automatically adjusts to show the full route.
The map can be zoomed in or out to view different areas of the route.
3. Distance Calculation
The app can calculate and display the total distance of a route by summing up the distances between consecutive waypoints using the Haversine formula.
4. GUI Components
RouteViewer: A main window that provides interactive elements such as:
Buttons for creating routes, adding waypoints, and viewing routes on the map.
A combo box to select and manage routes.
A map panel that shows the route's waypoints and the paths between them.
RouteManager: Manages routes and their associated waypoints in the background. The RouteManager handles the logic of adding and deleting routes, as well as loading and storing route data.
5. Data Persistence
Routes and waypoints are stored in memory and managed using a RouteManagerImpl class, which facilitates the creation, loading, and deletion of routes.


Usage
Create a Route: Click on the "Create Route" button, enter a route name, and the route will be created.
Add a Waypoint: Select a route, then click "Add Waypoint", provide the waypoint name and coordinates (latitude and longitude).
View Route on Map: After creating a route, select it from the dropdown and click "View Route" to visualize the route and its waypoints on the map.
Calculate Total Distance: After adding multiple waypoints to a route, select the route and click "Calculate Distance" to see the total distance between the waypoints.
Delete a Route: Select a route from the dropdown and click "Delete Route" to remove it from the list.

Setup Instructions
Clone the Repository: Clone the project repository to your local machine.
Build the Project: Open the project in an IDE such as IntelliJ IDEA or Eclipse and build the project using Maven or Gradle.
Run the Application: After building the project, run the Main class to launch the RouteViewer window.
Dependencies
JXMapViewer: A library for rendering maps and handling geographical coordinates.
Swing: Used for building the GUI components such as buttons, combo boxes, and panels.
Example Route Data
The project includes preloaded example routes such as:

Cluj-București: A route between Cluj-Napoca, Sibiu, and Bucharest.
Cluj-Timișoara: A route between Cluj-Napoca and Timișoara.
Craiova-Constanța: A route connecting Craiova and Constanța.

