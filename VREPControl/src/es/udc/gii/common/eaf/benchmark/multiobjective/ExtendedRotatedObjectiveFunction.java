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
 * ExtendedRotatedObjectiveFunction.java
 *
 * Created on November 23, 2007, 5:47 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.benchmark.LoadFromFile;
import org.apache.commons.configuration.Configuration;

/**
 * CEC 2007 Testsuite: Common definitions for extended and rotated functions.
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class ExtendedRotatedObjectiveFunction extends ObjectiveFunction {

    /** Linear transformation orthogonal matrix, with condition number=1. */
    protected double[][] M = null;
    /** The scale factor. */
    protected double[] lambda = null;
    /** The bounds of a solution in parameter space. */
    protected double[][] bounds = null;
    /** The parameter space dimension. */
    protected int dimension = 0;
    
    private String prefix = "/cec2007data/";

    /** Creates a new instance of ExtendedRotatedObjectiveFunction */
    public ExtendedRotatedObjectiveFunction() {
    }

    /** Creates a new instance of ExtendedRotatedObjectiveFunction */
    public ExtendedRotatedObjectiveFunction(int dimension) {
        this.dimension = dimension;
        initData(dimension);
    }

    private void initData(int dimension) {
        /* The file containing the transformation matrix.*/
        String mfileName = prefix + getTransformationMatrixFile();

        /* The file containing the scale factor vector.*/
        String lambdafileName = prefix + getScaleFactorVectorFile();

        M = new double[dimension][dimension];
        LoadFromFile.loadMatrixFromResourceFile(mfileName, dimension, dimension,
                M);

        lambda = new double[dimension];
        LoadFromFile.loadRowVectorFromResourceFile(lambdafileName, dimension,
                lambda);

        bounds = new double[2][dimension];
        LoadFromFile.loadMatrixFromResourceFile(prefix + getBoundsFile(), 2,
                dimension, bounds);
    }

    protected abstract String getTransformationMatrixFile();

    protected abstract String getScaleFactorVectorFile();

    protected abstract String getBoundsFile();

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        /* Dimension of the parameter space. */
        this.dimension = conf.getInt("Dimension");

        initData(this.dimension);
    }

    @Override
    public int getDimension() {
        return this.dimension;
    }        
}
