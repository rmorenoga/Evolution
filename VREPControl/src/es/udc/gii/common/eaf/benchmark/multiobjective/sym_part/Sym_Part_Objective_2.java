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
 * Sym_Part_Objective_2.java
 *
 * Created on November 20, 2007, 12:53 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.sym_part;

/**
 * CEC 2007 Testsuite: SYM_PART
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Sym_Part_Objective_2 extends Sym_Part_Objective {

    /** Creates a new instance of Sym_Part_Objective_2 */
    public Sym_Part_Objective_2() {
    }

    @Override
    public double evaluate(double[] values) {

        double omega = Math.PI / 4.0;
        double si = Math.sin(omega), co = Math.cos(omega);

        // copy array to preserve original values
        double[] x = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            x[i] = 10 * values[i] + 10;
        }

        int dim;
        double h1;

        //rotate( nx, x );
        for (dim = 0; dim + 1 < values.length; dim += 2) {
            h1 = x[dim];
            x[dim] = co * h1 - si * x[dim + 1];
            x[dim + 1] = si * h1 + co * x[dim + 1];
        }

        double x1 = x[0], x2 = x[1];

        int i, j, xnum;

        // find tile
        int[] ij = findTile(x1, x2);
        i = ij[0];
        j = ij[1];

        // restrict to 9 tiles
        if (i > 1) {
            i = 1;
        } else if (i < -1) {
            i = -1;
        }

        if (j > 1) {
            j = 1;
        } else if (j < -1) {
            j = -1;
        }

        // get values
//        double f0 = 0;
        double f1 = 0;

        for (xnum = 0; xnum < values.length; xnum++) {
            x1 = x[xnum];

            if ((xnum % 2) == 0) {
//			f0 += Math.pow(x1+a-i*c2,2);
                f1 += Math.pow(x1 - a - i * c2, 2);
            } else {
//			f0 += Math.pow(x1-j*b,2);
                f1 += Math.pow(x1 - j * b, 2);
            }
        }

//	f0 /= (double)doubleValues.size();
        f1 /= (double) values.length;

        return f1;
    }

    @Override
    public void reset() {
    }

    @Override
    public String toString() {
        return "Sym_Part_Objective_2";
    }
}
