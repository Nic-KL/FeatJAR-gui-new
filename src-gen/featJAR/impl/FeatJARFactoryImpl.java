/**
 */
package featJAR.impl;

import featJAR.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FeatJARFactoryImpl extends EFactoryImpl implements FeatJARFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FeatJARFactory init() {
		try {
			FeatJARFactory theFeatJARFactory = (FeatJARFactory) EPackage.Registry.INSTANCE
					.getEFactory(FeatJARPackage.eNS_URI);
			if (theFeatJARFactory != null) {
				return theFeatJARFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FeatJARFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatJARFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case FeatJARPackage.FEATURE_MODEL:
			return createFeatureModel();
		case FeatJARPackage.FEATURE:
			return createFeature();
		case FeatJARPackage.CONSTRAINT:
			return createConstraint();
		case FeatJARPackage.GROUP_NODE:
			return createGroupNode();
		case FeatJARPackage.ATTRIBUTES:
			return createAttributes();
		case FeatJARPackage.CARDINALITY:
			return createCardinality();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FeatureModel createFeatureModel() {
		FeatureModelImpl featureModel = new FeatureModelImpl();
		return featureModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Feature createFeature() {
		FeatureImpl feature = new FeatureImpl();
		return feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Constraint createConstraint() {
		ConstraintImpl constraint = new ConstraintImpl();
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GroupNode createGroupNode() {
		GroupNodeImpl groupNode = new GroupNodeImpl();
		return groupNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Attributes createAttributes() {
		AttributesImpl attributes = new AttributesImpl();
		return attributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cardinality createCardinality() {
		CardinalityImpl cardinality = new CardinalityImpl();
		return cardinality;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FeatJARPackage getFeatJARPackage() {
		return (FeatJARPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FeatJARPackage getPackage() {
		return FeatJARPackage.eINSTANCE;
	}

} //FeatJARFactoryImpl
