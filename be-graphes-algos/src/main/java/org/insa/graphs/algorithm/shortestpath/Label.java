package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    protected Node sommet_courant;
    protected boolean marque;
    protected double cost;
    protected Node father;

    public Label(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
        this.marque = false;
        this.cost = Double.POSITIVE_INFINITY;
        this.father = null;
    }

    public double getCost() {
        return this.cost;
    }

    public Node getSommet() {
        return sommet_courant;
    }

    public double getTotalCost() {
        return this.cost;
    }

    public boolean isMarque() {
        return this.marque;
    }

    public Node getFather() {
        return this.father;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.getTotalCost(), other.getTotalCost());
    }
}