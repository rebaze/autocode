package com.rebaze.mystique;

import com.rebaze.mystique.rule.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonit on 15/10/16.
 */
public class MutationBuilder
{

    private List<MutationRule> ruleStack = new ArrayList<>(  );

    public static MutationBuilder mutation() {
        return new MutationBuilder();
    }

    public MutationBuilder match( String pattern )
    {
        ruleStack.add( new RegexMatchRule( pattern ) );
        return this;
    }


    public MutationBuilder copy( )
    {
        ruleStack.add(new CopyMutationRule( ));
        return this;
    }

    public MutationBuilder ignore()
    {
        ruleStack.add( new IgnoreMutationRule( ));
        return this;

    }

    public MutationBuilder modify( String origin, String replacement )
    {
        ruleStack.add( new ReplaceInFileMutationRule( origin, replacement ));
        return this;
    }

    public MutationRule build() {
        return new SequentialMutationRule(ruleStack);
    }

    public MutationBuilder rename( String pattern, String rename )
    {
        ruleStack.add( new RenameTargetPathRule( pattern, rename ));
        return this;
    }

    public MutationBuilder replace( String path )
    {
        ruleStack.add(new LoadFromClasspathRule(path));
        return this;
    }
}
