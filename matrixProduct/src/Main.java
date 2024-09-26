import gridsim.*;
import gridsim.net.*;
import gridsim.net.flow.*;
import java.util.*;

public class Main {

    int num_user = 3; 
    ArrayList userList = new ArrayList(num_user);

    static int[][] A,B,result;
    static int n,m,p;

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        // la taille des matrices
        System.out.print("Enter the number of rows (Matrix A) : ");
        n = scanner.nextInt();
        System.out.print("Enter the number of columns (Matrix B & Matrix A): ");
        m = scanner.nextInt();
        System.out.print("Enter the number of cols (matrix B): ");
        p = scanner.nextInt();

        A = new int[n][m];
        B = new int[m][p];
        result = new int[n][p];

        System.out.println("filling the matrices : ");
        System.out.println("Matrix A:");
        fillMatrice(A,n,m);
        System.out.println("matrix B :");
        fillMatrice(B, m, p);

        Main main = new Main();
        System.out.println("Starting network example ...");

        try {
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;

            System.out.println("Initializing GridSim package");
            
            GridSim.initNetworkType(GridSimTags.NET_FLOW_LEVEL);
            GridSim.init(main.num_user, calendar, trace_flag);
            
            Router r1 = new FlowRouter("router1", trace_flag);   // router 1
            Router r2 = new FlowRouter("router2", trace_flag);   // router 2
            
            String sender1 = "user1";
            String sender2 = "user2";
            String receipient1 = "test1";
            String receipient2 = "test2";
            
            FlowNetUser user1 = new FlowNetUser(sender1, receipient1, 5.0,0,0);
            FlowNetUser user2 = new FlowNetUser(sender2, receipient2, 20.0,0,0);

            main.userList.add(user1);
            main.userList.add(user2);
            
            FlowTest test1 = new FlowTest(receipient1,sender1 ,result);
            FlowTest test2 = new FlowTest(receipient2,sender2 ,result);

            FIFOScheduler userSched1 = new FIFOScheduler("NetUserSched_0");
            r1.attachHost(user1, userSched1);
            
            FIFOScheduler userSched2 = new FIFOScheduler("NetUserSched_1");
            r1.attachHost(user2, userSched2);
            
            FIFOScheduler testSched1 = new FIFOScheduler("FlowTestSched_0");
            r2.attachHost(test1, testSched1);
             
            FIFOScheduler testSched2 = new FIFOScheduler("FlowTestSched_1");
            r2.attachHost(test2, testSched2);

            double baud_rate = 1572864; // bits/sec (baud) [1.5Mb/s]
            double propDelay = 300;   // propagation delay in millisecond
            int mtu = Integer.MAX_VALUE; // max. transmission unit in byte
            
            Link link = new FlowLink("r1_r2_link", baud_rate, propDelay, mtu);
            FIFOScheduler r1Sched = new FIFOScheduler("r1_Sched");
            FIFOScheduler r2Sched = new FIFOScheduler("r2_Sched");
            
            r1.attachRouter(r2, link, r1Sched, r2Sched);
            
            main.Distributiontaches();
            GridSim.startGridSimulation();
            main.printMatrice();

            System.out.println("\nFinish network example ...");
        } catch (Exception e) {

            e.printStackTrace();
            System.err.print(e.toString());
            System.out.println("Unwanted errors happen");
        }
    }

    void Distributiontaches() {
        int numUsers = userList.size();
        System.out.println(numUsers);
        int rowsPerUser = p / numUsers;
        int remainingRows = p % numUsers;
        int startRow = 0;
        for (int i = 0; i < numUsers ; i++) {
            int endRow = startRow + rowsPerUser;
            if (remainingRows > 0) {
                endRow++;
                remainingRows--;
            }
            
                FlowNetUser user = (FlowNetUser) userList.get(i);
                user.start=startRow;
                user.end=endRow;
                startRow = endRow;
        }
    }

    static void fillMatrice(int[][] mat, int n, int m){
        
        Scanner scanner = new Scanner(System.in);
        Random r= new Random();
        for(int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++){
                if(n > 2 && m>2 && p>2){
                    mat[i][j] = r.nextInt(10);
                }else {
                    System.out.print("["+i+"]"+"["+j+"] = ");
                    int  temp = scanner.nextInt();
                    mat[i][j] = temp ;
                }
            }
        }
    }

    void printMatrice() {
        System.out.println("La matrice est : ");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++)
                System.out.print(result[i][j] + " ");
            System.out.println();
        }
    }
} // end class