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
 * CEC 2005 F20: Rotated Hybrid Composition Function with the Global Optimum
 * on the Bounds
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F20 extends F18 {

    public F20() {
    }

    public F20(int dimension) {
        super(dimension);
    }

    @Override
    protected final void initData(int dimension) {
        super.initData(dimension);

        int d2 = dimension / 2;

        for (int i = 1; i <= d2; i++) {
            o[0][2 * i - 1] = 5.0;
        }
    }

    @Override
    public final int getNumber() {
        return 20;
    }
}
