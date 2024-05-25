package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label implements Comparable<Label> {
    
    private double estimatedCost;

    public LabelStar(Node sommet_courant, double estimatedCost){
        super(sommet_courant);
        this.estimatedCost = estimatedCost;
    }

    @Override
    public double getTotalCost(){
        return this.getCost() + this.estimatedCost;
    }
}
