/**
 * OW2 FraSCAti
 * Copyright (C) 2011 INRIA, University of Lille 1
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: frascati@ow2.org
 *
 * Author: Nicolas Petitprez
 *
 * Contributor(s):
 *
 */
package org.easysoa.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.osoa.sca.annotations.Destroy;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Property;
import org.osoa.sca.annotations.Scope;

@Scope("COMPOSITE")
public class EntityManagerProviderImpl implements Provider<EntityManager> {
	private EntityManagerFactory entityManagerFactory;

	@Property(required = true)
	public String persistenceUnitName;

	@Destroy
	public void destroy() {
		entityManagerFactory.close();
	}

	public EntityManager get() {
		return entityManagerFactory.createEntityManager();
	}

	@Init
	public void setup() {
		entityManagerFactory = Persistence
				.createEntityManagerFactory(persistenceUnitName);
	}
}
