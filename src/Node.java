import gridsim.GridResource;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.GridletList;
import gridsim.ResourceCharacteristics;
import gridsim.net.SimpleLink;
import gridsim.net.FIFOScheduler;

public class Node extends GridResource {
    private int nodeId;

    public Node(String name, int nodeId, double baudRate, double delay) throws Exception {
        super(name);
        this.nodeId = nodeId;
        
        // Crée un lien réseau pour le nœud
        SimpleLink link = new SimpleLink("link", baudRate, delay, new FIFOScheduler("scheduler"));
        
        // Définit les caractéristiques de la ressource
        int rating = 1000; // Débit de traitement du nœud
        ResourceCharacteristics resConfig = new ResourceCharacteristics(name, rating, 1000, 1000, "x86", link);
        
        // Enregistre les caractéristiques de la ressource auprès du simulateur
        super.setResourceCharacteristics(resConfig);
    }

    // Méthode pour soumettre des tâches à exécuter sur le nœud
    public void submitTasks(GridletList gridletList) {
        for (int i = 0; i < gridletList.size(); i++) {
            Gridlet gridlet = (Gridlet) gridletList.get(i);
            gridlet.setUserID(nodeId); // Affecte l'ID du nœud à la tâche
            super.gridletSubmit(gridlet);
        }
    }

    // Méthode pour démarrer l'exécution des tâches sur le nœud
    public void startExecution() {
        super.gridletSubmit(null, GridSimTags.GRIDLET_SUBMIT, nodeId);
    }
}
