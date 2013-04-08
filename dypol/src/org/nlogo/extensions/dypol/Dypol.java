package org.nlogo.extensions.dypol;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import flanagan.math.PsRandom;
import java.util.HashMap;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.PrimitiveManager;

/**
 * This class is the extension class manager and it provides the new primitives
 * that can be called in NetLogo.
 *
 * @author Simone Gabbriellini
 * @version 1.0
 */
public class Dypol extends DefaultClassManager {

    public static HashMap<String, Integer> variables;
    public static UndirectedSparseGraph<Node, LinkNodes> g;
    public static PsRandom generator;
    public static double infl;
    public static int issues;
    public static int seed;
    public static int size;
    public static int linkId;
    public static int interactions;
    public static int[] mostPopularIssue;
    public static boolean saveToFile;
    public static int memory;

    /**
     *
     * @param primitiveManager
     */
    @Override
    public void load(PrimitiveManager primitiveManager) {
        primitiveManager.addPrimitive("clear-all", new ClearAll());
        primitiveManager.addPrimitive("initialize-agent", new Initialize());
        primitiveManager.addPrimitive("initialize-distances", new InitializeDistances());
        primitiveManager.addPrimitive("influence", new Influence());
        primitiveManager.addPrimitive("stats", new Stats());
    }
}
