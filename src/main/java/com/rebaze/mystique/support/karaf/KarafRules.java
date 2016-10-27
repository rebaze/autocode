package com.rebaze.mystique.support.karaf;

import com.rebaze.mystique.MutationContext;

import java.util.HashMap;
import java.util.Map;

import static com.rebaze.mystique.MutationBuilder.mutation;

/**
 *  This may even go directly into gradle.build.
 */
public class KarafRules
{
    public static MutationContext karafRules(KarafParameters customization) {
        MutationContext ctx = new MutationContext();
        ctx.addRule( mutation().match( ".*" ).copy() );
        Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("productName", "Mustache");
        ctx.addRule( mutation().match( "README" ).template( "/karaf/README",customization ));

        ctx.addRule( mutation().match( "bin/karaf.*" ).rename( "karaf",customization.launcherName ) );
        ctx.addRule( mutation().match( ".*\\.DS_Store" ).ignore() );
        ctx.addRule( mutation().match( ".*\\.git" ).ignore() );
        ctx.addRule( mutation().match( "data/.*" ).ignore() );
        ctx.addRule( mutation().match( "demos/.*" ).ignore() );
        ctx.addRule( mutation().match( "lock" ).ignore() );

        return ctx;
    }
}
