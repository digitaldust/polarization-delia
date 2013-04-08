package org.nlogo.extensions.dypol;

/**
 *
 * @author Simone Gabbriellini
 */
public class LinkNodes {
    
    private int id;
    private Node end1;
    private Node end2;

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

    /**
     * @return the end1
     */
    public Node getEnd1() {
        return end1;
    }

    /**
     * @param end1 the end1 to set
     */
    public void setEnd1(Node end1) {
        this.end1 = end1;
    }

    /**
     * @return the end2
     */
    public Node getEnd2() {
        return end2;
    }

    /**
     * @param end2 the end2 to set
     */
    public void setEnd2(Node end2) {
        this.end2 = end2;
    }
}
