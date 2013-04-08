package org.nlogo.extensions.dypol;

import java.util.Collection;
import org.nlogo.agent.World;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.LogoList;
import org.nlogo.api.LogoListBuilder;

/**
 * This class reports and analyzes each simulation data.
 * @author Simone Gabbriellini
 *
 */
class Stats extends DefaultReporter {

    @Override
    public LogoList report(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        
        // retrieve world
        World world = (World) cntxt.getAgent().world();
        // list to save all results
        LogoListBuilder results = new LogoListBuilder();
        // list to save HH-Index values
        LogoListBuilder hhIndex = new LogoListBuilder();
        // list to save Kurtosis values
        LogoListBuilder kurtosis = new LogoListBuilder();
        // list to save Variance values
        LogoListBuilder variance = new LogoListBuilder();
        // list issues popularity
        LogoListBuilder mostPopular = new LogoListBuilder();
        // add HH-Index to results
        results.add(hhIndex.toLogoList());
        // retrieve all nodes
        Collection<Node> vertices = Dypol.g.getVertices();
        // add a dynamic list builder for how many issues
        for(int i=0;i<Dypol.issues;i++){
            // list to hold values of this issue
            LogoListBuilder issue = new LogoListBuilder();  
            // array to hold values for statistical analysis
            double[] values = new double[vertices.size()];
            // for each node in the network
            for(Node n:vertices){
                // retrieve value
                double attitude = n.getIssuesInterest(world.ticks())[i];
                // temporary store it
                values[n.getWho()] = attitude;
                // normalize value
                if(attitude > -100.0 && attitude < -90.0){
                    issue.add(-100.0);
                } else if(attitude > -90.0 && attitude < -80.0){
                    issue.add(-90.0);
                } else if(attitude > -80.0 && attitude < -70.0){
                    issue.add(-80.0);
                } else if(attitude > -70.0 && attitude < -60.0){
                    issue.add(-70.0);
                } else if(attitude > -60.0 && attitude < -50.0){
                    issue.add(-60.0);
                } else if(attitude > -50.0 && attitude < -40.0){
                    issue.add(-50.0);
                } else if(attitude > -40.0 && attitude < -30.0){
                    issue.add(-40.0);
                } else if(attitude > -30.0 && attitude < -20.0){
                    issue.add(-30.0);
                } else if(attitude > -20.0 && attitude < -10.0){
                    issue.add(-20.0);
                } else if(attitude > -10.0 && attitude < 0.0){
                    issue.add(-10.0);
                } else if(attitude > 0.0 && attitude < 10.0){
                    issue.add(10.0);
                } else if(attitude > 10.0 && attitude < 20.0){
                    issue.add(20.0);
                } else if(attitude > 20.0 && attitude < 30.0){
                    issue.add(30.0);
                } else if(attitude > 30.0 && attitude < 40.0){
                    issue.add(40.0);
                } else if(attitude > 40.0 && attitude < 50.0){
                    issue.add(50.0);
                } else if(attitude > 50.0 && attitude < 60.0){
                    issue.add(60.0);
                } else if(attitude > 60.0 && attitude < 70.0){
                    issue.add(70.0);
                } else if(attitude > 70.0 && attitude < 80.0){
                    issue.add(80.0);
                } else if(attitude > 80.0 && attitude < 90.0){
                    issue.add(90.0);
                } else {
                    issue.add(100.0);
                }
            }
            // add value to kurtosis list
            kurtosis.add(flanagan.analysis.Stat.kurtosis(values));
            // add value to variance list
            variance.add(flanagan.analysis.Stat.variance(values));
            // add value to most popular list
            mostPopular.add((double)Dypol.mostPopularIssue[i]);
            // add this issue values to results
            results.add(issue.toLogoList());
        }
        results.add(kurtosis.toLogoList());
        results.add(variance.toLogoList());
        results.add(mostPopular.toLogoList());
        // return values
        return results.toLogoList();
    }
    
}
