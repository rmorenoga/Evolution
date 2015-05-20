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
 * ExtendedShiftedObjectiveFunction.java
 *
 * Created on November 23, 2007, 4:49 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.benchmark.LoadFromFile;
import org.apache.commons.configuration.Configuration;

/**
 * CEC 2007 Testsuite: Common definitions for extended and shifted functions.
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class ExtendedShiftedObjectiveFunction extends ObjectiveFunction {

    /** The shifted vector in parameter space. */
    protected double[] o = null;
    /** The extended length of the lower bound. */
    protected double[] d = null;
    /** The scale factor. */
    protected double[] lambda = null;
    /** The bounds of a solution in parameter space. */
    protected double[][] bounds = null;

    /** The problem's dimension. */
    private int dimension = 0;
    
    private final static String prefix = "/cec2007data/";
    
    /**
     * Creates a new instance of ExtendedShiftedObjectiveFunction.
     */
    public ExtendedShiftedObjectiveFunction() {
    }

    /**
     * Creates a new instance of ExtendedShiftedObjectiveFunction.
     */
    public ExtendedShiftedObjectiveFunction(int dimension) {
        initData(dimension);
    }

    private void initData(int dimension) {
        /* The file containing the parameters. The first line must be the values
        of the shifted vector in parameter space (o). The second line must be
        the values of the extended length vector of the lower bound (d). And
        the third line must be the values for the scale factor vector (lambda).*/
        String fileName = prefix + getDataFile();

        double[][] data = new double[3][dimension];

        LoadFromFile.loadMatrixFromResourceFile(fileName, 3, dimension, data);

        o = data[0];
        d = data[1];
        lambda = data[2];

        bounds = new double[2][dimension];
        LoadFromFile.loadMatrixFromResourceFile(prefix + getBoundsFile(), 2, 
                dimension, bounds);
    }

    protected abstract String getDataFile();

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
