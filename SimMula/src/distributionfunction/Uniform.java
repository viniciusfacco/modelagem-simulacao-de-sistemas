/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributionfunction;

import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 *
 * @author Vinicius
 */
public class Uniform implements DistributionFunction{
    
    private UniformRealDistribution distribution;

    @Override
    public float nextValue() {
        return (float) distribution.sample();
    }

    @Override
    //parameter[0]: alpha / parameter[1]: beta
    public boolean setParameters(float[] parameters) {
        distribution = new UniformRealDistribution(parameters[0], parameters[1]);
        return true;
    }
    
}
