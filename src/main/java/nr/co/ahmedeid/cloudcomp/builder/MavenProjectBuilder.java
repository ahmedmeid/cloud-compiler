package nr.co.ahmedeid.cloudcomp.builder;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

/**
 * 
 * @author ahmedeid
 *
 */
public class MavenProjectBuilder {
	
	Logger logger = Logger.getLogger(MavenProjectBuilder.class);
	
	/**
	 * 
	 * @param mavenPomFile
	 * @return
	 * @throws MavenInvocationException
	 */
	public String buildMavenProject(File mavenPomFile) throws MavenInvocationException
	{
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(mavenPomFile);
		request.setGoals(Arrays.asList("test", "compile"));
 		Invoker invoker = new DefaultInvoker();
 		invoker.setMavenHome(new File(System.getenv("M3_HOME")));
 		final StringBuffer buff = new StringBuffer("");
 		InvocationOutputHandler outputHandler = new InvocationOutputHandler() {
			@Override
			public void consumeLine(String arg0) {
				
				logger.info(arg0);
				if(arg0.startsWith("Test"))
				{
				  buff.append(arg0);
				}
				buff.append("\n");
			}
		};
		invoker.setOutputHandler(outputHandler);
        invoker.execute(request);
		return buff.toString();
	}

}
