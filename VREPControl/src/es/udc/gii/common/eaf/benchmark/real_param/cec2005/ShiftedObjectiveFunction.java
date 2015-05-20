/*
* Copyright (C) 2010 Grupo Integrado de Ingeniería
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/ 


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.cec2005;

import es.udc.gii.common.eaf.benchmark.LoadFromFile;

/**
 * Base class for all shifted functions for the CEC 2005 special session on
 * real-parameter optimization (functions 1-14).
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class ShiftedObjectiveFunction extends CEC2005ObjectiveFunction {

    /** The shifted vector in parameter space. */
    protected double[] o = null;

    public ShiftedObjectiveFunction() {
        super();
    }

    public ShiftedObjectiveFunction(int dimension) {
        super();
        initData(dimension);
    }

    @Override
    protected void initData(int dimension) {
        this.dimension = dimension;
        this.o = new double[dimension];
        LoadFromFile.loadRowVectorFromResourceFile(prefix + "f" + getNumber() +
                "_o.txt", dimension, o);
    }

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[1][];
        double[] optimum = new double[dim];

        for (int i = 0; i < dim; i++) {
            optimum[i] = o[i];
        }

        optimums[0] = optimum;
        return optimums;
    }

    /**
     * Shifts a denormalized vector. The vector is shifted using the optimum in
     * parameter space.
     * @param values Denormalized vector.
     * @return Shifted vector i parameter space.
     */
    protected final double[] shift(double[] values) {
        double[] shifted = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            shifted[i] = values[i] - o[i];
        }
        return shifted;
    }

    /**
     * Shifts and translates a denormalized vector. The vector is shifted using
     * the optimum in parameter space.
     * @param values Denormalized vector to shift and translate. 
     * @param delta Amount of translation.
     * @return Shifted and translated vector in parameter space.
     */
    protected final double[] shift(double[] values, double delta) {
        double[] shifted = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            shifted[i] = values[i] - o[i] + delta;
        }
        return shifted;
    }

    @Override
    public void reset() {
    }
}
