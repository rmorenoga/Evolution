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
 * Sym_Part_Objective.java
 *
 * Created on November 20, 2007, 12:26 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.sym_part;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import org.apache.commons.configuration.Configuration;

/**
 * CEC 2007 Testsuite: SYM_PART
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class Sym_Part_Objective extends ObjectiveFunction {

    protected final double a = 1.0;
    protected final double b = 10.0;
    protected final double c = 8.0;
    protected final double c1 = (a + c / 2);
    protected final double c2 = (c + 2 * a);
    protected final double b1 = (b / 2);
    private int dimension = 0;
    
    /** Creates a new instance of Sym_Part_Objective */
    public Sym_Part_Objective() {
    }

    /* these variables are needed to speed up rotation */
    protected int[] findTile(double x1, double x2) {
        int[] t = new int[2];

        double xx1 = (x1 < 0) ? -x1 : x1;
        double xx2 = (x2 < 0) ? -x2 : x2;

        t[0] = (xx1 < c1) ? 0 : ((int) Math.ceil((xx1 - c1) / c2));
        t[1] = (xx2 < b1) ? 0 : ((int) Math.ceil((xx2 - b1) / b));
        if (x1 < 0) {
            t[0] = -(t[0]);
        }
        if (x2 < 0) {
            t[1] = -(t[1]);
        }
        return t;
    }

    /* returns tile number between 0 and 8
    returns - 1 if out of any tile, function does
    not depend on objFct! */
    protected int findTileSYMPART(double x1, double x2) {
        int i, j, dim;
        double[] x = new double[2];
        double h1;
        double omega = Math.PI / 4.0;
        double si = Math.sin(omega), co = Math.cos(omega);

        x[0] = x1;
        x[1] = x2;

        //rotate( 2, x );
        for (dim = 0; dim + 1 < 2; dim += 2) {
            h1 = x[dim];
            x[dim] = co * h1 - si * x[dim + 1];
            x[dim + 1] = si * h1 + co * x[dim + 1];
        }

        int[] ij = findTile(x[0], x[1]);

        i = ij[0];
        j = ij[1];

        // restrict to 9 tiles
        if (Math.abs(i) > 1 || Math.abs(j) > 1) {
            return -1;
        }

        return (i + 1) * 3 + (j + 1);
    }

    @Override
    public void configure(Configuration conf) {
        this.dimension = conf.getInt("Dimension");
    }

    
    @Override
    public int getDimension() {
        return this.dimension;
    }        
}
