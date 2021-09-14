/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributionfunction;

import org.apache.commons.math3.distribution.GammaDistribution;

/**
 *
 * @author Vinicius
 */
public class Gamma implements DistributionFunction{
    
    private GammaDistribution distribution;
    
    public Gamma(){
        
    }

    @Override
    public float nextValue() {
        return (float) distribution.sample();
    }

    @Override
    //parameter[0]: alpha / parameter[1]: beta / parameter[2]: gamma
    public boolean setParameters(float[] parameters) {
        distribution = new GammaDistribution(parameters[0], parameters[1], parameters[2]);
        return true;
    }
    
}
