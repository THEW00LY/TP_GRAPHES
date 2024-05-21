package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label implements Comparable<Label>{

    public double estimatedCost;


    public LabelStar(Node sommet_courant, double estimatedCost) {
        super(sommet_courant);
        this.estimatedCost = estimatedCost;
    }

    public double getTotalCost() {
        return this.cost + this.estimatedCost;
    }   


    @Override
    public int compareTo(Label other) {
        return Double.compare(this.getTotalCost(), ((LabelStar) other).getTotalCost());
    }

}
