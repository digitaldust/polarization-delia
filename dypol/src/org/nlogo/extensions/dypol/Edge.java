package org.nlogo.extensions.dypol;

/**
 *
 * @author Simone Gabbriellini
 */

public class Edge {
    private int id;
    private Node fromid;
    private Node toid;
    /**
     * @return the fromid
     */
    public Node getFromid() {
        return fromid;
    }

    /**
     * @param fromid the fromid to set
     */
    public void setFromid(Node fromid) {
        this.fromid = fromid;
    }

    /**
     * @return the toid
     */
    public Node getToid() {
        return toid;
    }

    /**
     * @param toid the toid to set
     */
    public void setToid(Node toid) {
        this.toid = toid;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
}
