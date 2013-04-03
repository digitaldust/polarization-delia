package org.nlogo.extensions.dypol;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.HashMap;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.Link;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.api.Turtle;
/**
 *
 * @author Simone Gabbriellini
 * @version 1.0
 */
public class Dypol extends DefaultClassManager {
    
    public static HashMap<String, Integer> variables = null;
    public static UndirectedSparseGraph<Turtle, Link> g = null;
    /**
     *
     * @param primitiveManager
     */
    @Override
  public void load(PrimitiveManager primitiveManager) {
    primitiveManager.addPrimitive("initialize", new CreatePopulation());    
  }
    
}
