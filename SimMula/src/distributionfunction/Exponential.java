/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributionfunction;

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 *
 * @author Vinicius
 */
public class Exponential implements DistributionFunction{
    
    private ExponentialDistribution distribution;
    
    public Exponential(){
        
    }

    @Override
    public float nextValue() {
        return (float) distribution.sample();
    }

    @Override
    //parameter[0]: média / parameter[1]: lambda
    public boolean setParameters(float[] parameters) {
        distribution = new ExponentialDistribution(parameters[0], parameters[1]);
        return true;
    }
    
}
