package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ConverterGenerator {
	
	static final String stringTo = 	"package converters;\r\n" + 
									"\r\n" + 
									"import org.apache.commons.lang.StringUtils;\r\n" + 
									"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
									"import org.springframework.core.convert.converter.Converter;\r\n" + 
									"import org.springframework.stereotype.Component;\r\n" + 
									"import org.springframework.transaction.annotation.Transactional;\r\n" + 
									"\r\n" + 
									"import domain.%s;\r\n" + 
									"import repositories.%sRepository;\r\n" + 
									"\r\n" + 
									"@Component\r\n" + 
									"@Transactional\r\n" + 
									"public class StringTo%sConverter implements Converter<String, %s> {\r\n" + 
									"\r\n" + 
									"	@Autowired\r\n" + 
									"	%sRepository %sRepository;\r\n" + 
									"\r\n" + 
									"	@Override\r\n" + 
									"	public %s convert(String text) {\r\n" + 
									"		%s result;\r\n" + 
									"		int id;\r\n" + 
									"\r\n" + 
									"		try {\r\n" + 
									"			if (StringUtils.isEmpty(text)) {\r\n" + 
									"				result = null;\r\n" + 
									"			} else {\r\n" + 
									"				id = Integer.valueOf(text);\r\n" + 
									"				result = %sRepository.findOne(id);\r\n" + 
									"			}\r\n" + 
									"		} catch (Throwable oops) {\r\n" + 
									"			throw new IllegalArgumentException(oops);\r\n" + 
									"		}\r\n" + 
									"		return result;\r\n" + 
									"	}\r\n" + 
									"}";
	
	static final String entityTo = 	"package converters;\r\n" + 
									"\r\n" + 
									"import org.springframework.core.convert.converter.Converter;\r\n" + 
									"import org.springframework.stereotype.Component;\r\n" + 
									"import org.springframework.transaction.annotation.Transactional;\r\n" + 
									"\r\n" + 
									"import domain.%s;\r\n" + 
									"\r\n" + 
									"@Component\r\n" + 
									"@Transactional\r\n" + 
									"public class %sToStringConverter implements Converter<%s, String> {\r\n" + 
									"\r\n" + 
									"	@Override\r\n" + 
									"	public String convert(%s %s) {\r\n" + 
									"		String result;\r\n" + 
									"\r\n" + 
									"		if (%s == null) {\r\n" + 
									"			result = null;\r\n" + 
									"		} else {\r\n" + 
									"			result = String.valueOf(%s.getId());\r\n" + 
									"		}\r\n" + 
									"		return result;\r\n" + 
									"	}\r\n" + 
									"}";
	
	static final String dstFolder = new String("C:\\Users\\fvz_1\\OneDrive\\Escritorio\\converters");
	
	static final StringBuilder xml = new StringBuilder(	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
														"\r\n" + 
														"<beans xmlns=\"http://www.springframework.org/schema/beans\"\r\n" + 
														"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:util=\"http://www.springframework.org/schema/util\"\r\n" + 
														"	xsi:schemaLocation=\"\r\n" + 
														"		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd		\r\n" + 
														"		http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.0.xsd		\r\n" + 
														"	\">\r\n" + 
														"\r\n" + 
														"	<util:list id=\"converters\">");
	
	static final String domainFolder = new String("C:\\Users\\fvz_1\\AppData\\Roaming\\workspace\\DT2Group14\\Projects\\Acme-Rookie-Rank\\src\\main\\java\\domain");
	
	public static void main(String[] args) throws Exception {
		final PrintStream xmlPrinter = new PrintStream(new File(dstFolder + File.separator + "converters.xml"));
		xmlPrinter.println(xml);
		
		File srcFolder = new File(domainFolder);
		
		for(File e : srcFolder.listFiles()) {
			Class<?> clazz = Class.forName("domain." + e.getName().replace(".java", ""));
			
			printXml(clazz, xmlPrinter);
			printStringTo(clazz);
			printEntityTo(clazz);
		}
		
		xmlPrinter.println(	"</util:list>\r\n" + 
							"\t</beans>");
		xmlPrinter.close();
	}
	
	static final void printXml(Class<?> clazz, PrintStream ps) {
		String pluse = clazz.getSimpleName();
		ps.println(String.format("\t\t<bean class=\"converters.%sToStringConverter\" />", pluse));
		ps.println(String.format("\t\t<bean class=\"converters.StringTo%sConverter\" />", pluse));
	}
	
	static final void printEntityTo(Class<?> clazz) throws FileNotFoundException {
		String minus = clazz.getSimpleName().toLowerCase();
		String pluse = clazz.getSimpleName();
		
		PrintStream ps = new PrintStream(new FileOutputStream(new File(String.format(dstFolder + File.separator + "%sToStringConverter.java", pluse))));
		System.setOut(ps);
		
		System.out.println(String.format(entityTo, pluse, pluse, pluse, pluse, minus, minus, minus));
	}
	
	static final void printStringTo(Class<?> clazz) throws FileNotFoundException {
		String minus = clazz.getSimpleName().toLowerCase();
		String pluse = clazz.getSimpleName();
		
		PrintStream ps = new PrintStream(new FileOutputStream(new File(dstFolder + File.separator + "StringTo" + pluse + "Converter.java")));
		System.setOut(ps);
		
		System.out.println(String.format(stringTo, pluse, pluse, pluse, pluse, pluse, minus, pluse, pluse, minus));
		
		ps.close();
	}

}
