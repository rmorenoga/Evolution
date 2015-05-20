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
 * LoadFromFile.java
 *
 * Created on 17 de julio de 2007, 17:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class LoadFromFile {
    
    //
    // Utility functions for loading data from the given text file
    //
    static public void loadTestDataFromFile(String file, int num_test_points, int test_dimension, double[][] x, double[] f) {
        try {
            BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadMatrix(brSrc, num_test_points, test_dimension, x);
            loadColumnVector(brSrc, num_test_points, f);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    static public void loadRowVectorFromFile(String file, int columns, double[] row) {
        try {
            BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadRowVector(brSrc, columns, row);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    static public void loadRowVectorFromResourceFile(String resource, int columns, double[] row) {
        try {
            InputStream is = LoadFromFile.class.getResourceAsStream(resource);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader brSrc = new BufferedReader(isr);
            loadRowVector(brSrc, columns, row);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }    
    
    static public void loadColumnVectorFromResourceFile(String resource, int rows, double[] column) {
        try {
            InputStream is = LoadFromFile.class.getResourceAsStream(resource);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader brSrc = new BufferedReader(isr);
            loadColumnVector(brSrc, rows, column);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    static public void loadRowVector(BufferedReader brSrc, int columns, double[] row) throws Exception {
        String stToken;
        String line = brSrc.readLine();
        StringTokenizer stTokenizer = new StringTokenizer(line);
        for (int i = 0 ; i < columns ; i ++) {
            stToken = stTokenizer.nextToken();
            row[i] = Double.parseDouble(stToken);
        }
    }
    
    static public void loadColumnVectorFromFile(String file, int rows, double[] column) {
        try {
            BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadColumnVector(brSrc, rows, column);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    static public void loadColumnVector(BufferedReader brSrc, int rows, double[] column) throws Exception {
        String stToken;
        for (int i = 0 ; i < rows ; i ++) {
            StringTokenizer stTokenizer = new StringTokenizer(brSrc.readLine());
            stToken = stTokenizer.nextToken();
            column[i] = Double.parseDouble(stToken);
        }
    }
    
    static public void loadNMatrixFromFile(String file, int N, int rows, int columns, double[][][] matrix) {
        try {
            BufferedReader brSrc = new BufferedReader(new FileReader(file));
            for (int i = 0 ; i < N ; i ++) {
                loadMatrix(brSrc, rows, columns, matrix[i]);
            }
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    static public void loadMatrixFromFile(String file, int rows, int columns, double[][] matrix) {
        try {
            BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadMatrix(brSrc, rows, columns, matrix);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    static public void loadMatrixFromResourceFile(String resource, int rows, int columns, double[][] matrix) {
        try {                        
            InputStream is = LoadFromFile.class.getResourceAsStream(resource);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader brSrc = new BufferedReader(isr);
            loadMatrix(brSrc, rows, columns, matrix);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }    
    
    static public void loadMatrix(BufferedReader brSrc, int rows, int columns, double[][] matrix) throws Exception {
        for (int i = 0 ; i < rows ; i ++) {
            loadRowVector(brSrc, columns, matrix[i]);
        }
    }
    
}
