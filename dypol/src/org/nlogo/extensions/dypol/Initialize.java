package org.nlogo.extensions.dypol;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import flanagan.math.PsRandom;
import java.util.HashMap;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.agent.World;
import org.nlogo.api.AgentException;
import org.nlogo.api.Link;
import org.nlogo.api.Syntax;
import org.nlogo.api.Turtle;

/**
 *
 * @author Simone Gabbriellini
 */
class Initialize extends DefaultCommand {

    // syntax to get the agent
    @Override
    public Syntax getSyntax() {
        return Syntax.commandSyntax(new int[]{Syntax.TurtleType()});
    }

    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {

        // retrieve world
        World world = (World) cntxt.getAgent().world();
        // retrieve ticks now
        Double ticks = world.ticks();
        // initialize the network if it does not exist yet
        if (Dypol.g == null) {
            Dypol.g = new UndirectedSparseGraph<Node, Link>();
        }
        if (Dypol.generator == null) {
            Dypol.generator = new PsRandom(Dypol.seed);
        }
        // initialize hashmap to hold variables index
        if (Dypol.variables == null) {
            // retrieve turtle who number
            int who = world.turtlesOwnIndexOf("WHO");
            // set xcor
            int xcor = world.turtlesOwnIndexOf("XCOR");
            // set ycor
            int ycor = world.turtlesOwnIndexOf("YCOR");
            // set shape
            int shape = world.turtlesOwnIndexOf("SHAPE");
            // set color 
            int color = world.turtlesOwnIndexOf("COLOR");
            // create hashmap to hold references
            Dypol.variables = new HashMap<String, Integer>();
            // add values to hashmap
            Dypol.variables.put("WHO", who);
            Dypol.variables.put("XCOR", xcor);
            Dypol.variables.put("YCOR", ycor);
            Dypol.variables.put("SHAPE", shape);
            Dypol.variables.put("COLOR", color);
        }
        // retrieve current turtle
        Turtle turtle = argmnts[0].getTurtle();
        //
        try {
            // set turtle's shape
            turtle.setVariable(Dypol.variables.get("SHAPE"), "PERSON");
            // set turtle's color
            turtle.setVariable(Dypol.variables.get("COLOR"), 105.0);
            // set turtle's xcor
            turtle.setVariable(Dypol.variables.get("XCOR"), Dypol.generator.nextDouble(0.0, 32.0));
            // set turtle's ycor
            turtle.setVariable(Dypol.variables.get("YCOR"), Dypol.generator.nextDouble(0.0, 32.0));
            
        } catch (AgentException ex) {
            // throw related error
            throw new ExtensionException(ex);
        }
        Node node = new Node();
        node.setWho((int)Math.floor((Double)turtle.getVariable(Dypol.variables.get("WHO"))));
        node.setXcor((Double)turtle.getVariable(Dypol.variables.get("XCOR")));
        node.setYcor((Double)turtle.getVariable(Dypol.variables.get("YCOR")));
        Double[] turtles = new Double[Dypol.size];
        for(int i=0;i<Dypol.size;i++){
            turtles[i] = 0.0;
        }
        node.setContactedPeople(ticks, turtles.clone());
        node.setSocDistanceProb(ticks, turtles.clone());
        node.setEuclideanDistance(ticks, turtles.clone());
        node.setNormEuclideanDistance(ticks, turtles.clone());
        // initialize issue interest
        Double[] issuesInterest = new Double[Dypol.issues];
        Double[] issuesChosen = new Double[Dypol.issues];
        for(int i=0;i<Dypol.issues;i++){
            double nextNormal = Dypol.generator.nextNormal(0, 33.3);
            if(nextNormal > 100.0){
                nextNormal = 100.0;
            } else if (nextNormal < -100.0){
                nextNormal = -100.0;
            } else if (nextNormal > 0.0 && nextNormal < 1.0){
                nextNormal = 1.0;
            } else if (nextNormal > -1.0 && nextNormal < 0.0){
                nextNormal = -1.0;
            } else {
                // do nothing
            }
            issuesInterest[i] = nextNormal;
            // bound the value
            issuesChosen[i] = 0.0;
        }
        node.setIssuesInterest(ticks, issuesInterest);
        node.setIssuesChosen(ticks, issuesChosen);
        // add node to the network
        Dypol.g.addVertex(node);
        
        
    }
}
