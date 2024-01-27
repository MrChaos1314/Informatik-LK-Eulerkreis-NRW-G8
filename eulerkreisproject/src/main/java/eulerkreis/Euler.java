package eulerkreis;

public class Euler {

    Graph g = new Graph();
    
    List<Edge> path = new List<Edge>();

    public Euler() {
        create();
        Vertex current = g.getVertex("1");
        searchpath(g, current);
        read(path);
    }

    

    public void read(List<Edge> l) {
        l.toFirst();
        while (l.hasAccess()) {
            System.out.print(l.getContent().getVertices()[0].getID());
            System.out.print(l.getContent().getVertices()[1].getID() + "-");
            l.next();
        }
    }

    public List<Edge> searchpath(Graph gr, Vertex current) {
        g.setAllEdgeMarks(false);
        path.toFirst();
        while (!gr.allEdgesMarked()) {
            if (UnmarkedEdge(gr, current) != null) {
                Edge e = UnmarkedEdge(gr, current);
                path.append(e);
                e.setMark(true);
                current = replace(gr, current, e);
            } else {
                System.out.println("a");
                path.toFirst();
                while (path.hasAccess()) {
                    if (path != null) {
                        
                        current = replace(gr, current, path.getContent());
                        break;
                    } else {
                        path.append(path.getContent());
                        path.remove();
                    }
                }
                path.toFirst();
                Edge e = UnmarkedEdge(gr, current);
                path.append(e);
                e.setMark(true);
                current = replace(gr, current, e);
            }
            read(path);
        }
        return path;
    }

    /*
     * Sucht Kanten von einem Knoten, welche nicht markiert sind
     * true = Edge
     * false = null
     * */
    public Edge UnmarkedEdge(Graph gr, Vertex V) {
        List<Edge> edges = gr.getEdges(V);
        edges.toFirst();
        while (edges.hasAccess()) {
            if (!edges.getContent().isMarked()) {
                return edges.getContent();
            }
            edges.next();
        }
        return null;
    }

    /* 
     * Tauscht current durch das andere Ende einer Kante, welche UnmarkedEdge findet
     * */
    public Vertex replace(Graph gr, Vertex current, Edge e) {        
        if (e.getVertices()[1] != current) {
            return e.getVertices()[1];
        } else {
            return e.getVertices()[0];
        }
    }


    public void create() {
        g.addVertex(new Vertex("1"));
        g.addVertex(new Vertex("2"));
        g.addVertex(new Vertex("3"));
        g.addVertex(new Vertex("4"));
        g.addVertex(new Vertex("5"));

        g.addEdge(new Edge(g.getVertex("1"), g.getVertex("2"), 0));
        g.addEdge(new Edge(g.getVertex("1"), g.getVertex("3"), 0));
        g.addEdge(new Edge(g.getVertex("1"), g.getVertex("4"), 0));
        g.addEdge(new Edge(g.getVertex("2"), g.getVertex("3"), 0));
        g.addEdge(new Edge(g.getVertex("2"), g.getVertex("4"), 0));
        g.addEdge(new Edge(g.getVertex("3"), g.getVertex("4"), 0));
        g.addEdge(new Edge(g.getVertex("3"), g.getVertex("5"), 0));
        g.addEdge(new Edge(g.getVertex("4"), g.getVertex("5"), 0));
    }

    public static void main(String[] args) throws Exception {
        Euler v = new Euler();
    }

}