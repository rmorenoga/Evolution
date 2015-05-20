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
 * Base class for all shifted and rotated functions for the CEC 2005 special session on
 * real-parameter optimization (functions 1-14).
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class ShiftedRotatedObjectiveFunction extends ShiftedObjectiveFunction {

    /** The transformation matrix. */
    protected double[][] M;

    public ShiftedRotatedObjectiveFunction() {
    }

    public ShiftedRotatedObjectiveFunction(int dimension) {
        super(dimension);
        initData(dimension);
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);
        M = new double[dimension][dimension];

        if (dimension <= 10) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m10.txt", dimension, dimension, M);
        } else if (dimension > 10 && dimension <= 30) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m30.txt", dimension, dimension, M);
        } else if (dimension > 30 && dimension <= 50) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m50.txt", dimension, dimension, M);
        }
    }

    /**
     * Shifts and rotates a denormalized vector in parameter space.
     * @param values Denormalized vector.
     * @return Shifted and rotated vector.
     */
    protected final double[] shiftAndRotate(double[] values) {
        double[] res = new double[values.length];

        double[] dvalues = shift(values);

        for (int c = 0; c < values.length; c++) {
            for (int i = 0; i < values.length; i++) {
                res[c] += dvalues[i] * M[i][c];
            }
        }

        return res;
    }
}
