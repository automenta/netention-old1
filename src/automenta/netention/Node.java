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

        public AbstractNode() {
            super();
        }

        public AbstractNode(String id) {
            this();
            this.id = id;
            this.name = id;
        }

        public AbstractNode(String id, String name) {
            this(id);

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
    }

}
