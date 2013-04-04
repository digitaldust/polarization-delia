package org.nlogo.extensions.dypol;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import flanagan.math.PsRandom;
import java.util.HashMap;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.Link;
import org.nlogo.api.PrimitiveManager;
/**
 *
 * @author Simone Gabbriellini
 * @version 1.0
 */
public class Dypol extends DefaultClassManager {
    
    public static HashMap<String, Integer> variables = null;
    public static UndirectedSparseGraph<Node, Link> g = null;
    public static PsRandom distribution = new PsRandom();
    /**
     *
     * @param primitiveManager
     */
    @Override
  public void load(PrimitiveManager primitiveManager) {
    primitiveManager.addPrimitive("clear-all", new ClearAll());    
    primitiveManager.addPrimitive("initialize", new CreatePopulation());    
  }
    
}
