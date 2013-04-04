package org.nlogo.extensions.dypol;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

/**
 *
 * @author Simone Gabbriellini
 */
class ClearAll extends DefaultCommand {

    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        Dypol.g = null;
    }
    
}
