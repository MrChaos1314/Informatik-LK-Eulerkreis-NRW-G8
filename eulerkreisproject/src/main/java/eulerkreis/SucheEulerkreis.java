package eulerkreis;

public class SucheEulerkreis {
    public static void main(String[] args) {

        SucheEulerkreis m = new SucheEulerkreis();
        m.run();
    }

    public void run() {
        Graph g = generateGraph();
        //System.out.println(readPath(searchCircle(g)));
    }

    public List<Edge> searchCircle(Graph g) {
        List<Vertex> firstV = g.getVertices();
        List<Vertex> path = new List<Vertex>();
        List<Edge> eList = new List<Edge>();
        List<Edge> edgeVisited = new List<Edge>();
        firstV.toFirst();
        path.toFirst();
        Vertex currentV = firstV.getContent();
        path.append(currentV);
        while (!g.allEdgesMarked()) {
            if (isAnyEdgeUnmarked(g, edgeVisited, currentV) == true) {
                Edge temp = giveVertexEdge(g, edgeVisited, currentV);
                eList.append(temp);
                //temp.setMark(true);
                edgeVisited.append(temp);
                currentV = giveAnotherVertexFromEdge(temp, currentV);
                path.append(currentV);
            } else {
                currentV = giveVisitedUnmarkedEdgeVertex(g, edgeVisited, path);

                //Edge currentEdge = giveVisitedUnmarkedEdge(g, eList, path);
                // eList = changeCurrentPath(eList, currentV); //wahrscheinlich nicht so möglich
                // mit Listen

                List<Edge> helpList = new List<Edge>();
                eList.toFirst();
                helpList.toFirst();
                while (eList.hasAccess()) {
                    if (eList.getContent().getVertices()[0] == currentV || eList.getContent().getVertices()[1] == currentV) {
                        eList.next();
                        if (eList.getContent().getVertices()[0] == currentV || eList.getContent().getVertices()[1] == currentV) {
                            while (eList.hasAccess()) {
                                helpList.append(eList.getContent());
                                eList.remove();
                            }
                            eList.toFirst();
                            while (eList.hasAccess()) {
                                helpList.append(eList.getContent());
                                eList.remove();
                            }
                        }
                    }
                    eList.next();
                }
                eList.concat(helpList);

                Edge tempEdge = giveVertexEdge(g, edgeVisited, currentV);
                eList.append(tempEdge);
                edgeVisited.append(tempEdge);
                currentV = giveAnotherVertexFromEdge(tempEdge, currentV);
                path.append(currentV);
            }
            System.out.println(readPath(eList));
            areEdgesAllMarked(g, edgeVisited);
        }

        return eList;
    }

    public boolean isAnyEdgeUnmarked(Graph g, List<Edge> eMarkedList, Vertex v) {
        List<Edge> e = new List<Edge>();
        e = g.getEdges(v);
        e.toFirst();
        eMarkedList.toFirst();
        while (e.hasAccess()) {
            while (eMarkedList.hasAccess()) {
                if (e.getContent() == eMarkedList.getContent()) {
                    eMarkedList.toFirst();
                    break;
                }
                eMarkedList.next();
                if (!eMarkedList.hasAccess()) {
                    return true;
                }
            }
            e.next();
            if (eMarkedList.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Edge giveVertexEdge(Graph g, List<Edge> eMarkedList, Vertex v) {

        List<Edge> e = g.getEdges(v);
        e.toFirst();
        eMarkedList.toFirst();
        while (e.hasAccess()) {
            while (eMarkedList.hasAccess()) {
                if (e.getContent() == eMarkedList.getContent()) {
                    eMarkedList.toFirst();
                    break;
                }
                eMarkedList.next();
                if (!eMarkedList.hasAccess()) {
                    return e.getContent();
                }
            }
            e.next();
            if (eMarkedList.isEmpty()) {
                return e.getContent();
            }
        }
        return null;
    }

    public Vertex giveAnotherVertexFromEdge(Edge e, Vertex v) {
        Vertex[] vS = e.getVertices();
        for (int i = 0; i <= vS.length; i++) {
            if (!vS[i].getID().equalsIgnoreCase(v.getID())) {
                return vS[i];
            }
        }
        return null;
    }

    public Vertex giveVisitedUnmarkedEdgeVertex(Graph g, List<Edge> eMarkedList, List<Vertex> vList) {

        vList.toFirst();
        while (vList.hasAccess()) {
            if (isAnyEdgeUnmarked(g, eMarkedList, vList.getContent())) {
                return vList.getContent();
            }
            vList.next();
        }

        return null;
    }

    public Edge giveVisitedUnmarkedEdge(Graph g, List<Edge> eMarkedList, List<Vertex> vList) {

        vList.toFirst();
        while (vList.hasAccess()) {
            if (isAnyEdgeUnmarked(g, eMarkedList, vList.getContent())) {
                return giveVertexEdge(g, eMarkedList, vList.getContent());
            }
            vList.next();
        }

        return null;
    }

    public void areEdgesAllMarked(Graph g, List<Edge> eMarked) { // Zählen ist unsicher; überprüfung, ob das die
                                                                 // Richtigen Edges sind nicht berücksichtigt!
        List<Edge> eList = g.getEdges();
        int numberEList = 0;
        int numberEMarked = 0;

        eList.toFirst();
        eMarked.toFirst();
        while (eList.hasAccess()) {
            numberEList++;
            eList.next();
        }
        while (eMarked.hasAccess()) {
            numberEMarked++;
            eMarked.next();
        }

        if (numberEList == numberEMarked) {
            g.setAllEdgeMarks(true);
        }

    }

    // public List<Edge> changeCurrentPath(List<Edge> eList, Vertex v){

    // List<Edge> helpEList = new List<Edge>();
    // eList.toFirst();
    // while (eList.hasAccess()) {
    // if(eList.getContent().getVertices()[0] == v){
    // while (eList.hasAccess()) {
    // helpEList.append(eList.getContent());
    // System.out.print(helpEList.getContent() + "n");
    // eList.remove();
    // }
    // eList.toFirst();
    // while (eList.hasAccess()) {
    // helpEList.append(eList.getContent());
    // System.out.print(helpEList.getContent() + "n");
    // eList.remove();
    // }
    // return helpEList;
    // }
    // eList.next();
    // }

    // return null;

    // }

    public String readPath(List<Edge> eList) {

        String pathString = "";
        eList.toFirst();
        while (eList.hasAccess()) {
            pathString = pathString + eList.getContent().getVertices()[0].getID();
            pathString = pathString + eList.getContent().getVertices()[1].getID();
            pathString = pathString + "-";
            eList.next();
        }

        return pathString;
    }

    public String printMarkedEdges(List<Edge> eList) {
        return readPath(eList);

    }

    public Graph generateGraph() {
        Graph g = new Graph();
        generateVertices(g);
        generateEdges(g);
        return g;
    }

    public void generateVertices(Graph g) {
        g.addVertex(new Vertex("A"));
        g.addVertex(new Vertex("B"));
        g.addVertex(new Vertex("C"));
        g.addVertex(new Vertex("D"));
        g.addVertex(new Vertex("E"));
        g.addVertex(new Vertex("F"));
    }

    public void generateEdges(Graph g) {
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("B"), 0));
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("C"), 0));
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("E"), 0));
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("F"), 0));
        g.addEdge(new Edge(g.getVertex("B"), g.getVertex("F"), 0));
        g.addEdge(new Edge(g.getVertex("B"), g.getVertex("E"), 0));
        g.addEdge(new Edge(g.getVertex("B"), g.getVertex("C"), 0));
        g.addEdge(new Edge(g.getVertex("C"), g.getVertex("E"), 0));
        g.addEdge(new Edge(g.getVertex("C"), g.getVertex("D"), 0));
        g.addEdge(new Edge(g.getVertex("D"), g.getVertex("E"), 0));
    }

    public String printAllEdges(Graph g) {
        List<Edge> eList = g.getEdges();

        return readPath(eList);
    }

}