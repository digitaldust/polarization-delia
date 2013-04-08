package org.nlogo.extensions.dypol;

import org.nlogo.agent.World;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

/**
 * This class clears from memory some values stored in the class manager.
 * @author Simone Gabbriellini
 */
class ClearAll extends DefaultCommand {
    
    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        
        Dypol.g = null;
        Dypol.generator = null;
        Dypol.variables = null;
        Dypol.linkId = 0;
        Dypol.interactions = 0;
        Dypol.mostPopularIssue = null;
        // retrieve world
        World world = (World) cntxt.getAgent().world();
        // IMPORTANT!!! add here all the new GUI parameters
        // should I save to file?
        Dypol.saveToFile = (Boolean)world.getObserverVariableByName("SAVE-TO-FILE");
        Dypol.issues = (int)Math.floor((Double)world.getObserverVariableByName("NUMBER-OF-ISSUES"));
        Dypol.infl = (Double)world.getObserverVariableByName("INFL");
        Dypol.seed = (int)Math.floor((Double)world.getObserverVariableByName("SEED-NUMBER"));
        Dypol.size = (int)Math.floor((Double)world.getObserverVariableByName("POPULATION"));
        Dypol.memory = (int)Math.floor((Double)world.getObserverVariableByName("MEMORY"));
        
    }
    
}
