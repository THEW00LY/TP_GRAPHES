package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;



public class AStarAlgorithm extends DijkstraAlgorithm {


    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        
    }

    @Override
    public ArrayList<Label> Initialisation(Graph graph, Node destination) {
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        for (Node node : graph.getNodes()) {
            listeLabel.add(new LabelStar(node, destination));
        }
        return listeLabel;
    }

}
