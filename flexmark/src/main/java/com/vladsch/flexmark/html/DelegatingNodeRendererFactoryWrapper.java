package com.vladsch.flexmark.html;

import com.vladsch.flexmark.html.renderer.DelegatingNodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import java.util.function.Function;
import com.vladsch.flexmark.util.dependency.Dependent;
import com.vladsch.flexmark.util.options.DataHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Factory for instantiating new node renderers with dependencies
 */
class DelegatingNodeRendererFactoryWrapper implements Function<DataHolder, NodeRenderer>, Dependent<DelegatingNodeRendererFactoryWrapper>, DelegatingNodeRendererFactory {
    private final NodeRendererFactory nodeRendererFactory;
    private List<DelegatingNodeRendererFactoryWrapper> nodeRenderers;
    private Set<Class> myDelegates = null;

    public DelegatingNodeRendererFactoryWrapper(List<DelegatingNodeRendererFactoryWrapper> nodeRenderers, NodeRendererFactory nodeRendererFactory) {
        this.nodeRendererFactory = nodeRendererFactory;
        this.nodeRenderers = nodeRenderers;
    }

    @Override
    public NodeRenderer apply(DataHolder options) {
        return nodeRendererFactory.apply(options);
    }

    public NodeRendererFactory getFactory() {
        return nodeRendererFactory;
    }

    @Override
    public Set<Class<? extends NodeRendererFactory>> getDelegates() {
        return nodeRendererFactory instanceof DelegatingNodeRendererFactory ? ((DelegatingNodeRendererFactory) nodeRendererFactory).getDelegates() : null;
    }

    @Override
    public final Set<? extends Class> getAfterDependents() {
        return null;
    }

    @Override
    public Set<? extends Class> getBeforeDependents() {
        if (myDelegates == null && nodeRenderers != null) {
            Set<Class<? extends NodeRendererFactory>> delegates = getDelegates();
            if (delegates != null) {
                myDelegates = new HashSet<Class>();
                for (DelegatingNodeRendererFactoryWrapper factory : nodeRenderers) {
                    if (delegates.contains(factory.getFactory().getClass())) {
                        myDelegates.add(factory.getFactory().getClass());
                    }
                }
            }

            // release reference
            nodeRenderers = null;
        }
        return myDelegates;
    }

    @Override
    public final boolean affectsGlobalScope() {
        return false;
    }
}
