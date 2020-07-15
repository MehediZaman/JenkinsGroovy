import groovy.io.FileType;
import java.io.File;
import java.util.Calendar.*;
import java.text.SimpleDateFormat
import hudson.model.*

@NonCPS
def call(Map config=[:])
{
	def dir = new File(pwd());

	new File(dir.path + '/releasenotes.txt').withWriter('utf-8') 
	{ 
		writer -> 
		
		def now = new Date();
		def fmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		writer.writeLine("Date and Time IS: " + fmt.format(now));

		writer.writeLine("Build Number is: ${BUILD_NUMBER}");

		if (config.changes != "false"){
			def changeLogSets = currentBuild.changeSets;

			for (change in changeLogSets) {
				def entries = change.items;
				for (entry in entries) {
					writer.writeLine("${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}");
					for (file in entry.affectedFiles) {
						writer.writeLine("${file.editType.name} ${file.path}");
					}
				}
			}
		}
		
		dir.eachFileRecurse(FileType.ANY){ file ->
			if (file.isDirectory()){
				writer.writeLine(file.name);            
			}
			else
			{
				writer.writeLine('\t' + file.name + '\t' + file.length());
			}		
		} 
	}
}