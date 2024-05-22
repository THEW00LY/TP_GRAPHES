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

public class BellmanFordAlgorithm extends ShortestPathAlgorithm {


    public BellmanFordAlgorithm(ShortestPathData data) {
        super(data);
    }
 
    @Override
    protected ShortestPathSolution doRun() { 
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        ArrayList<Node> file = new ArrayList<Node>();
        ArrayList<Label> listeLabel = new ArrayList<Label>();

        Graph graph = data.getGraph();
        //parcours en profondeur du graphe jusqu'à dépasser 20km
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        graph.getNodes().forEach(node -> listeLabel.add(new Label(node)));
        for(int i = 0; i < listeLabel.size(); i++) {
            if( i != listeLabel.get(i).sommet_courant.getId())
                System.out.println(listeLabel.get(i).sommet_courant.getId() + " " + i);
        }
        //initialisation du tableau des arcs précédents
        Arc[] predecessorArcs = new Arc[graph.size()];
        Arrays.fill(predecessorArcs, null);

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        file.add(origin);
        listeLabel.get(origin.getId()).cost = 0;
        heap.insert(listeLabel.get(origin.getId()));

        while (!heap.isEmpty() && !listeLabel.get(destination.getId()).isMarque()){

            Label x = heap.deleteMin();
            x.setMarque(true);
            notifyNodeMarked(x.sommet_courant);
            for (Arc arc : x.sommet_courant.getSuccessors()) {
                Node y = arc.getDestination();
                if (!data.isAllowed(arc)) {
                    continue;
                }
                if (!listeLabel.get(y.getId()).isMarque()) {
                    double oldDistance = listeLabel.get(y.getId()).getCost();
                    double newDistance = listeLabel.get(x.sommet_courant.getId()).getCost() + data.getCost(arc);
                    if (newDistance < oldDistance) {
                        if(oldDistance != Double.POSITIVE_INFINITY) {
                            heap.remove(listeLabel.get(y.getId()));
                        }
                        
                        listeLabel.get(y.getId()).setCost(newDistance);
                        notifyNodeReached(y);
                        heap.insert(listeLabel.get(y.getId()));
                        listeLabel.get(y.getId()).father = x.sommet_courant;
                        predecessorArcs[y.getId()] = arc;
                    }
                }
            } 
        }
        //si la destination a été atteinte

        if (listeLabel.get(destination.getId()).cost != Double.POSITIVE_INFINITY) {
            ArrayList<Arc> arcs = new ArrayList<>();
            Node current = destination;
            while (current != origin) {
                Arc arc = predecessorArcs[current.getId()];
                arcs.add(arc);
                current = arc.getOrigin();
            }
            Collections.reverse(arcs);
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        else {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        return solution;

    }
}


/* package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class BellmanFordAlgorithm extends ShortestPathAlgorithm {

    public BellmanFordAlgorithm(ShortestPathData data) {
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

        // Actual algorithm, we will assume the graph does not contain negative
        // cycle...
        boolean found = false;
        for (int i = 0; !found && i < nbNodes; ++i) {
            found = true;
            for (Node node: graph.getNodes()) {
                for (Arc arc: node.getSuccessors()) {

                    // Small test to check allowed roads...
                    if (!data.isAllowed(arc)) {
                        continue;
                    }

                    // Retrieve weight of the arc.
                    double w = data.getCost(arc);
                    double oldDistance = distances[arc.getDestination().getId()];
                    double newDistance = distances[node.getId()] + w;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                    }

                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        found = false;
                        distances[arc.getDestination().getId()] = distances[node.getId()] + w;
                        predecessorArcs[arc.getDestination().getId()] = arc;
                    }
                }
            }
        }

        ShortestPathSolution solution = null;

        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

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
 */