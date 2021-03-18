package cn.modoumama.common.spring.filter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * 类描述：TODO(用一句话描述该类做什么)<br>
 * <br/>
 * 创建人：邓强 <br>
 * 创建时间：2017年12月7日 下午3:37:57 <br>
 * 修改人： <br>
 * 修改时间：2017年12月7日 下午3:37:57 <br>
 * 修改备注： <br>
 * 
 * @version V1.0
 */
public class AnnotationMethodFilter extends AbstractTypeHierarchyTraversingFilter {
	/** 类上的注解 */
	private final Class<? extends Annotation> typeAnnotationType;
	/** 方法上的注解 */
	private final Class<? extends Annotation> methodAnnotationType;

	private final boolean considerInherited;

	private final boolean considerInterfaces;

	/**
	 * Create a new AnnotationTypeFilter for the given annotation type. This
	 * filter will also match meta-annotations. To disable the meta-annotation
	 * matching, use the constructor that accepts a
	 * '{@code considerMetaAnnotations}' argument. The filter will not match
	 * interfaces.
	 * 
	 * @param annotationType
	 *            the annotation type to match
	 */
	public AnnotationMethodFilter(Class<? extends Annotation> methodAnnotationType) {
		this(null, methodAnnotationType, true);
	}

	/**
	 * Create a new AnnotationTypeFilter for the given annotation type. This
	 * filter will also match meta-annotations. To disable the meta-annotation
	 * matching, use the constructor that accepts a
	 * '{@code considerMetaAnnotations}' argument. The filter will not match
	 * interfaces.
	 * 
	 * @param annotationType
	 *            the annotation type to match
	 */
	public AnnotationMethodFilter(Class<? extends Annotation> methodAnnotationType, boolean considerMetaAnnotations) {
		this(null, methodAnnotationType, considerMetaAnnotations);
	}

	/**
	 * Create a new AnnotationTypeFilter for the given annotation type. This
	 * filter will also match meta-annotations. To disable the meta-annotation
	 * matching, use the constructor that accepts a
	 * '{@code considerMetaAnnotations}' argument. The filter will not match
	 * interfaces.
	 * 
	 * @param annotationType
	 *            the annotation type to match
	 */
	public AnnotationMethodFilter(Class<? extends Annotation> methodAnnotationType, boolean considerMetaAnnotations,
			boolean considerInterfaces) {
		this(null, methodAnnotationType, considerMetaAnnotations, considerInterfaces);
	}

	/**
	 * Create a new AnnotationTypeFilter for the given annotation type. This
	 * filter will also match meta-annotations. To disable the meta-annotation
	 * matching, use the constructor that accepts a
	 * '{@code considerMetaAnnotations}' argument. The filter will not match
	 * interfaces.
	 * 
	 * @param annotationType
	 *            the annotation type to match
	 */
	public AnnotationMethodFilter(Class<? extends Annotation> typeAnnotationType,
			Class<? extends Annotation> methodAnnotationType) {
		this(typeAnnotationType, methodAnnotationType, true);
	}

	/**
	 * Create a new AnnotationTypeFilter for the given annotation type. The
	 * filter will not match interfaces.
	 * 
	 * @param annotationType
	 *            the annotation type to match
	 * @param considerMetaAnnotations
	 *            whether to also match on meta-annotations
	 */
	public AnnotationMethodFilter(Class<? extends Annotation> typeAnnotationType,
			Class<? extends Annotation> methodAnnotationType, boolean considerMetaAnnotations) {
		this(typeAnnotationType, methodAnnotationType, considerMetaAnnotations, true);
	}

	/**
	 * Create a new {@link AnnotationTypeFilter} for the given annotation type.
	 * 
	 * @param annotationType
	 *            the annotation type to match
	 * @param considerMetaAnnotations
	 *            whether to also match on meta-annotations
	 * @param considerInterfaces
	 *            whether to also match interfaces
	 */
	public AnnotationMethodFilter(Class<? extends Annotation> typeAnnotationType,
			Class<? extends Annotation> methodAnnotationType, boolean considerInherited, boolean considerInterfaces) {
		super(typeAnnotationType == null ? true : typeAnnotationType.isAnnotationPresent(Inherited.class),
				considerInterfaces);
		this.considerInherited = considerInherited;
		this.considerInterfaces = considerInterfaces;
		this.typeAnnotationType = typeAnnotationType;
		this.methodAnnotationType = methodAnnotationType;
	}

	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		boolean flag = true;
		if (this.typeAnnotationType != null) {
			flag = matchTypeAnnotation(metadataReader, metadataReaderFactory);
		}

		if (flag) {
			flag = matchMethod(metadataReader.getClassMetadata().getClassName());
		}
		return flag;

	}

	protected boolean matchTypeAnnotation(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		// This method optimizes avoiding unnecessary creation of ClassReaders
		// as well as visiting over those readers.
		if (matchSelf(metadataReader)) {
			return true;
		}
		ClassMetadata metadata = metadataReader.getClassMetadata();
		if (matchClassName(metadata.getClassName())) {
			return true;
		}

		if (this.considerInherited) {
			if (metadata.hasSuperClass()) {
				// Optimization to avoid creating ClassReader for super class.
				Boolean superClassMatch = matchSuperClass(metadata.getSuperClassName());
				if (superClassMatch != null) {
					if (superClassMatch.booleanValue()) {
						return true;
					}
				} else {
					// Need to read super class to determine a match...
					if (matchTypeAnnotation(metadataReaderFactory.getMetadataReader(metadata.getSuperClassName()),
							metadataReaderFactory)) {
						return true;
					}
				}
			}
		}

		if (this.considerInterfaces) {
			for (String ifc : metadata.getInterfaceNames()) {
				// Optimization to avoid creating ClassReader for super class
				Boolean interfaceMatch = matchInterface(ifc);
				if (interfaceMatch != null) {
					if (interfaceMatch.booleanValue()) {
						return true;
					}
				} else {
					// Need to read interface to determine a match...
					if (matchTypeAnnotation(metadataReaderFactory.getMetadataReader(ifc), metadataReaderFactory)) {
						return true;
					}
				}
			}

		}
		return false;
	}

	@Override
	protected boolean matchSelf(MetadataReader metadataReader) {
		if (this.typeAnnotationType != null) {
			AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
			return metadata.hasAnnotation(this.typeAnnotationType.getName());
		} else {
			return true;
		}
	}

	@Override
	protected Boolean matchSuperClass(String superClassName) {
		if (Object.class.getName().equals(superClassName)) {
			return Boolean.FALSE;
		} else if (superClassName.startsWith("java.")) {
			try {
				Class<?> clazz = getClass().getClassLoader().loadClass(superClassName);
				return clazz.getAnnotation(this.typeAnnotationType) != null;
			} catch (ClassNotFoundException ex) {
			}
		}
		return null;
	}

	@Override
	protected Boolean matchInterface(String superClassName) {
		if (Object.class.getName().equals(superClassName)) {
			return Boolean.FALSE;
		} else if (superClassName.startsWith("java.")) {
			try {
				Class<?> clazz = getClass().getClassLoader().loadClass(superClassName);
				return clazz.getAnnotation(this.typeAnnotationType) != null;
			} catch (ClassNotFoundException ex) {
				// Class not found - can't determine a match that way.
			}
		}
		return null;
	}

	protected Boolean matchMethod(String className) {
		if (methodAnnotationType == null) {
			return true;
		}

		try {
			Class<?> clazz = getClass().getClassLoader().loadClass(className);
			Method[] methods = clazz.getMethods();

			for (Method method : methods) {
				if (method.getAnnotation(methodAnnotationType) != null) {
					return true;
				}
			}
		} catch (ClassNotFoundException ex) {
			// Class not found - can't determine a match that way.
		}
		return false;
	}
}
