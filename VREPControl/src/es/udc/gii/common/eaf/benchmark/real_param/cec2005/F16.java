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
 * CEC 2005 F16: Rotated Hybrid Composition Function
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F16 extends F15 {

    public F16() {
    }

    public F16(int dimension) {
        super(dimension);
    }

    @Override
    public int getNumber() {
        return 16;
    }

    @Override
    protected final void initData(int dimension) {
        super.initData(dimension);
        double[][] m = new double[N * dimension][dimension];

        if (dimension <= 10) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m10.txt", N * dimension, dimension, m);
        } else if (dimension > 10 && dimension <= 30) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m30.txt", N * dimension, dimension, m);
        } else if (dimension > 30 && dimension <= 50) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m50.txt", N * dimension, dimension, m);
        }

        for (int n = 0; n < N; n++) {
            for (int r = 0; r < dimension; r++) {
                M[n][r] = m[n * dimension + r];
            }
        }
    }
}
