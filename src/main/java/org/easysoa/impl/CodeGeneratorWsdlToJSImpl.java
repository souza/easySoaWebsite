package org.easysoa.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.cxf.tools.common.ToolContext;
import org.apache.cxf.tools.wsdlto.WSDLToJava;
import org.easysoa.api.CodeGenerator;
import org.easysoa.api.ServiceManager;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.component.factory.api.MembraneGeneration;
import org.ow2.frascati.factory.WebServiceCommandLine;
import org.ow2.frascati.wsdl.WsdlCompiler;

public class CodeGeneratorWsdlToJSImpl implements CodeGenerator {

	@Reference
	protected WsdlCompiler wsdlCompiler;
	@Reference
	protected ServiceManager serviceManager;
	@Reference
	protected MembraneGeneration membraneGeneration;
	

	@Override
	public void generate(String service, String port, String wsdlLocation) {
		System.out.println("generate");
		this.compile(wsdlLocation);
		String generatedPackage = serviceManager.getCurrentApplication().getPackageName()+".impl.generated";
		try {
			ClassLoader cl = membraneGeneration.compileJavaSource();
			Class<?> interfaceClass = cl.loadClass(generatedPackage+"."+port);

			File jsFile = this.createJSFile(service);
			
			FileWriter fw = new FileWriter(jsFile, false);
			BufferedWriter writer = new BufferedWriter(fw);
			
			for(Method method : interfaceClass.getMethods()){
				writer.write("function "+method.getName()+"(");
				int paramIndex = 1;
				for(Class<?> params : method.getParameterTypes()){
					if(paramIndex == 1) writer.write("param"+paramIndex);
					else writer.write(",param"+paramIndex);
					paramIndex++;
				}
				writer.write("){"+System.getProperty("line.separator"));
				writer.write("wsdl-reference."+method.getName()+"(");
				paramIndex = 1;
				for(Class<?> params : method.getParameterTypes()){
					if(paramIndex == 1) writer.write("param"+paramIndex);
					else writer.write(",param"+paramIndex);
					paramIndex++;
				}
				writer.write(");"+System.getProperty("line.separator")+"}"+System.getProperty("line.separator")+System.getProperty("line.separator"));
			}
			writer.flush();
			writer.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private File createJSFile(String service) {
		try {
			String resources = serviceManager.getCurrentApplication()
					.getResources();
			File scriptDir = new File(resources + File.separator + "scripts");
			scriptDir.mkdirs();
			File scriptFile = new File(scriptDir + File.separator + service
					+ ".js");
			scriptFile.createNewFile();
			return scriptFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void compile(String wsdlLocation) {
		System.out.println("compile");
		try {
			if (!wsdlLocation.startsWith("http://")) {
				wsdlLocation = "http://" + wsdlLocation;
			}
			WebServiceCommandLine cmd = new WebServiceCommandLine("wsdl2java");

			String[] args = new String[4];
			args[0] = "-u";
			args[1] = wsdlLocation;
			args[2] = "-o";
			String outputDirectory = serviceManager.getCurrentApplication()
					.getSources().substring(0, serviceManager.getCurrentApplication()
							.getSources().indexOf(serviceManager.getCurrentApplication().getPackageName().replace(".", File.separator)));
			args[3] = outputDirectory;
			cmd.parse(args);
			System.out.println("After parsing");

			String outputDir = cmd.getOutputDir();
			File wsdlF = cmd.getWsdlFile();
			URL wsdlUrl = cmd.getWsdlUrl();

			if ((wsdlF == null) && (wsdlUrl == null)) {
				System.err.println("Please set the WSDL file/URL to parse");
			}
			if ((wsdlF != null) && (wsdlUrl != null)) {
				System.err
						.println("Please choose either a WSDL file OR an URL to parse (not both!).");
			}

			System.out.println("  output directory : " + outputDir);
			String wsdl = (wsdlF == null ? wsdlUrl.toString() : wsdlF
					.getAbsolutePath());
			String[] params = new String[] { "-d", outputDirectory, wsdl };
			ToolContext toolContext = new ToolContext();
			toolContext.setPackageName(serviceManager.getCurrentApplication().getPackageName()+".impl.generated");
			new WSDLToJava(params).run(toolContext);
			System.out.println("Java code successfully generated!\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
