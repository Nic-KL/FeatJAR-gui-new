/**
 */
package featJAR;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link featJAR.GroupNode#getParent <em>Parent</em>}</li>
 *   <li>{@link featJAR.GroupNode#getFeatureList <em>Feature List</em>}</li>
 *   <li>{@link featJAR.GroupNode#getCardinality <em>Cardinality</em>}</li>
 * </ul>
 *
 * @see featJAR.FeatJARPackage#getGroupNode()
 * @model
 * @generated
 */
public interface GroupNode extends Identifiable {
	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link featJAR.Feature#getGroupNodeList <em>Group Node List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(Feature)
	 * @see featJAR.FeatJARPackage#getGroupNode_Parent()
	 * @see featJAR.Feature#getGroupNodeList
	 * @model opposite="groupNodeList" transient="false"
	 * @generated
	 */
	Feature getParent();

	/**
	 * Sets the value of the '{@link featJAR.GroupNode#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Feature value);

	/**
	 * Returns the value of the '<em><b>Feature List</b></em>' containment reference list.
	 * The list contents are of type {@link featJAR.Feature}.
	 * It is bidirectional and its opposite is '{@link featJAR.Feature#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature List</em>' containment reference list.
	 * @see featJAR.FeatJARPackage#getGroupNode_FeatureList()
	 * @see featJAR.Feature#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<Feature> getFeatureList();

	/**
	 * Returns the value of the '<em><b>Cardinality</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cardinality</em>' containment reference.
	 * @see #setCardinality(Cardinality)
	 * @see featJAR.FeatJARPackage#getGroupNode_Cardinality()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Cardinality getCardinality();

	/**
	 * Sets the value of the '{@link featJAR.GroupNode#getCardinality <em>Cardinality</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cardinality</em>' containment reference.
	 * @see #getCardinality()
	 * @generated
	 */
	void setCardinality(Cardinality value);

} // GroupNode
