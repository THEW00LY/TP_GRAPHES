package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

	public AStarAlgorithm(ShortestPathData data) {
		super(data);
	}
	
	@Override
    public void Initialisation(Graph graph, ShortestPathData data, ArrayList<Label> listeLabel) {
        for (Node node : graph.getNodes()) { 
            //System.out.println("distance a vol d'oiseau : " + node.getPoint().distanceTo(data.getDestination().getPoint()));
            listeLabel.add(new LabelStar(node, node.getPoint().distanceTo(data.getDestination().getPoint())));
        }
    }

}
