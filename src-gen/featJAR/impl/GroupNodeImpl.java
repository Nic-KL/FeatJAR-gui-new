/**
 */
package featJAR.impl;

import featJAR.Cardinality;
import featJAR.FeatJARPackage;
import featJAR.Feature;
import featJAR.GroupNode;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link featJAR.impl.GroupNodeImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link featJAR.impl.GroupNodeImpl#getFeatureList <em>Feature List</em>}</li>
 *   <li>{@link featJAR.impl.GroupNodeImpl#getCardinality <em>Cardinality</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GroupNodeImpl extends IdentifiableImpl implements GroupNode {
	/**
	 * The cached value of the '{@link #getFeatureList() <em>Feature List</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureList()
	 * @generated
	 * @ordered
	 */
	protected EList<Feature> featureList;

	/**
	 * The cached value of the '{@link #getCardinality() <em>Cardinality</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCardinality()
	 * @generated
	 * @ordered
	 */
	protected Cardinality cardinality;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FeatJARPackage.Literals.GROUP_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Feature getParent() {
		if (eContainerFeatureID() != FeatJARPackage.GROUP_NODE__PARENT)
			return null;
		return (Feature) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(Feature newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newParent, FeatJARPackage.GROUP_NODE__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParent(Feature newParent) {
		if (newParent != eInternalContainer()
				|| (eContainerFeatureID() != FeatJARPackage.GROUP_NODE__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject) newParent).eInverseAdd(this, FeatJARPackage.FEATURE__GROUP_NODE_LIST,
						Feature.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FeatJARPackage.GROUP_NODE__PARENT, newParent,
					newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Feature> getFeatureList() {
		if (featureList == null) {
			featureList = new EObjectContainmentWithInverseEList<Feature>(Feature.class, this,
					FeatJARPackage.GROUP_NODE__FEATURE_LIST, FeatJARPackage.FEATURE__PARENT);
		}
		return featureList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cardinality getCardinality() {
		return cardinality;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCardinality(Cardinality newCardinality, NotificationChain msgs) {
		Cardinality oldCardinality = cardinality;
		cardinality = newCardinality;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					FeatJARPackage.GROUP_NODE__CARDINALITY, oldCardinality, newCardinality);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCardinality(Cardinality newCardinality) {
		if (newCardinality != cardinality) {
			NotificationChain msgs = null;
			if (cardinality != null)
				msgs = ((InternalEObject) cardinality).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - FeatJARPackage.GROUP_NODE__CARDINALITY, null, msgs);
			if (newCardinality != null)
				msgs = ((InternalEObject) newCardinality).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - FeatJARPackage.GROUP_NODE__CARDINALITY, null, msgs);
			msgs = basicSetCardinality(newCardinality, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FeatJARPackage.GROUP_NODE__CARDINALITY,
					newCardinality, newCardinality));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetParent((Feature) otherEnd, msgs);
		case FeatJARPackage.GROUP_NODE__FEATURE_LIST:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getFeatureList()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			return basicSetParent(null, msgs);
		case FeatJARPackage.GROUP_NODE__FEATURE_LIST:
			return ((InternalEList<?>) getFeatureList()).basicRemove(otherEnd, msgs);
		case FeatJARPackage.GROUP_NODE__CARDINALITY:
			return basicSetCardinality(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			return eInternalContainer().eInverseRemove(this, FeatJARPackage.FEATURE__GROUP_NODE_LIST, Feature.class,
					msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			return getParent();
		case FeatJARPackage.GROUP_NODE__FEATURE_LIST:
			return getFeatureList();
		case FeatJARPackage.GROUP_NODE__CARDINALITY:
			return getCardinality();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			setParent((Feature) newValue);
			return;
		case FeatJARPackage.GROUP_NODE__FEATURE_LIST:
			getFeatureList().clear();
			getFeatureList().addAll((Collection<? extends Feature>) newValue);
			return;
		case FeatJARPackage.GROUP_NODE__CARDINALITY:
			setCardinality((Cardinality) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			setParent((Feature) null);
			return;
		case FeatJARPackage.GROUP_NODE__FEATURE_LIST:
			getFeatureList().clear();
			return;
		case FeatJARPackage.GROUP_NODE__CARDINALITY:
			setCardinality((Cardinality) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case FeatJARPackage.GROUP_NODE__PARENT:
			return getParent() != null;
		case FeatJARPackage.GROUP_NODE__FEATURE_LIST:
			return featureList != null && !featureList.isEmpty();
		case FeatJARPackage.GROUP_NODE__CARDINALITY:
			return cardinality != null;
		}
		return super.eIsSet(featureID);
	}

} //GroupNodeImpl
