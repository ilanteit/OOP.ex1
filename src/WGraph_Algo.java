package Ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph wg;
    private HashMap<Integer, Boolean> visited;  //hashmap of the vertex who were visited - its for the Dijkstra algorithm
    private HashMap<Integer, Double> distance; // hashmap of the distance each vertex has from source

    public WGraph_Algo() {
        wg = new WGraph_DS();
        visited = new HashMap<>();
        distance = new HashMap<>();
    }

    @Override
    public void init(weighted_graph g) { // resetting the values of the graph as the constructor so you can use a new graph and not a used one.
        wg = g;
        visited = new HashMap<>();
        distance = new HashMap<>();
        for (node_info n : g.getV()) {
            visited.put(n.getKey(), false);   //updating the hashmap to be filled with false.
            n.setTag(-1);
            distance.put(n.getKey(), Double.MAX_VALUE);
        }

    }

    @Override
    public weighted_graph getGraph() {
        return wg;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph newG = new WGraph_DS();
        LinkedList<node_info> newvetrices = new LinkedList<>(wg.getV()); //list of the vertices in wg
        Queue<node_info> que = new LinkedList<>();
        if (newvetrices.isEmpty()) // copies an empty graph
            return newG;


        while (!newvetrices.isEmpty()) {
            que.add(newvetrices.getFirst());
            newG.addNode(newvetrices.getFirst().getKey());
            newG.getNode(newvetrices.getFirst().getKey()).setInfo(newvetrices.getFirst().getInfo());  //rebooting the first node

            while (!que.isEmpty()) {
                node_info temp = que.poll();
                for (node_info n : wg.getV(temp.getKey())) { //copying each node to the new graph
                    if (visited.get(n.getKey()) == false) {
                        newG.addNode(n.getKey());
                        newG.getNode(n.getKey()).setInfo(n.getInfo());
                        que.add(n);  //adding to the queue
                        newvetrices.remove(n); //removing and checking the vertex so we wont copy it multiple times
                    }
                    if (!newG.hasEdge(temp.getKey(), n.getKey())) {
                        newG.connect(temp.getKey(), n.getKey(), wg.getEdge(temp.getKey(), n.getKey()));  //connecting the nodes in the new graph

                    }


                }
                visited.put(temp.getKey(), true); // visited the vertex.
                newvetrices.remove(temp); //removing the parent


            }

        }
        return newG;
    }

private void Dijkstra(weighted_graph g, int src){

        double totaldist=0;
        distance.put(src, (double) 0); //distance from source to source
    PriorityQueue<Integer> que = new PriorityQueue<>((o1, o2) -> (int) (distance.get(o1)-distance.get(o2))); //organizing the queue accroding to the smallest distance


        que.add(src);
        while (!que.isEmpty()){

            int temp = que.poll();  //the key of the node with the smallest distance
            node_info temp2= wg.getNode(temp); ////node with the smallest distance
            for (node_info n : g.getV(temp2.getKey())) {
                if (visited.get(n.getKey()) == false){
                    totaldist=distance.get(temp2.getKey()) +g.getEdge(temp2.getKey(),n.getKey());
                    if(totaldist<distance.get(n.getKey()))
                    {
                        distance.put(n.getKey(),totaldist);    //updtaing the distance between src and the node if the distance is shorter than before
                        g.getNode(n.getKey()).setTag(temp2.getKey()); //updating the parent of the node
                        que.remove(n.getKey());// removing the "not" updated node and adding the updated node in the next line
                        que.add(n.getKey());  //adding to the queue
                    }



                }
            }
            visited.put(temp2.getKey(), true); // visited the vertex.



        }



}

    @Override
    public boolean isConnected() {

        init(wg); // rebooting the graph and visited(hashmap) so there wont be any duplecates
        LinkedList<node_info> vertices = new LinkedList<>(wg.getV()); //list of all the vertices
        if(vertices.isEmpty())
            return true;

        Dijkstra(wg,vertices.getFirst().getKey()); // using Dijkstra algorithm to fill up the "visited" hashmap so we can know is the graph is connected or not
       return !visited.containsValue(false);


    }

    @Override
    public double shortestPathDist(int src, int dest) {

        List<node_info>shortest=shortestPath(src,dest);
        if(shortest==null)
            return -1;

        return distance.get(dest);

    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {

        List<node_info>shortest=new LinkedList<>(); //list of nodes of the shortest path if exist
        Stack<node_info> shortstack=new Stack<node_info>(); //stack of nodes of the shortest path if exist
        if(wg.getNode(src)==null||wg.getNode(dest)==null)// checking if the input is valid
            return null;

        init(wg);
        shortest.add(wg.getNode(src));
        Dijkstra(wg,src);//using Dijkstra algorithm to fill up the the "distance" hashmap and the tags of each node which are the parents.
        if(wg.getNode(dest).getTag()==-1) //if the father of the des is -1 it means the Dijkstra algorithm didnt reach it from src and didnt update the parent of dest
            return null;

            node_info temp=wg.getNode(dest);
            while(temp!=wg.getNode(src)){   //until i reach the src
                shortstack.push(temp); // filling up a stack is more convenient because we are filling up the stack from last to first .
                temp=wg.getNode((int)(temp.getTag())); // updating the temp to be the father of the temp
            }

            for(int i=0;i<shortstack.size();i++)
            {
                shortest.add(shortstack.pop()); //popping all the nodes from first to last into a linked list.

            }
            return shortest;



    }

    @Override
    public boolean save(String file) {
        System.out.println("starting to Serialize to " + file + "\n");
        FileOutputStream fileOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try {
             fileOutputStream = new FileOutputStream(file);
             objectOutputStream = new ObjectOutputStream((fileOutputStream));
            objectOutputStream.writeObject((this.wg));


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {

            try {
                objectOutputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(("end of Serialize \n\n"));
        return true;

    }

    @Override
    public boolean load(String file) {
        System.out.println("Deserialize from : " + file + "\n");
        FileInputStream fileInputStream=null;
        ObjectInputStream objectInputStream =null;
        try {
          fileInputStream = new FileInputStream(file);
             objectInputStream = new ObjectInputStream((fileInputStream));
            WGraph_DS deserialzedGraph = (WGraph_DS) objectInputStream.readObject();
            wg = deserialzedGraph;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
