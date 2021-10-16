package model;

import model.city.connection.CitiesConnection;

import java.util.List;

public class SolvedPath {

    private final List<CitiesConnection> connections;

    public SolvedPath(List<CitiesConnection> connections) {
        this.connections = connections;
    }

    public List<CitiesConnection> getConnections() {
        return connections;
    }

}
