package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class CompositeFlowInfoTask extends FlowInfoTask {

    private int i = 0;

    protected List<FlowInfoTask> equipment = new ArrayList<FlowInfoTask>();

    public boolean add(FlowInfoTask equipment) {
        return this.equipment.add(equipment);
    }

    @SuppressWarnings("rawtypes")
    public Iterator iter() {
        return equipment.iterator();
    }


    public boolean hasNext() {
        return i < equipment.size();
    }

    public Object next() {
        if (hasNext())
            return equipment.get(i++);
        else
            throw new NoSuchElementException();
    }

    public List<FlowInfoTask> getEquipment() {
        return equipment;
    }

}
