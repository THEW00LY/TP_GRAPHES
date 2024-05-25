package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double estimatedCost;

    public LabelStar(Node sommet_courant, Node destination) {
        super(sommet_courant);
        this.estimatedCost = sommet_courant.getPoint().distanceTo(destination.getPoint());
    }

    public double getEstimatedCost() {
        return this.estimatedCost;
    }

    /*
    private double calculateEstimatedCost(Node node, Node destination) {
        return node.getPoint().distanceTo(destination.getPoint());
    }
    */

    @Override
    public double getTotalCost() {
        return this.getCost() + this.estimatedCost;
    }

}