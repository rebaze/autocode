package com.rebaze.mystique.rule;

import com.rebaze.mystique.MutableItem;
import com.rebaze.mystique.MutationRule;

import java.util.List;

/**
 * Created by tonit on 18/10/16.
 */
public class SequentialMutationRule implements MutationRule
{

    private final List<MutationRule> list;

    public SequentialMutationRule( List<MutationRule> stack )
    {
        list = stack;
    }

    @Override public boolean apply( MutableItem item )
    {
        for (MutationRule rule: list) {
            if(!rule.apply( item )) {
                return false;
            }
        }
        return true;
    }
}
