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
public class InitializeDistances extends DefaultCommand {

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
        for (Node from : turtles) {
            // ...to ask all other turtles
            for (Node to : turtles) {
                // do not do it for self with self
                if (from.getWho() != to.getWho()) {
                    // array of from's interest in issues
                    Double[] a = from.getIssuesInterest(ticks);
                    // array of to's interest in issues 
                    Double[] b = to.getIssuesInterest(ticks);
                    // array to hold squared differences between the two
                    double squaredDifferences = 0;
                    // cicle through the array
                    for (int i = 0; i < Dypol.issues; i++) {
                        // find squared differences between from and to's values
                        squaredDifferences += Math.pow(a[i] - b[i], 2.0);
                    }
                    // find squared value 
                    double sqrt = Math.sqrt(squaredDifferences);
                    // save value in euclidean distances array
                    from.getEuclideanDistance(ticks)[to.getWho()] = sqrt;
                    // check for max value
                    if (sqrt > maxEuclideanDistance) {
                        // update max value
                        maxEuclideanDistance = sqrt;
                    }
                }
            }
        }
        // ask every turtles
        for (Node pi : turtles) {
            for (Node pj : turtles) {
                pi.getNormEuclideanDistance(ticks)[pj.getWho()] = pi.getEuclideanDistance(ticks)[pj.getWho()] / maxEuclideanDistance;
            }
        }
        // find social probabilities to be chosen
        double meanValue = 0;
        // counter to count how many entries we have
        double counter = 0;
        // for each turtle
        for (Node t : turtles) {
            // retrieve the normalized euclidean distances array
            Double[] normEuclideanDistances = t.getNormEuclideanDistance(ticks);
            // for each value
            for (double d : normEuclideanDistances) {
                // sum it
                meanValue += d;
                // and increment counter
                counter++;
            }
        }
        // find mean value of normalized euclidean distances
        meanValue /= counter;
        // save the array for each turtle
        for (Node t : turtles) {
            // fill the array with the probability value
            for (int i = 0; i < Dypol.size; i++) {
                if(i!=t.getWho()){
                    t.getSocDistanceProb(ticks)[i] = 1 - meanValue;
                }
            }
        }
    }
}
