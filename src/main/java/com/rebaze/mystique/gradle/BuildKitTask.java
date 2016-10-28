package com.rebaze.mystique.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.tasks.TaskAction;
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

        for ( ResolvedArtifact d : c2.getResolvedArtifacts()) {
            System.out.println("Resolved" + d.getFile().getAbsolutePath());
            File out = new File(getProject().getBuildDir(),kit.getName());
            Archiver archiver = ArchiverFactory.createArchiver("zip");
            archiver.extract(d.getFile(), out);


        }


    }
}
