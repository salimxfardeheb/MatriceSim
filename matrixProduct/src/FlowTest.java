import gridsim.*;
import gridsim.net.*;
import gridsim.net.flow.*;
import gridsim.util.SimReport;
import eduni.simjava.*;
import org.javatuples.*;

public class FlowTest extends GridSim {
    private int myID_;          // my entity ID
    private String name_;       // my entity name
    private String destName_;   // destination name
    private int destID_;        // destination id
    private SimReport report_;  // logs every activity
    private int[][] result;

    public FlowTest(String name, String destName, Link link, int[][] result) throws Exception {
        super(name, link);

        this.name_ = super.get_name();
        this.myID_ = super.get_id();
        this.destName_ = destName;
        this.result = result;

        report_ = new SimReport(name);
        report_.write("Creates " + name);
    }

    public FlowTest(String name, String destName, int[][] result) throws Exception {
        super(name, new FlowLink(name+"_link",10485760,250,Integer.MAX_VALUE));

        this.name_ = super.get_name();
        this.myID_ = super.get_id();
        this.destName_ = destName;
        this.result = result;
  
        report_ = new SimReport(name);
        report_.write("Creates " + name);
    }

    public void body() {
        if (result == null) {
            System.out.println("La matrice result n'a pas été initialisée correctement.");
            return;
        }
        this.destID_ = GridSim.getEntityId(destName_);
        int packetSize = 1500;
        Sim_event ev = new Sim_event();
        
        while (Sim_system.running()) {
            super.sim_get_next(ev);
            
            if (ev.get_tag() == GridSimTags.END_OF_SIMULATION) {

                System.out.println();
                write(super.get_name() + ".body(): exiting ...");
                break;

            }else if (ev.get_tag() == GridSimTags.FLOW_SUBMIT) { 

                Triplet<Integer, Integer, Integer> msgrecu = (Triplet<Integer, Integer, Integer>) ev.get_data();
                int val = (int) msgrecu.getValue(0);
                int indice_ligne = (int) msgrecu.getValue(1);
                int indice_colonne = (int) msgrecu.getValue(2);

                if (indice_ligne >= 0 && indice_ligne < result.length && indice_colonne >= 0 && indice_colonne < result[0].length) {
                    result[indice_ligne][indice_colonne] = val;
                }
                System.out.println();
                write(super.get_name() + ".body(): receive " + msgrecu.getValue0() + ", in ("+msgrecu.getValue1()+","+msgrecu.getValue2()+
                        "), at time = " + GridSim.clock());

                IO_data data = new IO_data(ev.get_data(), packetSize, destID_);
                super.send(super.output, GridSimTags.SCHEDULE_NOW,
                           GridSimTags.FLOW_ACK, data);
            }
            
        }
        shutdownUserEntity();
        terminateIOEntities();

        if (report_ != null) {
            report_.finalWrite();
        }
        
        System.out.println(this.name_ + ":%%%% Exiting body() at time " +
                           GridSim.clock());
    }
    
    private void write(String msg) {
        System.out.println(msg);
        if (report_ != null) {
            report_.write(msg);
        }        
    }
}
