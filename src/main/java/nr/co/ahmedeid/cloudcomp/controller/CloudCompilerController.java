package nr.co.ahmedeid.cloudcomp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import nr.co.ahmedeid.cloudcomp.builder.AntProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.JavaProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.MavenProjectBuilder;
import nr.co.ahmedeid.cloudcomp.builder.ZipFileHandler;

/**
 * 
 * @author ahmedeid
 *
 */
@Controller
public class CloudCompilerController {
	
	Logger logger = Logger.getLogger(CloudCompilerController.class);
	
	@Autowired
	ZipFileHandler zipFileHandler;
	
	@Autowired 
	JavaProjectBuilder javaCompiler;
	
	@Autowired
	MavenProjectBuilder mavenBuilder;
	
	@Autowired
	AntProjectBuilder antBuilder;
    
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
    
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model){
            try {
            	String filename = file.getOriginalFilename();
            	logger.info("uploading file:"+filename);
            	
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =  new BufferedOutputStream(new FileOutputStream(new File(filename)));
                stream.write(bytes);
                stream.close();      
                
                logger.info("file:"+filename+" has been uploaded");
                
                logger.info("verifying file:"+filename);
                
                zipFileHandler.verifyFile(filename);
                
                logger.info("file:"+filename+" is a valid zip file");
                
                logger.info("checking for old processig directory...");
                File inputDir = new File("input");
                if(inputDir.exists())
                {
                  FileUtils.deleteDirectory(inputDir);
                  logger.info("old directory has been deleted");
                }
                
                inputDir.mkdir();
                
                logger.info("new directory has been created");
                
                logger.info("unzipping file:"+filename);
                zipFileHandler.unzipFile(filename, "input");
                
                String zipContents[] = inputDir.list();
                
                logger.info("contents:"+zipContents[0]);
                if(zipContents.length==1)
                {
                	String projectDirname = "input"+File.separator+zipContents[0];
                	String projectType = "java";
                	File mavenPom = new File(projectDirname+File.separator+"pom.xml");
                	if(mavenPom.exists())
                	{
                		 logger.info("it is a maven project");
                		 projectType = "maven";
                		 logger.info("running maven test");
                		 String testResults = mavenBuilder.buildMavenProject(mavenPom);
                		 model.addAttribute("result", testResults);
                		 logger.info("zipping output...");
                		 String outputFilename = zipContents[0]+"-binary.zip";
                		 String target = projectDirname+File.separator+"target";
                	     zipFileHandler.createZipFile(target, outputFilename);
                         model.addAttribute("filename", outputFilename); 
                		 
                	}
                	File antBuild = new File(projectDirname+File.separator+"build.xml");
                	if(antBuild.exists())
                	{
                		logger.info("it is an ant project");
                		projectType = "ant";
                		antBuilder.buildAnt(antBuild);
                	}
                	if(projectType.equals("java"))
                	{
                		logger.info("this is a java project");
                		String src= projectDirname+File.separator+"src";
                		String classes = projectDirname+File.separator+"classes";
                		javaCompiler.compile(src, classes);
                		logger.info("zipping output...");
                		String outputFilename = zipContents[0]+"-binary.zip";
                		zipFileHandler.createZipFile(classes, outputFilename);
                		model.addAttribute("filename", outputFilename);
                		model.addAttribute("result", "project has been compiled successfully!");             		
                	}
                	model.addAttribute("project_type", projectType+" project");
                }
               
            } catch (Exception e) {
            	 model.addAttribute("result", e.getMessage());
            	 e.printStackTrace();
            	 return "home";
            }
    
             
        	 return "home";
        
    }
    
    @RequestMapping(value="/files/download", method=RequestMethod.GET, produces = "application/zip")
    @ResponseBody
    public FileSystemResource downloadFile(@RequestParam(value="filename") String filename, HttpServletResponse response) {
    	response.setHeader( "Content-Disposition", "attachment;filename=" + filename );
        return new FileSystemResource(new File(filename));
    }

}
