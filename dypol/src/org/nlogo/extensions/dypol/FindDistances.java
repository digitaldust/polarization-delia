/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlogo.extensions.dypol;

import java.util.Collection;
import org.nlogo.agent.World;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

/**
 *
 * @author Simone Gabbriellini
 */
public class FindDistances extends DefaultCommand {
    
    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        
        // retrieve world
        World world = (World) cntxt.getAgent().world();
        // retrieve ticks now
        Double ticks = world.ticks();
        // max euclidean distance
        double maxEuclideanDistance = 0;
        // retrieve all the turtles in the network
        Collection<Node> turtles = Dypol.g.getVertices();
        // ask each turtles...
        for(Node from:turtles){
            // new array to hold euclidean distances
            Double[] euclDistances = new Double[turtles.size()];
            // ...to ask all other turtles
            for(Node to:turtles){
                // array of from's interest in issues
                Double[] a = from.getIssuesInterest(ticks);
                // array of to's interest in issues 
                Double[] b = to.getIssuesInterest(ticks);
                // array to hold squared differences between the two
                double squaredDifferences = 0;
                // cicle through the array
                for(int i=0;i<a.length;i++){
                    // find squared differences between from and to's values
                    squaredDifferences += Math.pow(a[i] - b[i], 2.0);
                }
                // find squared value 
                double sqrt = Math.sqrt(squaredDifferences);
                // save value in euclidean distances array
                euclDistances[(int)to.getWho()] = sqrt;
                // check for max value
                if(sqrt > maxEuclideanDistance){
                    // update max value
                    maxEuclideanDistance = sqrt;
                }
            }
            // save the euclidean differences found in from's euclidean distances array
            from.setEuclideanDistance(ticks + 1.0, euclDistances);
        }
        // prepare to normalization
        Double[] listNormEuclideanDistances = new Double[turtles.size()];
        // ask every turtles
        for(Node t:turtles){
            // retrieve euclidean distances
            Double[] euclideanDistances = t.getEuclideanDistance(ticks + 1.0);
            // normalize distances
            for(int i=0;i<euclideanDistances.length;i++){
                // divide for max value
                euclideanDistances[i] /= maxEuclideanDistance;
            }
            // save normalized distances array
            t.setNormEuclideanDistance(ticks + 1.0, euclideanDistances);
        }
        
//  ;; normalize
//  let list-norm-euclidean-distance []
//  ask turtles [
//    table:put norm-euclidean-distance (ticks + 1) (map [?1 / max-euclidean-distance] \\\ table:get euclidean-distance ticks)
        
//    set list-norm-euclidean-distance fput (sentence table:get norm-euclidean-distance ticks) list-norm-euclidean-distance
//  ]
//  ask turtles [
//    table:put soc-distance-prob (ticks + 1) map[(1 - mean (item 0 list-norm-euclidean-distance))] table:get norm-euclidean-distance ticks
//  ]  
    }
    
}
