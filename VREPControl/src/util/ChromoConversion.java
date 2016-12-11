/* 
 * EDHMOR - Evolutionary designer of heterogeneous modular robots
 * <https://bitbucket.org/afaina/edhmor>
 * Copyright (C) 2015 GII (UDC) and REAL (ITU)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package util;

import java.util.Arrays;

/**
 *
 * @author fai
 */
public class ChromoConversion {

    static public double[] str2double(String cromo) {

        //Remove the fitness and brackets
        if (cromo.startsWith("[")) {
            int inicio = cromo.indexOf("(") + 1;
            int fin = cromo.lastIndexOf("]");
            cromo = cromo.substring(inicio, fin);
        }


        //Split the string using "," or " "
        String[] strs = cromo.split("(,)|(\\s)");
        
        //Remove the empty strings
        strs = Arrays.stream(strs)
                .filter(s -> (s != null && s.length() > 0))
                .toArray(String[]::new);   
            
        //convert them into doubles
        double[] chromosome = new double[strs.length];
        for (int i = 0; i < strs.length; i++) {
            chromosome[i] = Double.valueOf(strs[i]);
        }
            
        return chromosome;
    }

    static public int[] str2int(String chromo) {

        double[] chromoDouble = str2double(chromo);
        int[] chromosome = new int[chromoDouble.length];
        for (int i = 0; i < chromoDouble.length; i++) {
            chromosome[i] = (int) Math.floor(chromoDouble[i]);
        }
        return chromosome;
    }
}
