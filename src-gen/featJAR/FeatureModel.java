/**
 */
package featJAR;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link featJAR.FeatureModel#getRoots <em>Roots</em>}</li>
 *   <li>{@link featJAR.FeatureModel#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @see featJAR.FeatJARPackage#getFeatureModel()
 * @model
 * @generated
 */
public interface FeatureModel extends Identifiable {
	/**
	 * Returns the value of the '<em><b>Roots</b></em>' containment reference list.
	 * The list contents are of type {@link featJAR.Feature}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Roots</em>' containment reference list.
	 * @see featJAR.FeatJARPackage#getFeatureModel_Roots()
	 * @model containment="true"
	 * @generated
	 */
	EList<Feature> getRoots();

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link featJAR.Constraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see featJAR.FeatJARPackage#getFeatureModel_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

} // FeatureModel
