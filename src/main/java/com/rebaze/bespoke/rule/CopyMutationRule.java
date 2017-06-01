package com.rebaze.bespoke.rule;

import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationRule;

/**
 * Created by tonit on 16/10/16.
 */
public class CopyMutationRule implements MutationRule
{

    @Override public boolean apply( MutableItem item )
    {
        item.setTargetPath(item.getSourcePath());
        return true;
    }
}
