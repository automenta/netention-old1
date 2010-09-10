package automenta.netention;

import java.io.Serializable;

/** analogous to an RDF resource */
public interface Node extends Serializable {

    /** URI, or UUID */
    public String getID();

    public String getName();
    
    public static abstract class AbstractNode implements Node {

        private String id;
        private String name;


        public AbstractNode(String id) {
            this(id, id);
        }

        public AbstractNode(String id, String name) {
            super();

            this.id = id;
            this.name = name;
        }

        /** universally unique ID */
        public String getID() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String nextName) {
            this.name = nextName;
        }

        @Override public String toString() {
            return getID() + " (" + getName() + ")";
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AbstractNode) {
                AbstractNode an = (AbstractNode)obj;
                return an.id.equals(id);
            }
            return false;
        }

    }
    
    public static class StringNode extends AbstractNode {

        public StringNode(String id) {
            super(id);
        }
        @Override public String toString() {
            return getName();
        }
        
    }

}
