package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        // Retrieve the graph.
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of distances.
        double[] distances = new double[nbNodes]; 
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[data.getOrigin().getId()] = 0;

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes]; 

        Node Firstnode = data.getOrigin();
        ArrayList<Node> Noeuds = new ArrayList<Node>();
        Noeuds.add(Firstnode);
        while(!Noeuds.isEmpty()) {
        	Node x = Noeuds.get(0);
        	Noeuds.remove(0);
        	for(Arc arc : x.getSuccessors()) {
        		Node y = arc.getDestination();
        		if(!data.isAllowed(arc)) {
        			continue;
        		}
        		double oldDistance = distances[y.getId()];
        		double newDistance = distances[x.getId()] + data.getCost(arc);
        		if(Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
        			notifyNodeReached(y);
        		}
        		if(newDistance < oldDistance) {
        			distances[y.getId()] = newDistance;
        			predecessorArcs[y.getId()] = arc;
        			if(Noeuds.contains(y)) {
        				Noeuds.remove(y);
        			}
        			Noeuds.add(y);
        		}
        	}
        }
    
        
        

        return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, predecessorArcs, data.getDestination(), data.getGraph()));
    }

}
