import groovy.io.FileType;
import java.io.File;

@NonCPS
def call(Map config=[:]){
	def dir = new File(pwd());
    
	new File(dir.path + '/releasenotes.txt').withWriter('utf-8') 
	{ 
		writer -> 
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