import java.util.Calendar;

import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.GridletList;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.net.SimpleLink;
import gridsim.net.FIFOScheduler;

public class MatriceSimulation {
    public static void main(String[] args) {
        try {
            // Initialise GridSim
            int numUser = 1;
            double baudRate = 1000.0;
            boolean traceFlag = false;
            GridSim.init(numUser, Calendar.getInstance(), traceFlag);
            
            // Crée un lien réseau
            SimpleLink link = new SimpleLink("link", baudRate, 0.010, new FIFOScheduler("scheduler"));
            
            // Crée des caractéristiques de ressource
            int rating = 1000;
            ResourceCharacteristics resConfig = new ResourceCharacteristics("gridNode", rating, 1000, 1000, "x86", link);
            
            // Crée un nœud de calcul (machine virtuelle)
            GridResource gridResource = new GridResource("gridResource", resConfig);
            
            // Démarre la simulation de la grille
            GridSim.startGridSimulation();
            
            // Crée les tâches pour le produit matriciel
            MatriceTask matriceTask = new MatriceTask("matriceTask", 100, 100);
            // Soumet les tâches à exécuter sur le nœud de calcul
            gridResource.submitTasks(new GridletList(matriceTask));
            
            // Attend que la simulation se termine
            GridSim.waitGridSimulation(0);
            
            // Récupère les résultats
            ResultatMatrice resultat = matriceTask.getResultat();
            System.out.println("Résultat du produit matriciel:");
            System.out.println(resultat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
