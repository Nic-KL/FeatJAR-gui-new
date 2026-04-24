/**
 */
package featJAR;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link featJAR.Feature#getParent <em>Parent</em>}</li>
 *   <li>{@link featJAR.Feature#getGroupNodeList <em>Group Node List</em>}</li>
 *   <li>{@link featJAR.Feature#getCardinality <em>Cardinality</em>}</li>
 * </ul>
 *
 * @see featJAR.FeatJARPackage#getFeature()
 * @model
 * @generated
 */
public interface Feature extends Identifiable {
	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link featJAR.GroupNode#getFeatureList <em>Feature List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(GroupNode)
	 * @see featJAR.FeatJARPackage#getFeature_Parent()
	 * @see featJAR.GroupNode#getFeatureList
	 * @model opposite="featureList" transient="false"
	 * @generated
	 */
	GroupNode getParent();

	/**
	 * Sets the value of the '{@link featJAR.Feature#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(GroupNode value);

	/**
	 * Returns the value of the '<em><b>Group Node List</b></em>' containment reference list.
	 * The list contents are of type {@link featJAR.GroupNode}.
	 * It is bidirectional and its opposite is '{@link featJAR.GroupNode#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Node List</em>' containment reference list.
	 * @see featJAR.FeatJARPackage#getFeature_GroupNodeList()
	 * @see featJAR.GroupNode#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<GroupNode> getGroupNodeList();

	/**
	 * Returns the value of the '<em><b>Cardinality</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cardinality</em>' containment reference.
	 * @see #setCardinality(Cardinality)
	 * @see featJAR.FeatJARPackage#getFeature_Cardinality()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Cardinality getCardinality();

	/**
	 * Sets the value of the '{@link featJAR.Feature#getCardinality <em>Cardinality</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cardinality</em>' containment reference.
	 * @see #getCardinality()
	 * @generated
	 */
	void setCardinality(Cardinality value);

} // Feature
