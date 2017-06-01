package com.rebaze.bespoke.gradle;

import com.rebaze.bespoke.LocalCopyingMaterializer;
import com.rebaze.bespoke.LocalUniverse;
import com.rebaze.bespoke.Materializer;
import com.rebaze.bespoke.MutationContext;
import com.rebaze.bespoke.support.karaf.KarafParameters;
import com.rebaze.bespoke.support.karaf.KarafRules;
import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.org.apache.maven.artifact.DefaultArtifact;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;

/**
 * Created by tonit on 28/10/16.
 */
public class BuildKitTask extends DefaultTask
{
    @TaskAction
    public void build() throws Exception {
        Configuration conf = getProject().getConfigurations().getByName( "kit" );
        MystiqueExtension kit = ( MystiqueExtension ) getProject().getExtensions().getByName( "kit" );
        System.out.println("Name :" + kit.getName());
        System.out.println("Launcher Name :" + kit.getLauncherName());


        System.out.println("Artifacts to publish: " + conf.getAllArtifacts().size() );
        System.out.println("Dependencies: " + conf.getDependencies().size());
        for ( Dependency d : conf.getDependencies()) {
            System.out.println("Dependency" + d);
        }

        ResolvedConfiguration c2 = conf.getResolvedConfiguration();
        File out = new File(getProject().getBuildDir(),kit.getName());
        String version = "";
        for ( ResolvedArtifact d : c2.getResolvedArtifacts()) {
            version = d.getModuleVersion().getId().getVersion();
            System.out.println("Resolved" + d.getFile().getAbsolutePath());
            Archiver archiver = ArchiverFactory.createArchiver(d.getFile());
            archiver.extract(d.getFile(), out);
            // fix single out:
            if (out.listFiles().length == 1) {
                out = out.listFiles()[0];
            }
        }

        KarafParameters karafCustom = new KarafParameters();
        karafCustom.launcherName = kit.getLauncherName();
        karafCustom.productName = kit.getName();

        MutationContext karafRules = KarafRules.karafRules( karafCustom );
        LocalUniverse universe = new LocalUniverse( out );
        karafRules.applyTo( universe );

        // Materialize to disk
        File outFinal = new File(getProject().getBuildDir(),kit.getName() + "_out");

        Materializer mat = new LocalCopyingMaterializer( outFinal, true );
        mat.materialize( universe );
        // pack it up again:
        String archiveName = kit.getName() + "-" + version;
        File destination = getProject().getBuildDir();
        File source = outFinal;

        Archiver archiver = ArchiverFactory.createArchiver( "zip" );
        File archive = archiver.create(archiveName, destination, source);
        System.out.println("Written: " + archive.getAbsolutePath());

        // add to publish artifacts:
        DefaultArtifact artifact = new DefaultArtifact( getProject().getGroup().toString(),getProject().getName(),getProject().getVersion().toString(),null,"assembly","tar.gz",null );
        artifact.setFile( archive );
        getProject().getArtifacts().add( "",artifact );
        System.out.println("Set publish: " + artifact);


    }
}
