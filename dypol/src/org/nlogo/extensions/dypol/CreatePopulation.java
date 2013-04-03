package org.nlogo.extensions.dypol;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
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
class CreatePopulation extends DefaultCommand {

    // syntax to get the agent
    @Override
    public Syntax getSyntax() {
        return Syntax.commandSyntax(new int[]{Syntax.TurtleType()});
    }

    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {

        // initialize the network if it does not exist yet
        if (Dypol.g == null) {
            Dypol.g = new UndirectedSparseGraph<Turtle, Link>();
        }
        // initialize hashmap to hold variables index
        if (Dypol.variables == null) {
            // retrieve world
            World world = (World) cntxt.getAgent().world();
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
        Turtle turtle = (Turtle)argmnts[0].getTurtle();
        try {
            // add values to variables
            turtle.setVariable(Dypol.variables.get("SHAPE"), "person");
            turtle.setVariable(Dypol.variables.get("COLOR"), "blue");
        } catch (AgentException ex) {
            throw new ExtensionException("Failed to set turtle shape.");
        }
        // add node to the network
        Dypol.g.addVertex(turtle);
        
//        AgentSet.Iterator turtleIterator = world.turtles().iterator();
//        while (turtleIterator.hasNext()) {
//            // retrieve this turtle
//            Turtle turtle = (Turtle) turtleIterator.next();
//            
//            
//            
//            
//            
//        }
        throw new ExtensionException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
