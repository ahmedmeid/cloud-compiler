package nr.co.ahmedeid.cloudcomp.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import nr.co.ahmedeid.cloudcomp.CloudCompilerApplication;
import nr.co.ahmedeid.cloudcomp.builder.JavaProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.MavenProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.ZipFileHandler;

/**
 * 
 * @author ahmedeid
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CloudCompilerApplication.class)
@WebAppConfiguration
public class CloudCompilerApplicationTests {
	
	@Autowired
	ZipFileHandler zipfileHandler;
	
	@Autowired
	MavenProjectBuilder mavenBuilder;
	
	@Autowired
    JavaProjectBuilder javaProjectBuilder;

	@Test
	public void testCorruptedZipFile()
	{
		try{
			zipfileHandler.unzipFile("src/test/resources/corrupted_archive.zip", "src/test");
		}catch(Exception ex)
		{
			assert(ex.getMessage().equals("Invalid ZIP file!"));
		}
	}
	
	@Test 
	public void testExtractZipFile() throws Exception
	{
		zipfileHandler.unzipFile("src/test/resources/archive.zip", "src/test");
		File extracted = new File("src/test/test");
		assert(extracted.exists());
	}
	
	@Test
	public void testMavenProject() throws Exception
	{
		File tmp = new File("tmp");
		tmp.mkdir();	
		zipfileHandler.unzipFile("src/test/resources/maven_project.zip", "tmp");
		mavenBuilder.buildMavenProject(new File("tmp/maven_project/pom.xml"));
		FileUtils.deleteDirectory(tmp);
	}
	
	@Test 
	public void testJavaProjectSuccess() throws Exception
	{
		File tmp = new File("tmp");
		tmp.mkdir();
		zipfileHandler.unzipFile("src/test/resources/java_project.zip", "tmp");
		javaProjectBuilder.compile("tmp/java_project/src", "tmp/java_project/src/classes");
		FileUtils.deleteDirectory(tmp);
	}
	

}
