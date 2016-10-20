package om.rebaze.mystique.support.karaf;

import com.rebaze.mystique.LocalUniverse;
import com.rebaze.mystique.MutationContext;
import com.rebaze.mystique.Mystique;
import com.rebaze.mystique.support.karaf.KarafParameters;
import com.rebaze.mystique.support.karaf.KarafRules;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class KarafDistroTest
{
    @Test public void testSingle() throws IOException
    {
        // Input
        KarafParameters karafCustom = new KarafParameters();
        karafCustom.launcherName = "mylauncher";

        MutationContext karafRules = KarafRules.karafRules( karafCustom );
        LocalUniverse universe = new LocalUniverse( new File( "/Users/tonit/cardtech/payengine/apache-karaf-4.0.7/" ) );
        karafRules.applyTo( universe );

        // Materialize to disk
        Mystique mystique = new Mystique( new File( "target/customKaraf" ) );
        mystique.materialize( universe );

        // assume changes:
        File out = new File( "target/customKaraf/bin/mylauncher" );
        assertTrue( "Launcher should exist", out.exists() );

    }
}
