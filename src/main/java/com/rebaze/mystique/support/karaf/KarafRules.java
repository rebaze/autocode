package com.rebaze.mystique.support.karaf;

import com.rebaze.mystique.MutationContext;

import static com.rebaze.mystique.MutationBuilder.mutation;

/**
 *  This may even go directly into gradle.build.
 */
public class KarafRules
{
    public static MutationContext karafRules(KarafParameters customization) {
        MutationContext ctx = new MutationContext();
        ctx.addRule( mutation().match( ".*" ).copy() );
        ctx.addRule( mutation().match( ".*/bin/karaf" ).rename( "karaf",customization.launcherName ) );
        ctx.addRule( mutation().match( ".*\\.DS_Store" ).ignore() );
        ctx.addRule( mutation().match( ".*\\.git" ).ignore() );
        return ctx;
    }
}
