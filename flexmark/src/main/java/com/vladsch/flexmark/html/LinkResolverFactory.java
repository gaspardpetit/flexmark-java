package com.vladsch.flexmark.html;

import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import java.util.function.Function;
import com.vladsch.flexmark.util.dependency.Dependent;

import java.util.Set;

public interface LinkResolverFactory extends Function<LinkResolverContext, LinkResolver>, Dependent<LinkResolverFactory> {
    @Override
    Set<Class<? extends LinkResolverFactory>> getAfterDependents();

    @Override
    Set<Class<? extends LinkResolverFactory>> getBeforeDependents();

    @Override
    boolean affectsGlobalScope();

    @Override
    LinkResolver apply(LinkResolverContext context);
}
