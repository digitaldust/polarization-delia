package org.nlogo.extensions.dypol;

import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.PrimitiveManager;
/**
 *
 * @author Simone Gabbriellini
 * @version 1.0
 */
public class ClassManager extends DefaultClassManager {
    /**
     *
     * @param primitiveManager
     */
    @Override
  public void load(PrimitiveManager primitiveManager) {
    primitiveManager.addPrimitive("create-population", new CreatePopulation());    
  }
    
}
