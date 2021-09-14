/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributionfunction;

/**
 *
 * @author Vinicius
 */
public interface DistributionFunction {
    
    public float nextValue();
    
    public boolean setParameters(float[] parameters);
    
}
