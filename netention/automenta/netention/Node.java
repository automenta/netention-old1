package automenta.netention;

import java.io.Serializable;
import java.util.Date;

/**
 * analogous to an RDF resource
 */
public class Node implements Serializable {

    private String id;
    private String name;
    private Date when;

    public Node() {
        id = null;
        name = null;
        when = null;                
    }

    public Node(String id) {
        this(id, id);
    }

    public Node(String id, String name) {
        this.id = id;
        this.name = name;
        this.when = new Date();
    }

    public void setID(String id) {
        this.id = id;
    }        

    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * universally unique ID
     */
    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    /** when created */
    public void setWhen(Date when) {
        this.when = when;
    }
       
    /** when created */
    public Date getWhen() {
        return when;
    }

    @Override
    public String toString() {
        return getID() + " (" + getName() + ")";
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node an = (Node) obj;
            return an.id.equals(id);
        }
        return false;
    }
//    public static class StringNode extends AbstractNode {
//
//        public StringNode(String id) {
//            super(id);
//        }
//        @Override public String toString() {
//            return getName();
//        }
//        
//    }
}
