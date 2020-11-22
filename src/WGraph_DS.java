package Ex1.src;

import java.util.*;

public class WGraph_DS implements weighted_graph,java.io.Serializable {

    private HashMap<Integer,node_info>vertices;
   private int counterEdeges;
    private int modecounter;

    public WGraph_DS() {
        vertices=new HashMap<>();
        counterEdeges=0;
        modecounter=0;
    }


    private class Nodeinfo implements node_info,java.io.Serializable {

        private int key;
        private String info;
        private double tag; //id of the parent
        private HashMap<Integer,node_info> neighbor; // the neighbors  of a specific vertex
        private HashMap<Integer,Double>edges; //the weight of the edge

        public Nodeinfo(int key) {
            this.key = key;
            this.info ="";
            this.tag =-1;
            neighbor=new HashMap<>();
            edges=new HashMap<>();
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;

        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Nodeinfo nodeinfo = (Nodeinfo) o;
            return key == nodeinfo.key &&
                    Double.compare(nodeinfo.tag, tag) == 0 &&
                    info.equals(nodeinfo.info) &&
                    neighbor.keySet().equals(nodeinfo.neighbor.keySet()) &&
                    edges.equals(nodeinfo.edges);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, info, tag, neighbor, edges);
        }
    }


    @Override
    public node_info getNode(int key) {
        return vertices.get(key);

    }

    @Override
    public boolean hasEdge(int node1, int node2) {

        Nodeinfo temp1= (Nodeinfo) getNode(node1);
        Nodeinfo temp2= (Nodeinfo) getNode(node2);
        if(temp1 ==null || temp2 == null ||node1==node2) //checking if one of the inputs are valid
            return false;


       return (temp1.neighbor.containsKey(node2)&&temp2.neighbor.containsKey(node1)); //checks if node2 contained in temp1  and if node1 contained in temp2
                                                                                      //  (contained in their neighbor hashmap)

    }

    @Override
    public double getEdge(int node1, int node2) {

                if(!hasEdge(node1,node2)) //checking if they even have an edge
            return -1;

        Nodeinfo temp1= (Nodeinfo) getNode(node1);
        return temp1.edges.get(node2); //returns the weight between node1 and node 2




    }

    @Override
    public void addNode(int key) {
        Nodeinfo temp1= (Nodeinfo) getNode(key);
        if(temp1==null) {
            vertices.put(key, new Nodeinfo(key)); //adding a vertex to the hashmap
            modecounter++;
        }


        }


    @Override
    public void connect(int node1, int node2, double w) {
        Nodeinfo temp1= (Nodeinfo) getNode(node1);
        Nodeinfo temp2= (Nodeinfo) getNode(node2);
        if(node1==node2) //checking if both nodes are the same
            return;
        if(hasEdge(node1,node2)) { //if their an edge between the nodes
            temp1.edges.put(node2, w); //updates the weight between them
            temp2.edges.put(node1,w); //updates the weight between them
        }
        else{
            temp1.neighbor.put(node2,temp2);//adding the vertex into temp1 neighbor hashmap
            temp2.neighbor.put(node1,temp1);//adding the vertex into temp2 neighbor hashmap
            temp1.edges.put(node2, w); //adding the weight between them
            temp2.edges.put(node1,w); //adding the weight between them
            counterEdeges++;
            modecounter++;


        }

    }

    @Override
    public Collection<node_info> getV() {
        return vertices.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        Nodeinfo temp1= (Nodeinfo) getNode(node_id);
        if(temp1!=null)
            return temp1.neighbor.values(); //returning a collection of the neighbors of node_id

        return null;
    }

    @Override
    public node_info removeNode(int key) {

        Nodeinfo temp1= (Nodeinfo) getNode(key);


        if(vertices.get(key)==null) // checks the input of the key
            return null;

        List<node_info> tempList=new LinkedList<>(temp1.neighbor.values()); //linked list of all the neighbors

        for(node_info n : tempList) { //going through every neighbor of temp1
            removeEdge(key, n.getKey()); //removing every edge that node has with his neighbors
        }
       modecounter++; //depends if removing the vertex considered an action aswell as removing all the edges to the vertex
        return vertices.remove(key); //removing the vertex from the vertices hashmap

    }

    @Override
    public void removeEdge(int node1, int node2) {
        Nodeinfo temp1= (Nodeinfo) getNode(node1);
        Nodeinfo temp2= (Nodeinfo) getNode(node2);
        if(hasEdge(node1,node2)){   //checks if there an edge between both nodes
            temp1.neighbor.remove(node2); //removing the node from its neighbor
            temp2.neighbor.remove(node1);//removing the node from its neighbor
            temp1.edges.remove(node2);//removing the weight between them in the edges hashmap
            temp2.edges.remove(node1);//removing the weight between them in the edges hashmap
            counterEdeges--;
            modecounter++;

        }


    }

    @Override
    public int nodeSize() {
        return vertices.size();
    }

    @Override
    public int edgeSize() {
        return counterEdeges;
    }

    @Override
    public int getMC() {
        return modecounter;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return counterEdeges == wGraph_ds.counterEdeges &&
                modecounter == wGraph_ds.modecounter &&
                vertices.equals(wGraph_ds.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, counterEdeges, modecounter);
    }

}
