package nr.co.ahmedeid.cloudcomp.builder;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * 
 * @author ahmedeid
 *
 */
public class ZipFileHandler {
	
	/**
	 * 
	 * @param zipFileName
	 * @throws Exception
	 */
	public void verifyFile(String zipFileName) throws Exception
	{
        ZipFile zipFile = new ZipFile(zipFileName);
		
		//check if the zip file is valid
		if(!zipFile.isValidZipFile())
		{
			throw new Exception("Invalid ZIP file!");
		}
	}
	
	/**
	 * 
	 * @param zipFileName
	 * @param destinationDirName
	 * @throws Exception 
	 */
	public void unzipFile(String zipFileName, String destinationDirName) throws Exception
	{
		verifyFile(zipFileName);
		
		File outputDir = new File(destinationDirName);
		outputDir.mkdir();
		
		ZipFile zipFile = new ZipFile(zipFileName);
		
		//Extract from zip file
		zipFile.extractAll(destinationDirName);	
	}
	
	/**
	 * 
	 * @param sourceDirName
	 * @param zipFileName
	 * @throws ZipException
	 */
	public void createZipFile(String sourceDirName, String zipFileName) throws ZipException
	{
		ZipFile zipFile = new ZipFile(zipFileName);
		ZipParameters params = new ZipParameters();
		params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		params.setIncludeRootFolder(false);
		zipFile.addFolder(sourceDirName, params);
		
	}

}
