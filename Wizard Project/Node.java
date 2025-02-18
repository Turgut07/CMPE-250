import java.util.ArrayList;
import java.util.Objects;

public class Node implements Comparable<Node>{
    int x;
    int y;
    double distance;
    int nodeInfo;

    int nodeData;

    boolean revelad;

    CustomHashMap<String,Edge> edges;

    public Node(int x,int y, int nodeInfo,double distance) {
        this.x = x;
        this.y=y;
        this.nodeInfo =nodeInfo;
        this.distance=distance;
        this.edges=new CustomHashMap<>();
        this.nodeData=nodeInfo;
        this.revelad=false;
    }
    public Node(int x,int y, int nodeInfo,double distance,CustomHashMap<String,Edge> edges) {
        this.x = x;
        this.y=y;
        this.nodeInfo =nodeInfo;
        this.distance=distance;
        this.edges=new CustomHashMap<>();
        this.nodeData=nodeInfo;
        this.edges=edges;
    }

    @Override
    public int compareTo(Node o) {
        if(this.distance>o.distance)return 1;
        if(this.distance<o.distance)return -1;
        return 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}