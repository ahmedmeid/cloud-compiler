package nr.co.ahmedeid.cloudcomp.builder;

import java.io.File;


import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
/**
 * 
 * @author ahmedeid
 *
 */
public class AntProjectBuilder {

	/**
	 * 
	 * @param antBuildFile
	 */
	public void buildAnt(File antBuildFile)
	{
		   Project p = new Project();
		   p.setUserProperty("ant.file", antBuildFile.getAbsolutePath());
		   p.init();
		   ProjectHelper helper = ProjectHelper.getProjectHelper();
		   p.addReference("ant.projectHelper", helper);
		   helper.parse(p, antBuildFile);
		   p.executeTarget(p.getDefaultTarget());
	}
}
