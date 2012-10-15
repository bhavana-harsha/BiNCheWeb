package BiNGO.methods.hypergeometric;

/* * Copyright (c) 2005 Flanders Interuniversitary Institute for Biotechnology (VIB)
 * *
 * * Authors : Steven Maere, Karel Heymans
 * *
 * * This program is free software; you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation; either version 2 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * * The software and documentation provided hereunder is on an "as is" basis,
 * * and the Flanders Interuniversitary Institute for Biotechnology
 * * has no obligations to provide maintenance, support,
 * * updates, enhancements or modifications.  In no event shall the
 * * Flanders Interuniversitary Institute for Biotechnology
 * * be liable to any party for direct, indirect, special,
 * * incidental or consequential damages, including lost profits, arising
 * * out of the use of this software and its documentation, even if
 * * the Flanders Interuniversitary Institute for Biotechnology
 * * has been advised of the possibility of such damage. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program; if not, write to the Free Software
 * * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * *
 * * Authors: Steven Maere, Karel Heymans
 * * Date: Mar.25.2005
 * * Description: Class that calculates the hypergeometric test results for a given cluster    
 * */

import BiNGO.interfaces.CalculateTestTask;
import BiNGO.interfaces.DistributionCount;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cytoscape.task.TaskMonitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/**
 * ************************************************************
 * HypergeometricTestCalculate.java
 * -----------------------------------------
 * <p/>
 * Steven Maere & Karel Heymans (c) March 2005
 * <p/>
 * Class that calculates the hypergeometric test results for a given cluster
 * *************************************************************
 */

public class HypergeometricTestCalculateUnder implements CalculateTestTask {

    /**
     * logger (replacement for cyto's task monitor)
     */
    private static final Logger LOGGER = Logger.getLogger(HypergeometricTestCalculateUnder.class);

    /*--------------------------------------------------------------
    FIELDS.
    --------------------------------------------------------------*/

    /**
     * hashmap with as values the values of small n ; keys = GO labels.
     */
    private static HashMap mapSmallN;
    /**
     * hashmap with as values the values of small x ; keys = GO labels.
     */
    private static HashMap mapSmallX;
    /**
     * hashmap with values for big N.
     */
    private static HashMap mapBigN;
    /**
     * hashmap with values for big X.
     */
    private static HashMap mapBigX;
    /**
     * hashmap with the hypergeometric distribution results as values ; keys = GO labels
     */
    private static HashMap hypergeometricTestMap;

    // Keep track of progress for monitoring:
    private int maxValue;
    private boolean interrupted = false;

    /*--------------------------------------------------------------
    CONSTRUCTOR.
    --------------------------------------------------------------*/

    /**
     * constructor with as argument the selected cluster and the
     * annotation, ontology and alpha.
     */
    public HypergeometricTestCalculateUnder(DistributionCount dc) {

        dc.calculate();
        HypergeometricTestCalculateUnder.mapSmallN = dc.getMapSmallN();
        HypergeometricTestCalculateUnder.mapSmallX = dc.getMapSmallX();
        HypergeometricTestCalculateUnder.mapBigN = dc.getMapBigN();
        HypergeometricTestCalculateUnder.mapBigX = dc.getMapBigX();
        this.maxValue = mapSmallX.size();

    }

    /*--------------------------------------------------------------
      METHODS.
    --------------------------------------------------------------*/

    /**
     * method that redirects the calculation of hypergeometric distribution.
     */
    public void calculate() {

        {

            HypergeometricDistributionUnder hd;
            hypergeometricTestMap = new HashMap();

            HashSet set = new HashSet(mapSmallX.keySet());
            Iterator iterator = set.iterator();
            Integer id;
            Integer smallXvalue;
            Integer smallNvalue;
            Integer bigXvalue;
            Integer bigNvalue;
            int currentProgress = 0;
            try {
                while (iterator.hasNext()) {
                    id = new Integer(iterator.next().toString());
                    smallXvalue = new Integer(mapSmallX.get(id).toString());
                    smallNvalue = new Integer(mapSmallN.get(id).toString());
                    bigXvalue = new Integer(mapBigX.get(id).toString());
                    bigNvalue = new Integer(mapBigN.get(id).toString());
                    hd = new HypergeometricDistributionUnder(smallXvalue.intValue(), bigXvalue.intValue(),
                            smallNvalue.intValue(), bigNvalue.intValue());
                    hypergeometricTestMap.put(id, hd.calculateHypergDistr());

                    // set progress

                    currentProgress++;

                    if (interrupted) {
                        throw new InterruptedException();
                    }

                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, "Hypergeometric P-value calculation cancelled " + e);
            }
        }
    }

    /*--------------------------------------------------------------
      GETTERS.
    --------------------------------------------------------------*/

    /**
     * getter for the hypergeometric test map.
     *
     * @return HashMap hypergeometricTestMap
     */
    @Override
	public HashMap getTestMap() {

        return hypergeometricTestMap;
    }

    /**
     * getter for the mapSmallX.
     *
     * @return HashMap mapSmallX
     */
    @Override
	public HashMap getMapSmallX() {

        return mapSmallX;
    }

    /**
     * getter for the mapSmallN.
     *
     * @return HashMap mapSmallN
     */
    @Override
	public HashMap getMapSmallN() {

        return mapSmallN;
    }

    /**
     * getter for the bigX.
     *
     * @return HashMap
     */
    @Override
	public HashMap getMapBigX() {

        return mapBigX;
    }

    /**
     * getter for the bigN.
     *
     * @return HashMap
     */
    @Override
	public HashMap getMapBigN() {

        return mapBigN;
    }

    /**
     * Run the Task.
     */
    @Override
	public void run() {

        calculate();
    }

    /**
     * Non-blocking call to interrupt the task.
     */
    @Override
	public void halt() {

        this.interrupted = true;
    }

    /**
     * Gets the Task Title.
     *
     * @return human readable task title.
     */
    @Override
	public String getTitle() {

        return new String("Calculating Hypergeometric Distribution");
    }

	@Override
	public void setTaskMonitor(TaskMonitor arg0)
			throws IllegalThreadStateException {
		// TODO Auto-generated method stub
		
	}


}
