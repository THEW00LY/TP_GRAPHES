package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    Node sommet_courant;
    boolean marque;
    double cost;
    Node father;

    public Label(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
        this.marque = false;
        this.cost = Double.POSITIVE_INFINITY;
        this.father = null;
    }





    public double getCost() {
        return this.cost;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.cost, other.cost);
    }

}