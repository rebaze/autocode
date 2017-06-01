package om.rebaze.mystique.support.karaf;

import com.rebaze.bespoke.*;
import com.rebaze.bespoke.support.karaf.KarafParameters;
import com.rebaze.bespoke.support.karaf.KarafRules;
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
        karafCustom.productName = "My Distro";

        MutationContext karafRules = KarafRules.karafRules( karafCustom );
        LocalUniverse universe = new LocalUniverse( new File( "/Users/tonit/cardtech/payengine/apache-karaf-4.0.7/" ) );
        karafRules.applyTo( universe );

        // Materialize to disk
        Materializer mat = new LocalCopyingMaterializer( new File( "target/customKaraf" ), true );

        mat.materialize( universe );

        // assume changes:
        File out = new File( "target/customKaraf/bin/mylauncher" );
        assertTrue( "Launcher should exist", out.exists() );

    }


}
