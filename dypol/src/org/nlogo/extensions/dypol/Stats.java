/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlogo.extensions.dypol;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

/**
 *
 * @author Simone Gabbriellini
 */
class Stats extends DefaultReporter {

    @Override
    public Double report(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {
        
        // TODO calculate HH index and all the stats that Delia needs.
        return Dypol.generator.nextDouble(1.0);
    }
    
    
}
