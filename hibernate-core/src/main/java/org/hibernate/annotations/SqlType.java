/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

import org.hibernate.resource.beans.spi.ManagedBean;
import org.hibernate.resource.beans.spi.ManagedBeanRegistry;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Allows specifying the specific {@link org.hibernate.type.descriptor.sql.SqlTypeDescriptor}
 * to use for a particular column mapping.  Can reference a {@link ManagedBean} - see
 * {@link ManagedBeanRegistry}
 *
 * ````
 * @Entity
 * class User {
 *     ...
 *     @SqlType ( MyCustomSqlIntegerDescriptor.class )
 *     int getAge() { ... }
 *
 *     @SqlType ( MyCustomSqlVarcharDescriptor.class )
 *     String getName() { ... }
 * }
 * ````
 *
 * @apiNote Should not be used in combination with {@link SqlTypeCode}
 *
 * @see org.hibernate.type.descriptor.sql.SqlTypeDescriptor
 * @see org.hibernate.type.descriptor.sql.spi.SqlTypeDescriptorRegistry
 *
 * @author Steve Ebersole
 */
@java.lang.annotation.Target({METHOD, FIELD, ANNOTATION_TYPE})
@Inherited
@Retention(RUNTIME)
public @interface SqlType {
	Class<? extends SqlTypeDescriptor> value();
}