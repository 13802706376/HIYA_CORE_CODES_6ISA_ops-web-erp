package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import java.util.List;

/**
 * 
 * @author Ejon
 * @date 2018年5月26日
 */
public interface IGroup<T, A>
{

    List<A> group(List<T> list);
}
