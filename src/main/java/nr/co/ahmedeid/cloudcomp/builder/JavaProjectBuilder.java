package nr.co.ahmedeid.cloudcomp.builder;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import nr.co.ahmedeid.cloudcomp.exception.CompilationExcption;

/**
 * 
 * @author ahmedeid
 *
 */
public class JavaProjectBuilder {

/**
 * 
 * @param sourceDirName
 * @param classesDirName
 * @throws CompilationExcption
 */
public void compile(String sourceDirName, String classesDirName) throws CompilationExcption { 
		
		File sourceDir = new File(sourceDirName);
		File classesDir = new File(classesDirName);
		classesDir.mkdir();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); 
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>(); 
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null); 
        List<JavaFileObject> javaObjects = scanRecursivelyForJavaObjects(sourceDir, fileManager); 
         
        if (javaObjects.size() == 0) { 
            throw new CompilationExcption("There are no source files to compile in " + sourceDir.getAbsolutePath()); 
        } 
        String[] compileOptions = new String[]{"-d", classesDir.getAbsolutePath()} ; 
        Iterable<String> compilationOptions = Arrays.asList(compileOptions); 
         
        CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, javaObjects) ; 
         
        if (!compilerTask.call()) { 
        	String error = null;
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) { 
               error = String.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic); 
            } 
            throw new CompilationExcption("Could not compile project: \n"+error); 
        } 
    } 
 

    private List<JavaFileObject> scanRecursivelyForJavaObjects(File dir, StandardJavaFileManager fileManager) { 
        List<JavaFileObject> javaObjects = new LinkedList<JavaFileObject>(); 
        File[] files = dir.listFiles(); 
        for (File file : files) { 
            if (file.isDirectory()) { 
                javaObjects.addAll(scanRecursivelyForJavaObjects(file, fileManager)); 
            } 
            else if (file.isFile() && file.getName().toLowerCase().endsWith(".java")) { 
                javaObjects.add(readJavaObject(file, fileManager)); 
            } 
        } 
        return javaObjects; 
    } 
    
    private JavaFileObject readJavaObject(File file, StandardJavaFileManager fileManager) { 
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file); 
        Iterator<? extends JavaFileObject> it = javaFileObjects.iterator(); 
        if (it.hasNext()) { 
            return it.next(); 
        } 
        throw new RuntimeException("Could not load " + file.getAbsolutePath() + " java file object"); 
    } 

}
