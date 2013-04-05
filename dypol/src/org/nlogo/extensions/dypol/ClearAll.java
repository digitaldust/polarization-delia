package org.nlogo.extensions.dypol;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

/**
 *
 * @author Simone Gabbriellini
 */
class ClearAll extends DefaultCommand {

    @Override
    public Syntax getSyntax() {
        return Syntax.commandSyntax(new int[]{Syntax.NumberType(), Syntax.NumberType(), Syntax.NumberType(), Syntax.NumberType()});
    }
    
    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        
        // check
        if(argmnts[0]==null || argmnts[1]==null || argmnts[2]==null || argmnts[3]==null){
            throw new ExtensionException("You must provide four values in this order: number of issues, infl, seed and population size.");
        }
        
        Dypol.g = null;
        Dypol.generator = null;
        Dypol.variables = null;
        Dypol.issues = argmnts[0].getIntValue();
        Dypol.infl = argmnts[1].getDoubleValue();
        Dypol.seed = argmnts[2].getIntValue();
        Dypol.size = argmnts[3].getIntValue();
    }
    
}
