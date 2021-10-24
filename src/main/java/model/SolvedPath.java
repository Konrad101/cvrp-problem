package model;

import model.city.connection.CitiesConnection;

import java.util.List;
import java.util.Objects;

public class SolvedPath {

    private final List<CitiesConnection> connections;

    public SolvedPath(List<CitiesConnection> connections) {
        this.connections = connections;
    }

    public List<CitiesConnection> getConnections() {
        return connections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolvedPath path = (SolvedPath) o;
        return this.connections.equals(path.getConnections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(connections);
    }
}
