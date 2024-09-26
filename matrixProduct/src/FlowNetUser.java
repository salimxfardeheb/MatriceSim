import gridsim.*;
import gridsim.net.*;
import gridsim.net.flow.*;
import org.javatuples.*;

public class FlowNetUser extends GridSim
{
    private String name_;
    private String destName_;
    private int destID_;
    private double wait_;	
    public int start ;
    public int end ;

    public static final int SEND_MSG = 1;    
    public static final int ACK_MSG = 2;
    public static final int packetSize = 524288000; 

    public FlowNetUser(String name, String destName, Link link, double wait,int start, int end) throws Exception
    {
        super(name, link);
        this.name_ = super.get_name();
        this.destName_ = destName;
        this.wait_ = wait;
        this.start =start;
        this.end =end;
    }
    
    public FlowNetUser(String name, String destName, double wait, int start,int end) throws Exception
    {
        super(name, new FlowLink(name+"_link",10485760,450,Integer.MAX_VALUE));
        this.name_ = super.get_name();
        destName_ = destName;
        this.wait_ = wait;
        this.start =start;
        this.end =end;

    }

    Main main = new Main();
     int[][]A=main.A;
     int[][]B=main.B;

    public void body()
    {
        int i = 0;
        this.destID_ = GridSim.getEntityId(destName_);
        this.gridSimHold(this.wait_);

        for (i = start; i <= end; i++)
            productMatrice(A,B,i);

        this.gridSimHold(1000.0);

        super.send(destID_, GridSimTags.SCHEDULE_NOW,GridSimTags.END_OF_SIMULATION);

        shutdownUserEntity();
        terminateIOEntities();

        System.out.println(this.name_ + ":%%%% Exiting body() at time " +GridSim.clock() );
    }

    public void productMatrice(int[][] mat1, int[][] mat2, int count) {
        if (mat1.length == 0 || mat1[0].length == 0 || mat2.length == 0 || mat2[0].length == 0) {
            System.out.println("Les matrices ne peuvent pas être vides.");
            return;
        }
        if (count < 0 || count >= mat2[0].length) {
            System.out.println("L'indice 'count' est hors limites pour la matrice mat2.");
            return;
        }
        for (int i = 0; i < mat1.length; i++) {
            int val = 0;
            for (int k = 0; k < mat1[i].length; k++) {
                if (k < mat2.length) {
                    val += mat1[i][k] * mat2[k][count];
                } else {
                    System.out.println("Indice k hors limites pour la matrice mat2.");
                    return;
                }
            }
            
            Triplet<Integer, Integer, Integer> msg = new Triplet<>(val, i, count);
            IO_data data = new IO_data(msg, packetSize, destID_);
            System.out.println(name_ + ".body(): Sending  " + msg.getValue0() + ", in ("+msg.getValue1()+","+msg.getValue2()+ 
                    "), at time = " + GridSim.clock());
    
            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    GridSimTags.FLOW_SUBMIT, data);

            super.sim_pause(10.0);
            Object obj = null;
                obj = super.receiveEventObject();
        }
    }
} // end class

