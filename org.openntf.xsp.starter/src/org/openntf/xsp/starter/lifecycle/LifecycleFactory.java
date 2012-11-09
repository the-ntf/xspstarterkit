package org.openntf.xsp.starter.lifecycle;

import java.util.Iterator;

import javax.faces.lifecycle.Lifecycle;

import org.openntf.xsp.starter.Activator;

public class LifecycleFactory extends javax.faces.lifecycle.LifecycleFactory {
	public static final String STARTER_LIFECYCLE_ID = "StarterLifecycle";
	private javax.faces.lifecycle.LifecycleFactory delegate;
	private final static boolean _debug = Activator._debug;
	static {
		if (_debug)
			System.out.println(LifecycleFactory.class.getName() + " loaded");
	}

	public LifecycleFactory(javax.faces.lifecycle.LifecycleFactory defaultFactory) {
		delegate = defaultFactory;

		Lifecycle defaultLifecycle = delegate.getLifecycle(javax.faces.lifecycle.LifecycleFactory.DEFAULT_LIFECYCLE);

		addLifecycle(STARTER_LIFECYCLE_ID, new org.openntf.xsp.starter.lifecycle.Lifecycle(defaultLifecycle));
		if (_debug)
			System.out.println(getClass().getName() + " created");
	}

	@Override
	public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
		delegate.addLifecycle(lifecycleId, lifecycle);
	}

	@Override
	public Lifecycle getLifecycle(String lifecycleId) {
		String localId = lifecycleId;
		if (javax.faces.lifecycle.LifecycleFactory.DEFAULT_LIFECYCLE.equals(lifecycleId)) {
			localId = STARTER_LIFECYCLE_ID;
		}
		return delegate.getLifecycle(localId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<String> getLifecycleIds() {
		return delegate.getLifecycleIds();
	}
}
