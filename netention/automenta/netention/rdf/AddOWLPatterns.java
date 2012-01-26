package automenta.netention.rdf;

import java.io.File;
import java.net.MalformedURLException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.Self;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.integer.IntProp;
import automenta.netention.value.node.NodeProp;
import automenta.netention.value.real.RealProp;
import automenta.netention.value.string.StringProp;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;


/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their respective
 * authors, or their employers as appropriate. Authorship of the modifications
 * may be determined from the ChangeLog placed at the end of this file.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
/**
 * <p>Simple example. Read an ontology, and display the class hierarchy. May use
 * a reasoner to calculate the hierarchy.</p>
 *
 * Author: Sean Bechhofer<br> The University Of Manchester<br> Information
 * Management Group<br> Date: 17-03-2007<br> <br>
 */
public class AddOWLPatterns {

    //private static int INDENT = 4;
    private OWLReasonerFactory reasonerFactory;
    private OWLOntology ontology;
    private final Self self;

    public AddOWLPatterns(Self self, OWLOntologyManager manager, OWLReasonerFactory reasonerFactory)
            throws OWLException, MalformedURLException {
        this.self = self;
        this.reasonerFactory = reasonerFactory;
    }

    public static Set<String> getTypeSet(Set<? extends OWLClassExpression> classes) {
        Set<String> ss = new HashSet();
        for (OWLClassExpression ec : classes) {
            ss.add(ec.asOWLClass().getIRI().toString());
        }
        return Collections.unmodifiableSet(ss);
    }

    /**
     * Print the class hierarchy for the given ontology from this class down, assuming this class is at
     * the given level. Makes no attempt to deal sensibly with multiple
     * inheritance.
     */
    public void run(OWLOntology ontology, OWLClass clazz) throws OWLException {
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
        this.ontology = ontology;

        
        //System.out.println("PRINT DATATYPES");
        
        for (OWLDataPropertyExpression dpe : reasoner.getSubDataProperties(reasoner.getTopDataPropertyNode().getRepresentativeElement(), true).getFlattened()) {
            if (dpe.isAnonymous())
                continue;
            
            final OWLDataProperty dp = dpe.asOWLDataProperty();
            final String iri = dp.getIRI().toString();
            
            //System.out.println(iri + " " +  dp.getDomains(ontology) + " " + dp.getRanges(ontology));
            //System.out.println("  " + dp.getDatatypesInSignature());
            
            Property p = self.getProperty(iri);
            if (p == null) {
                Set<OWLDataRange> ranges = dp.getRanges(ontology);
                for (OWLDataRange o : ranges) {
                    String t = o.toString();
                    if (t.equals("xsd:integer") || t.equals("xsd:int") || t.equals("xsd:short") || t.equals("xsd:long")) {
                        p = new IntProp(iri, iri);
                    }
                    else if (t.equals("xsd:float") || t.equals("xsd:double")) {
                        p = new RealProp(iri, iri);
                    }
                    else if (t.equals("xsd:string")) {
                        p = new StringProp(iri, iri);
                    }
                    //TODO at time, etc... http://books.xmlschemata.org/relaxng/relax-CHP-19.html
                    if (p!=null)
                        break;
                }

                if (p == null)
                    p = new StringProp(iri, iri);
                 
                self.addProperty(p, getTypeSet(reasoner.getDataPropertyDomains(dp, true).getFlattened()));

                //TODO fold with bottom annotation iteration into a common method
                for (final OWLAnnotation a : dp.getAnnotations(ontology)) {
                    final String airi = a.getProperty().getIRI().toString();
                    if (airi.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
                        p.setDescription( a.getValue().toString() );
                    }
                    else if (airi.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                        p.setName( a.getValue().toString() );
                    }
                }
                
            }
        }
        
        //System.out.println(ontology.getDatatypesInSignature());

        //System.out.println("PRINT OBJECTTYPES");                
        //for (OWLObjectProperty op : ontology.getObjectPropertiesInSignature()) {
        for (OWLObjectPropertyExpression ope : reasoner.getSubObjectProperties(reasoner.getTopObjectPropertyNode().getRepresentativeElement(), true).getFlattened()) {
            if (ope.isAnonymous())
                continue;
            
            OWLObjectProperty op = ope.asOWLObjectProperty();
            
            final String iri = op.getIRI().toString();
            //Set<OWLClassExpression> domains = op.getDomains(ontology);
            Set<OWLClass> domains = reasoner.getObjectPropertyDomains(ope, true).getFlattened();
            Set<OWLClass> ranges = reasoner.getObjectPropertyRanges(ope, true).getFlattened();
                    
            //System.out.println("  " + iri + " " +  op.getDomains(ontology) + " " + op.getRanges(ontology));
            Property p = self.getProperty(iri);
            if (p == null) {
                p = new NodeProp(iri, op.getIRI().getFragment().toString(), getTypeSet(ranges));
                self.addProperty(p, getTypeSet(domains));
                
                for (final OWLAnnotation a : op.getAnnotations(ontology)) {
                    final String airi = a.getProperty().getIRI().toString();
                    if (airi.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
                        p.setDescription( a.getValue().toString() );
                    }
                    else if (airi.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                        p.setName( a.getValue().toString() );
                    }
                }

                //self.save(p);
            }
        }
        
        //System.out.println("PRINT CLASSES");
        run(reasoner, clazz, 0 );
        
        for (OWLClass cl: ontology.getClassesInSignature()) {
            if (!reasoner.isSatisfiable(cl)) {
                System.err.println(this + ": unsatisfied: " + cl.getIRI());
            }
        }
        
        //self.print();
        
        
        reasoner.dispose();
    }
    
    /**
     * Print the class hierarchy from this class down, assuming this class is at
     * the given level. Makes no attempt to deal sensibly with multiple
     * inheritance.
     */
    public void run(OWLReasoner reasoner, OWLClass clazz, int level)
            throws OWLException {
        /*
         * Only print satisfiable classes -- otherwise we end up with bottom
         * everywhere
         */
        if (clazz.isAnonymous()) {
            return;
        }

        if (reasoner.isSatisfiable(clazz)) {

//            for (int i = 0; i < level * INDENT; i++) {
//                out.print(" ");
//            }
//            System.out.println(clazz.getIRI() + " " + clazz.getSuperClasses(ontology) + " " + reasoner.getSuperClasses(clazz, true));



            Pattern t = self.getPattern(clazz.getIRI().toString());
            if (t == null) {
                t = new Pattern(clazz.getIRI().toString());
                t.setName( clazz.getIRI().getFragment() );
                self.addPattern(t);
            }

            try {

                for (final OWLAnnotation a : clazz.getAnnotations(ontology)) {
                    final String airi = a.getProperty().getIRI().toString();
                    if (airi.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
                        t.setDescription(a.getValue().toString());
                    } else if (airi.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                        t.setName(a.getValue().toString());
                    }
                }

                NodeSet<OWLClass> x = reasoner.getSuperClasses(clazz, true);

                for (OWLClass p : x.getFlattened()) {
                    if (p.isAnonymous()) {
                        continue;
                    }
                    t.addParent(p.asOWLClass().getIRI().toString());
                }

                //self.save(t);
            } catch (Exception ex) {
                ex.printStackTrace();
            }



            //out.println(labelFor( clazz ));
            /*
             * Find the children and recurse
             */
            for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
                if (!child.equals(clazz)) {
                    run(reasoner, child, level + 1);
                }
            }
        }
    }

    public static void add(String owlPath, Self self) {
        try {

            String reasonerFactoryClassName = "StructuralReasonerFactory";

            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();


            // Now load the ontology.
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlPath));

            // Report information about the ontology
            //System.out.println("Ontology Loaded...");
            //System.out.println("Document IRI: " + documentIRI);
            //System.out.println("Ontology : " + ontology.getOntologyID());
            //System.out.println("Format      : " + manager.getOntologyFormat(ontology));

            // / Create a new SimpleHierarchy object with the given reasoner.
            AddOWLPatterns i = new AddOWLPatterns(self, manager, new StructuralReasonerFactory());

            OWLClass clazz = manager.getOWLDataFactory().getOWLClass(OWLRDFVocabulary.OWL_THING.getIRI());

            i.run(ontology, clazz);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {

        //String owlPath = "schema/foaf.owl";
        //String owlPath = "schema/SUMO.owl";
        //String owlPath = "schema/doap.owl";
        //String owlPath = "schema/cv.owl";
        String owlPath = "schema/biography.owl";
        add(owlPath, new MemorySelf());

    }
}
