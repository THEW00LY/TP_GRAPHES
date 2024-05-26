
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

    public void Initialisation(Graph graph, ShortestPathData data, ArrayList<Label> listeLabel) {
        for (Node node : graph.getNodes()) {
            listeLabel.add(new Label(node));
        }
    }
    
    long startTime = System.nanoTime();
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        ArrayList<Node> ListeNode = new ArrayList<Node>();
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        Graph graph = data.getGraph();
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
    Initialisation(graph, data, listeLabel);
        //graph.getNodes().forEach(node -> listeLabel.add(new Label(node)));
        for(int i = 0; i < listeLabel.size(); i++) {
            if( i != listeLabel.get(i).sommet_courant.getId())
                System.out.println(listeLabel.get(i).sommet_courant.getId() + " " + i);
        }
    
        Arc[] predecessorArcs = new Arc[graph.size()];
        Arrays.fill(predecessorArcs, null);

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        ListeNode.add(origin);
        listeLabel.get(origin.getId()).cost = 0;
        heap.insert(listeLabel.get(origin.getId()));

        while (!heap.isEmpty() && !listeLabel.get(destination.getId()).isMarque()){

            Label x = heap.deleteMin(); // On retire le sommet de la liste
            x.setMarque(true); // On le marque
            notifyNodeMarked(x.sommet_courant);
            for (Arc arc : x.sommet_courant.getSuccessors()) { // On parcourt les successeurs
                Node y = arc.getDestination();
                if (!data.isAllowed(arc)) { // On vérifie si l'arc est dispo
                    continue;
                }
                if (!listeLabel.get(y.getId()).isMarque()) { // Si le sommet n'est pas marqué
                    double oldDistance = listeLabel.get(y.getId()).getTotalCost(); // On récupère la distance actuelle
                    double newDistance = listeLabel.get(x.sommet_courant.getId()).getCost() + data.getCost(arc); // On calcule la nouvelle distance
                    double Comparedistance = listeLabel.get(y.getId()).getTotalCost() - listeLabel.get(y.getId()).getCost();
                    if (newDistance < oldDistance && Comparedistance == 0) { // Si la nouvelle distance est plus petite
                        if(oldDistance != Double.POSITIVE_INFINITY) { // Si la distance est infinie, on ne l'enlève pas du tas
                            heap.remove(listeLabel.get(y.getId())); 
                        }
                        
                        listeLabel.get(y.getId()).setCost(newDistance); // On met à jour la distance
                        notifyNodeReached(y);
                        heap.insert(listeLabel.get(y.getId())); // On ajoute le sommet au tas
                        listeLabel.get(y.getId()).father = x.sommet_courant; // On met à jour le père
                        predecessorArcs[y.getId()] = arc; // On met à jour l'arc
                    } else if (newDistance + y.getPoint().distanceTo(data.getDestination().getPoint()) < oldDistance) { // Si la nouvelle distance est plus petite
                        if(oldDistance != Double.POSITIVE_INFINITY) { // Si la distance est infinie, on ne l'enlève pas du tas
                            heap.remove(listeLabel.get(y.getId()));
                        }
                        
                        listeLabel.get(y.getId()).setCost(newDistance); // On met à jour la distance
                        notifyNodeReached(y);
                        heap.insert(listeLabel.get(y.getId())); // On ajoute le sommet au tas
                        listeLabel.get(y.getId()).father = x.sommet_courant; // On met à jour le père
                        predecessorArcs[y.getId()] = arc; // On met à jour l'arc{

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
        long endTime = System.nanoTime();
        double time = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("Time taken: " + time + " s");
        return solution;
    }
}