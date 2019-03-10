/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.tree.spi.from;

import org.hibernate.metamodel.model.relational.spi.Column;
import org.hibernate.query.NavigablePath;
import org.hibernate.sql.ast.consume.spi.SqlAppender;
import org.hibernate.sql.ast.consume.spi.SqlAstWalker;
import org.hibernate.sql.ast.produce.metamodel.spi.AbstractColumnReferenceQualifier;
import org.hibernate.sql.ast.tree.spi.expression.ColumnReference;
import org.hibernate.sql.ast.tree.spi.expression.domain.NavigableReference;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractTableGroup
		extends AbstractColumnReferenceQualifier
		implements TableGroup {

	private final NavigablePath navigablePath;

	public AbstractTableGroup(String uid, NavigablePath navigablePath) {
		super( uid );
		this.navigablePath = navigablePath;
	}

	@Override
	public NavigablePath getNavigablePath() {
		return navigablePath;
	}

	@Override
	public NavigableReference getNavigableReference() {
		throw new UnsupportedOperationException();
	}

	protected void renderTableReference(TableReference tableBinding, SqlAppender sqlAppender, SqlAstWalker walker) {
		final String identificationVariable = tableBinding.getIdentificationVariable();
		String aliasString = "";
		if ( identificationVariable != null ) {
			aliasString = " as " + identificationVariable;
		}
		sqlAppender.appendSql(
				tableBinding.getTable().render(
						walker.getSessionFactory().getDialect(),
						walker.getSessionFactory().getJdbcServices().getJdbcEnvironment()
				) + aliasString
		);
	}

	@Override
	public ColumnReference locateColumnReferenceByName(String name) {
		Column column = getPrimaryTableReference().getTable().getColumn( name );
		if ( column == null ) {
			for ( TableReferenceJoin join : getTableReferenceJoins() ) {
				column = join.getJoinedTableReference().getTable().getColumn( name );
				if ( column != null ) {
					break;
				}
			}
		}

		if ( column == null ) {
			return null;
		}

		return resolveColumnReference( column );
	}


	@Override
	public String toLoggableFragment() {
		final StringBuilder buffer = new StringBuilder( "(" );

		buffer.append( "path=(" ).append( getNavigableReference().getNavigablePath().toLoggableFragment() ).append( "), " );
		buffer.append( "root=(" ).append( getPrimaryTableReference().toLoggableFragment() ).append( "), " );
		buffer.append( "joins=[" );
		if ( getTableReferenceJoins() != null ) {
			boolean firstPass = true;
			for ( TableReferenceJoin tableReferenceJoin : getTableReferenceJoins() ) {
				if ( firstPass ) {
					firstPass = false;
				}
				else {
					buffer.append( ',' );
				}

				buffer.append( tableReferenceJoin.toLoggableFragment() );
			}
		}

		return buffer.append( "])" ).toString();
	}
}