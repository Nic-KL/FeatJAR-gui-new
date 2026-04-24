package de.featjar.gui.handler.utils;

import featJAR.Cardinality;
import featJAR.FeatJARFactory;

public interface ICardinialityUtils {
	default Cardinality createCardinality(int lowerBound, int upperBound) {
		Cardinality cardinality = FeatJARFactory.eINSTANCE.createCardinality();
		cardinality.setLowerBound(lowerBound);
		cardinality.setUpperBound(upperBound);
		return cardinality;
	}
	
	// CREATE FEATURES
	
	default Cardinality createAndCardinality() {
		return createCardinality(0, -1);
	}
	
	default Cardinality createOrCardinality() {
		return createCardinality(1, -1);
	}
	
	default Cardinality createXorCardinality() {
		return createCardinality(1, 1);
	}
	
	// FEATURES
	
    default boolean isOptional(Cardinality cardinality) {
        return cardinality.getLowerBound() <= 0;
    }

    default boolean isMandatory(Cardinality cardinality) {
        return cardinality.getLowerBound() > 0;
    }

    default boolean isMultiple(Cardinality cardinality) {
        return cardinality.getUpperBound() > 1;
    }
    
   // GROUPS
	
   default boolean isAnd(Cardinality c) {
	   return c.getLowerBound() == 0 && c.getUpperBound() == -1;
   }
   
   default boolean isOr(Cardinality c) {
	   return c.getLowerBound() == 1 && c.getUpperBound() == -1;
   }
   
   default boolean isXor(Cardinality c) {
	   return c.getLowerBound() == 1 && c.getUpperBound() == 1;
   }
   
   default boolean isCardinality(Cardinality c) {
	   return !isAnd(c) && isOr(c) && isXor(c);
   }
	
}
