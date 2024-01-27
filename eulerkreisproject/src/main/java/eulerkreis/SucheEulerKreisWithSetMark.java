package eulerkreis;

public class SucheEulerKreisWithSetMark {
    
    public static void main(String[] args) {

        SucheEulerKreisWithSetMark m = new SucheEulerKreisWithSetMark();
        m.run();
    }

    List<Vertex> allVerticesWithNeighbour = new List<Vertex>();
    Vertex currentV;

    public void run() {
        Graph g = generateGraph();
        Graph uG = generateUnconnectedGraph();
        System.out.println(isGraphConnected(uG));
        List<Vertex> allVeritces = g.getVertices(); // NAME.getVertices() NAME ändern je nach Graph 
        allVeritces.toFirst();
        currentV = allVeritces.getContent();
        System.out.println(isGraphConnectedRec(g, currentV, allVerticesWithNeighbour)); // isGraphConnectedRec(NAME, currentV, allVerticesWithNeighbour) NAME ändern je nach Graph
        System.out.println(readPath(searchCircle(g)));
    }

    public List<Edge> searchCircle(Graph g) {
        List<Vertex> firstV = g.getVertices();
        List<Vertex> path = new List<Vertex>();
        List<Edge> eList = new List<Edge>();
        firstV.toFirst();
        path.toFirst();
        Vertex currentV = firstV.getContent();
        path.append(currentV);
        while (!g.allEdgesMarked()) {
            if (isAnyEdgeUnmarked(g, currentV) == true) {
                Edge temp = giveVertexEdge(g, currentV);
                eList.append(temp);
                temp.setMark(true);
                currentV = giveAnotherVertexFromEdge(temp, currentV);
                path.append(currentV);
            } else {
                currentV = giveVisitedUnmarkedEdgeVertex(g, path);
                eList.concat(changeListOrder(eList, currentV));
                Edge tempEdge = giveVertexEdge(g, currentV);
                eList.append(tempEdge);
                tempEdge.setMark(true);
                currentV = giveAnotherVertexFromEdge(tempEdge, currentV);
                path.append(currentV);
            }
        }

        return eList;
    }

    public boolean isAnyEdgeUnmarked(Graph g, Vertex v) { // suche nach ieiner Kante, die nicht makiert ist
        List<Edge> e = new List<Edge>();
        e = g.getEdges(v);
        e.toFirst();
        while (e.hasAccess()) {
            if(!e.getContent().isMarked()){
                return true;
            }
            e.next();
        }
        return false;
    }

    public Edge giveVertexEdge(Graph g, Vertex v) { //gib eine Kante vom Knoten, die nicht makiert ist
        List<Edge> e = g.getEdges(v);
        e.toFirst();
        while (e.hasAccess()) {
            if(!e.getContent().isMarked()){
                return e.getContent();
            }
            e.next();
        }
        return null;
    }

    public Vertex giveAnotherVertexFromEdge(Edge e, Vertex v) { //gib den anderen Knoten, der mit aktuellen Knoten und der gegebenen Kante verbunden ist
        Vertex[] vS = e.getVertices();
        for (int i = 0; i <= vS.length; i++) {
            if (!vS[i].getID().equalsIgnoreCase(v.getID())) {
                return vS[i];
            }
        }
        return null;
    }

    public Vertex giveVisitedUnmarkedEdgeVertex(Graph g, List<Vertex> vList) { //gib vom gelaufenen Weg einen Knoten der noch unbesuchte Kanten hat

        vList.toFirst();
        while (vList.hasAccess()) {
            if (isAnyEdgeUnmarked(g, vList.getContent())) {
                return vList.getContent();
            }
            vList.next();
        }

        return null;
    }

    public List<Edge> changeListOrder(List<Edge> eList, Vertex v){ //setze den Rundweg neu vom besuchten Knoten welches noch unbesuchte Kanten hat 
        List<Edge> helpList = new List<Edge>();
        eList.toFirst();
        helpList.toFirst();
        while (eList.hasAccess()) { //suche nach einer Kante mit currentVertex und nehme die darauffolgende Kante
            if (eList.getContent().getVertices()[0] == v || eList.getContent().getVertices()[1] == v) {
                eList.next();
                if (eList.getContent().getVertices()[0] == v || eList.getContent().getVertices()[1] == v) {
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
        return helpList;
    }

    

    public String readPath(List<Edge> eList) { // gib die gelaufenen Kanten

        String pathString = "";
        eList.toFirst();
        while (eList.hasAccess()) {
            pathString = pathString + eList.getContent().getVertices()[0].getID();
            pathString = pathString + eList.getContent().getVertices()[1].getID();
            
            eList.next();
            if(eList.hasAccess()){
                pathString = pathString + "-";
            }
        }

        return pathString;
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

    /*
        1. Füge alle Vertices in eine Liste (allVertices) 
        2. Wähle einen Startknoten vom Graphen 
        3. Füge den Startknoten in eine Liste (allVerticesWithNeighbour) mit dessen Nachbarn
        4. Markiere den derzeitigen Punkt 
        5. solange alle Punkte in der Liste (allVerticesWithNeighbour) nicht markiert sind:
        6. Wähle einen Punkt aus der Liste (allVerticesWithNeighbour), der nicht besucht wurde als derzeitigen Punkt
        7. Füge vom derzeitigen Punkt die Nachbarn in die Liste (allVerticesWithNeighbour) hinzu, die nicht enthalten sind
        8. markiere den derzeitigen Punkt
        9. solange ende
        10. Überprüfe, ob alle markiert sind 
        11. wenn nicht:
        12. gib falsch zurück
        13. sonst 
        14. gib richtig zurück 
        
    */

    public boolean isGraphConnected(Graph g){
        List<Vertex> allVeritces = g.getVertices();
        allVeritces.toFirst();
        Vertex currentV = allVeritces.getContent();
        List<Vertex> allVerticesWithNeighbour = new List<Vertex>();
        allVerticesWithNeighbour.append(currentV);
        List<Vertex> helpList = g.getNeighbours(currentV);
        helpList.toFirst();
        while (helpList.hasAccess()) {
            allVerticesWithNeighbour.append(helpList.getContent());
            helpList.next();
        }
        currentV.setMark(true);
        while (isAnyVertexUnmarked(allVerticesWithNeighbour)) {
            currentV = giveAnyUnmarkedVertex(allVerticesWithNeighbour);
            List<Vertex> currentVNeighbourList = g.getNeighbours(currentV);
            currentVNeighbourList.toFirst();
            while (currentVNeighbourList.hasAccess()) {
                boolean isInList = false;
                allVerticesWithNeighbour.toFirst();
                while (allVerticesWithNeighbour.hasAccess()) {
                    if(allVerticesWithNeighbour.getContent() == currentVNeighbourList.getContent()){
                        isInList = true;
                        break;
                    }
                    allVerticesWithNeighbour.next();
                }
                if(isInList == false){
                    allVerticesWithNeighbour.append(currentVNeighbourList.getContent());
                }
                currentVNeighbourList.next();
            }
            currentV.setMark(true);
        }
        if(g.allVerticesMarked()){ // false: Nicht zusammenhängend - true: Zusammenhängend
            return true;
        }
        return false;
    }

    
   
    public boolean isAnyVertexUnmarked(List<Vertex> vList){
        vList.toFirst();
        while (vList.hasAccess()) {
            if(!vList.getContent().isMarked()){
                return true;
            }
            vList.next();
        }
        return false;
    }

    public Vertex giveAnyUnmarkedVertex(List<Vertex> vList){
        vList.toFirst();
        while (vList.hasAccess()) {
            if(!vList.getContent().isMarked()){
                return vList.getContent();
            }
            vList.next();
        }
        return vList.getContent();
    }


    public Graph generateUnconnectedGraph() {
        Graph g = new Graph();
        generateUnconnectedVertices(g);
        generateUnconnectedEdges(g);
        return g;
    }

    public void generateUnconnectedVertices(Graph g) {
        g.addVertex(new Vertex("A"));
        g.addVertex(new Vertex("B"));
        g.addVertex(new Vertex("C"));
        g.addVertex(new Vertex("D"));
        g.addVertex(new Vertex("E"));
        g.addVertex(new Vertex("F"));
    }

    public void generateUnconnectedEdges(Graph g) {
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("B"), 0));
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("C"), 0));
        g.addEdge(new Edge(g.getVertex("A"), g.getVertex("D"), 0));
        g.addEdge(new Edge(g.getVertex("B"), g.getVertex("C"), 0));
        g.addEdge(new Edge(g.getVertex("E"), g.getVertex("F"), 0));
    }


    public String readVertexList(List<Vertex> vList){
        String read = "";
        vList.toFirst();
        while (vList.hasAccess()) {
            read = read + vList.getContent().getID() + " ";
            vList.next();
        }
        return read;
    }


    /*------------------------------------------------------
    *   Zusammenhängender Graph Rekursiv
    *-----------------------------------------------------*/

    

    public boolean isGraphConnectedRec(Graph g, Vertex v, List<Vertex> vList){
        currentV = v; 
        allVerticesWithNeighbour = vList;
        allVerticesWithNeighbour.append(currentV);
        currentV.setMark(true);
        List<Vertex> currentVNeighbours = g.getNeighbours(currentV);
        currentVNeighbours.toFirst();
        while (currentVNeighbours.hasAccess()) {
            if(!currentVNeighbours.getContent().isMarked()){
                isGraphConnectedRec(g, currentVNeighbours.getContent(), allVerticesWithNeighbour);
            }
            currentVNeighbours.next();
        }
        if(g.allVerticesMarked()){ // false: Nicht zusammenhängend - true: Zusammenhängend
            return true;
        }
        return false;
    }
}