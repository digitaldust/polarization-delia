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
        return Syntax.commandSyntax(new int[]{Syntax.TurtleType(), Syntax.NumberType()});
    }

    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {

        // initialize the network if it does not exist yet
        if (Dypol.g == null) {
            Dypol.g = new UndirectedSparseGraph<Node, Link>();
        }
        // retrieve world
        World world = (World) cntxt.getAgent().world();
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
        // retrieve the issues number
        double issuesNumber = argmnts[1].getDoubleValue();
        try {
            System.out.println("turtle " + turtle.getVariable(Dypol.variables.get("WHO")));
            // set turtle's shape
            turtle.setVariable(Dypol.variables.get("SHAPE"), "PERSON");
            // set turtle's color
            turtle.setVariable(Dypol.variables.get("COLOR"), 105.0);
            // set turtle's xcor
            turtle.setVariable(Dypol.variables.get("XCOR"), Dypol.distribution.nextDouble(0.0, 32.0));
            // set turtle's ycor
            turtle.setVariable(Dypol.variables.get("YCOR"), Dypol.distribution.nextDouble(0.0, 32.0));
            
        } catch (AgentException ex) {
            // throw related error
            throw new ExtensionException(ex);
        }
        Node node = new Node();
        node.setWho((Double)turtle.getVariable(Dypol.variables.get("WHO")));
        node.setXcor((Double)turtle.getVariable(Dypol.variables.get("XCOR")));
        node.setYcor((Double)turtle.getVariable(Dypol.variables.get("YCOR")));
        Double[] turtles = new Double[500];
        for(int i=0;i<500;i++){
            turtles[i] = 0.0;
        }
        node.setContactedPeople(world.ticks(), turtles);
        node.setSocDistanceProb(world.ticks(), turtles);
        node.setEuclideanDistance(world.ticks(), turtles);
        node.setNormEuclideanDistance(world.ticks(), turtles);
        // initialize issue interest
        Double[] issuesInterest = new Double[(int)issuesNumber];
        Double[] issuesChosen = new Double[(int)issuesNumber];
        for(int i=0;i<issuesNumber;i++){
            double nextNormal = Dypol.distribution.nextNormal(0, 33.3);
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
        node.setIssuesInterest(world.ticks(), issuesInterest);
        node.setIssuesChosen(world.ticks(), issuesChosen);
        // add node to the network
        Dypol.g.addVertex(node);
        System.out.println("net size " + Dypol.g.getVertexCount());
        
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
//        throw new ExtensionException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
