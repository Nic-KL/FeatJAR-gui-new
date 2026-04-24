package de.featjar.gui.handler.utils;

import featJAR.Cardinality;
import featJAR.FeatJARFactory;

/**
 * <p> Use {@link ICardinialityUtils} instead.
 * @deprecated
 */
public class CardinalityHelper {
	
		private static Cardinality createCarinality(int lowerBound, int upperBound) {
			Cardinality cardinality = FeatJARFactory.eINSTANCE.createCardinality();
			cardinality.setLowerBound(lowerBound);
			cardinality.setUpperBound(upperBound);
			return cardinality;
		}
		
		// CREATE FEATURES
		
		public static Cardinality createAndCardinality() {
			return createCarinality(0, -1);
		}
		
		public static Cardinality createOrCardinality() {
			return createCarinality(1, -1);
		}
		
		public static Cardinality createXorCardinality() {
			return createCarinality(1, 1);
		}
		
		// FEATURES
		
	    public static boolean isOptional(Cardinality cardinality) {
	        return cardinality.getLowerBound() <= 0;
	    }

	    public static boolean isMandatory(Cardinality cardinality) {
	        return cardinality.getLowerBound() > 0;
	    }

	    public static boolean isMultiple(Cardinality cardinality) {
	        return cardinality.getUpperBound() > 1;
	    }
	    
	   // GROUPS
		
	   public static boolean isAnd(Cardinality c) {
		   return c.getLowerBound() == 0 && c.getUpperBound() == -1;
	   }
	   
	   public static boolean isOr(Cardinality c) {
		   return c.getLowerBound() == 1 && c.getUpperBound() == -1;
	   }
	   
	   public static boolean isXor(Cardinality c) {
		   return c.getLowerBound() == 1 && c.getUpperBound() == 1;
	   }
	   
	   public static boolean isCardinality(Cardinality c) {
		   return !isAnd(c) && isOr(c) && isXor(c);
	   }
}
