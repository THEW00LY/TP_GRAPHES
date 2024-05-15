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

    public DijkstraAlgorithm(ShortestPathData data) { 
        super(data);
    }


    public boolean isMarque() {
        return this.marque;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        ArrayList<Node> tas = new ArrayList<Node>();

        Graph graph = data.getGraph();
        Node origin = data.getOrigin();

        // Initialize array of distances.
        double[] distances = new double[graph.size()];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[origin.getId()] = 0;

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[graph.size()];
        Arrays.fill(predecessorArcs, null);

        // Initialize the binary heap
        BinaryHeap<Node> heap = new BinaryHeap<Node>();
        tas.add(origin);
        heap.insert(origin);
        origin.setMarque(true);

        notifyOriginProcessed(origin);


        while (!heap.isEmpty()) {
            Node node = heap.deleteMin();
            node.setMarque(false);
            notifyNodeMarked(node);

            for (Arc arc : node.getSuccessors()) {
                Node successor = arc.getDestination();
                if (!data.isAllowed(arc)) {
                    continue;
                }

                double oldDistance = distances[successor.getId()];
                double newDistance = distances[node.getId()] + data.getCost(arc);

                if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                    notifyNodeReached(successor);
                    tas.add(successor);
                }

                if (newDistance < oldDistance) {
                    distances[successor.getId()] = newDistance;
                    predecessorArcs[successor.getId()] = arc;

                    if (tas.contains(successor)) {
                        tas.remove(successor);
                    }

                    if (!successor.isMarque()) {
                        heap.insert(successor);
                        tas.add(successor);
                        successor.setMarque(true);
                    } else {
                        heap.remove(successor);
                        heap.insert(successor);
                    }
                }
            }
        }


        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }


        return solution;
    }

}
