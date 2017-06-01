package com.rebaze.bespoke.rule;

import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationRule;

/**
 * Created by tonit on 18/10/16.
 */
public class RenameTargetPathRule implements MutationRule
{
    private final String rename;
    private final String pattern;

    public RenameTargetPathRule( String pattern, String rename )
    {
        this.pattern = pattern;
        this.rename = rename;
    }

    @Override public boolean apply( MutableItem item )
    {
        if (item.getSourcePath() == null) {
            return false;
        }
        item.setTargetPath( item.getTargetPath().replaceAll( pattern,rename ));
        return true;
    }
}
