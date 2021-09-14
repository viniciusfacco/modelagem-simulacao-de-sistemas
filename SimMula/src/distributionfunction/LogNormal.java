/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributionfunction;

import org.apache.commons.math3.distribution.LogNormalDistribution;

/**
 *
 * @author Vinicius
 */
public class LogNormal implements DistributionFunction{
    
    private LogNormalDistribution distribution;

    @Override
    public float nextValue() {
        return (float) distribution.sample();
    }

    @Override
    //parameter[0]: sigma / parameter[1]: mi
    public boolean setParameters(float[] parameters) {
        distribution = new LogNormalDistribution(parameters[0], parameters[1]);
        return true;
    }
    
}
