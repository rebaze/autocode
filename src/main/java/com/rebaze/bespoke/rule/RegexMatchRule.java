package com.rebaze.bespoke.rule;

import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationRule;

import java.util.regex.Pattern;

/**
 * Created by tonit on 18/10/16.
 */
public class RegexMatchRule implements MutationRule
{
    private final Pattern pattern;

    public RegexMatchRule( String pattern )
    {
        this.pattern = Pattern.compile(pattern);
    }

    @Override public boolean apply( MutableItem item )
    {
        return pattern.matcher( item.getSourcePath()).matches();
    }
}
