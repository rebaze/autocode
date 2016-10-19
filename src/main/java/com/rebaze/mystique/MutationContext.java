package com.rebaze.mystique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonit on 16/10/16.
 */
public class MutationContext
{
    private List<MutationRule> rules = new ArrayList<>(  );

    public void addRule( MutationBuilder builder )
    {
        rules.add(builder.build());
    }

    public void applyTo( Universe universe )
    {
        for (MutableItem path : universe.getItems())
        {
            for ( MutationRule rule : rules )
            {
                if ( rule.apply( path ) )
                {
                    // apply all in order!
                    //break;
                }
            }
        }
    }
}
