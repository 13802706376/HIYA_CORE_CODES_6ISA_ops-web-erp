package com.yunnex.ops.erp.modules.workflow.flow.strategy;

@FunctionalInterface
public interface IFunction<T, U> {
    Boolean group(T t, U u);
}
