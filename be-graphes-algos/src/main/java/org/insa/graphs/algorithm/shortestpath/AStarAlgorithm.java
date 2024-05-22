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

public class AStarAlgorithm extends ShortestPathAlgorithm {


    public AStarAlgorithm(ShortestPathData data) {
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
            double totalDistance = listeLabel.get(destination.getId()).cost;
            if (totalDistance > 20000) {
                solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            } else {
                solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            }
        }
        else {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        return solution;

    }
}