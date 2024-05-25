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

    public ArrayList<Label> Initialisation(Graph graph, Node destination) {
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        for (Node node : graph.getNodes()) { 
            listeLabel.add(new Label(node));
        }
        return listeLabel;
    }
 
    @Override
    protected ShortestPathSolution doRun() { 
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        ArrayList<Node> tas = new ArrayList<Node>();
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        Graph graph = data.getGraph();
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        listeLabel = Initialisation(graph, destination);
        //graph.getNodes().forEach(node -> listeLabel.add(new Label(node)));
        for(int i = 0; i < listeLabel.size(); i++) {
            if( i != listeLabel.get(i).sommet_courant.getId())
                System.out.println(listeLabel.get(i).sommet_courant.getId() + " " + i);
        }   
    


        Arc[] predecessorArcs = new Arc[graph.size()];
        Arrays.fill(predecessorArcs, null);

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        tas.add(origin);
        listeLabel.get(origin.getId()).cost = 0;
        heap.insert(listeLabel.get(origin.getId()));

        while (!heap.isEmpty() && !listeLabel.get(destination.getId()).isMarque()){

            Label x = heap.deleteMin();
            x.setMarque(true);
            //notifyNodeMarked(x.sommet_courant);
            for (Arc arc : x.sommet_courant.getSuccessors()) {
                Node y = arc.getDestination();
                if (!data.isAllowed(arc)) {
                    continue;
                }
                if (!listeLabel.get(y.getId()).isMarque()) {
                    double oldDistance = listeLabel.get(y.getId()).getTotalCost();
                    double newDistance = listeLabel.get(x.sommet_courant.getId()).getTotalCost() + data.getCost(arc);
                    if (newDistance < oldDistance) {
                        if(oldDistance != Double.POSITIVE_INFINITY) {
                            heap.remove(listeLabel.get(y.getId()));
                        }
                        
                        listeLabel.get(y.getId()).setCost(newDistance);
                        //notifyNodeReached(y);
                        heap.insert(listeLabel.get(y.getId()));
                        listeLabel.get(y.getId()).father = x.sommet_courant;
                        predecessorArcs[y.getId()] = arc;
                    }
                }
            } 
          
        }

        if (listeLabel.get(destination.getId()).cost != Double.POSITIVE_INFINITY) {
            ArrayList<Arc> arcs = new ArrayList<>();
            Node currentNode = destination;
            while (!currentNode.equals(origin)) {
                Arc arc = predecessorArcs[currentNode.getId()];
                arcs.add(arc);
                currentNode = arc.getOrigin();
            }
            Collections.reverse(arcs);
            Path path = new Path(graph, arcs);
            solution = new ShortestPathSolution(data, Status.OPTIMAL, path);
        } else {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE, null);
        }

        return solution;
    }

}
