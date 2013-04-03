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
class CreatePopulation extends DefaultCommand {

    //
    @Override
    public Syntax getSyntax() {
        return Syntax.commandSyntax(new int[]{Syntax.NumberType()});
    }

    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        
        int size = argmnts[0].getIntValue();
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
