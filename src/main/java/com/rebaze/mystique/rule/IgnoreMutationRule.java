package com.rebaze.mystique.rule;

import com.rebaze.mystique.MutableItem;
import com.rebaze.mystique.MutationRule;

/**
 * Created by tonit on 16/10/16.
 */
public class IgnoreMutationRule implements MutationRule
{

    @Override public boolean apply( MutableItem item )
    {
        item.setTargetPath( null );
        return true;
    }
}
