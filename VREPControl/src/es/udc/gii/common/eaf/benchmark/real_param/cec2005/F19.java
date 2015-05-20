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

/**
 * CEC 2005 F19: Rotated Hybrid Composition Function with a Narrow Basin for 
 * the Global Optimum
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F19 extends F18 {

    public F19() {
    }

    public F19(int dimension) {
        super(dimension);
    }

    @Override
    protected final void initData(int dimension) {
        super.initData(dimension);

        lambda[0] = 0.5 / 32.0;
        lambda[1] = 5.0 / 32.0;
        lambda[2] = 2.0;
        lambda[3] = 1.0;
        lambda[4] = 10.0 / 100.0;
        lambda[5] = 5.0 / 100.0;
        lambda[6] = 20.0;
        lambda[7] = 10.0;
        lambda[8] = 10.0 / 60.0;
        lambda[9] = 5.0 / 60.0;

        sigma[0] = 0.1;
        sigma[1] = 2.0;
        sigma[2] = 1.5;
        sigma[3] = 1.5;
        sigma[4] = 1.0;
        sigma[5] = 1.0;
        sigma[6] = 1.5;
        sigma[7] = 1.5;
        sigma[8] = 2.0;
        sigma[9] = 2.0;
    }

    @Override
    public final int getNumber() {
        return 19;
    }
}
