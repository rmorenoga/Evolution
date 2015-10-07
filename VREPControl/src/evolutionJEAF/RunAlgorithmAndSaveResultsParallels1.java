/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionJEAF;

import es.udc.gii.common.eaf.algorithm.CMAEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
//import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.stoptest.CompositeStopTest;
import es.udc.gii.common.eaf.stoptest.DimensionFEsStopTest;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import mpi.MPI;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class RunAlgorithmAndSaveResultsParallels1 {

    public static void main(String[] args) {

        int num_runs = 1;
        long seed = -Long.MAX_VALUE;

       /* if (args.length < 1) {
            System.err.println("Argumentos insuficientes");
            System.exit(-1);
        }
        
        String eaf_config_file = args[0];
        eaf_config_file = (eaf_config_file.startsWith(File.separator) ? 
                eaf_config_file : System.getProperty("user.dir") + File.separator + eaf_config_file);*/
        
        /* Iniciar el entorno paralelo. */
        MPI.Init(args);
        MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
        int myRank = MPI.COMM_WORLD.Rank();

        /**
         * At this point the configuration file is read and it is time to set up
         * the nums.
         */
        EAFFacade eaf_facade = new EAFFacade();

        for (int i = 0; i < num_runs; i++) {

            //Inicializar el generador de números aleatorios -> Es obligatorio.
            EAFRandom.init(seed != -Long.MAX_VALUE ? seed : System.currentTimeMillis());
            //Crear el algoritmo evolutivo a partir del fichero de configuración:
            EvolutionaryAlgorithm ea = eaf_facade.createAlgorithm("" + "de_configuration_01.xml");
            //Crear el test de para a partir del fichero de configuración:
            StopTest stptst = eaf_facade.createStopTest("./" + "de_configuration_01.xml");
            System.out.println("Hi from "+myRank);
            eaf_facade.resolve(stptst, ea);

        }
        System.out.println("Finished");

        /* Finalizar el entorno paralelo. */
        MPI.Finalize();
        

    }
}
