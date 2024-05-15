package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public boolean isMarque(Label tas, Node node) {
        //vérifier si le cost est infini
        if (tas.cost == Double.POSITIVE_INFINITY) {
            return false;
        }
        else {
            return true;
        }
    }

    public void setMarque(Label tas) {
        tas.marque = true;
    }
        
    @Override
    protected ShortestPathSolution doRun() { 
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        ArrayList<Node> tas = new ArrayList<Node>();
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        Graph graph = data.getGraph();
        Node origin = data.getOrigin();
        graph.getNodes().forEach(node -> listeLabel.add(new Label(node)));




        // Initialize array of distances.
        double[] distances = new double[graph.size()];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[origin.getId()] = 0;

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[graph.size()];
        Arrays.fill(predecessorArcs, null);

        // Initialize the binary heap
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        tas.add(origin);
        listeLabel.get(origin.getId()).cost = 0;
        heap.insert(listeLabel.get(origin.getId()));

        while (!heap.isEmpty()) {






















            /*             Label x = heap.deleteMin();
            setMarque(x);
            for (Arc arc : x.sommet_courant.getSuccessors()) {
                Node y = arc.getDestination();
                if (!data.isAllowed(arc)) {
                    continue;
                }
                if (!isMarque(listeLabel.get(y.getId()), y)) {
                    double oldDistance = listeLabel.get(y.getId()).cost;
                    double newDistance = listeLabel.get(x.sommet_courant.getId()).cost + data.getCost(arc);
                    if (newDistance < oldDistance) {
                        listeLabel.get(y.getId()).cost = newDistance;
                        tas.add(y);
                        heap.insert(listeLabel.get(y.getId()));
                        predecessorArcs[y.getId()] = arc;
                    }
                }
            } */
        }

        return solution;
    }

}
