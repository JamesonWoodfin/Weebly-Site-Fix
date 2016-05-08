import java.io.*;
import java.util.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class SiteFixClean {

   /* This program will fix my Weebly site after an export.
    * It places two images in the upload folder, appends each HTML file with
	* with a line that sets the icon, and adds the sitemap file.
	* 
	* This program assumes the existance of two directories under the same
	* directory that the program is in:
	* 	- 'Resources': Where all the necessary files to fix the sie are (e.g. images, sitemap, etc...)
	* 	- 'Fresh_Export': Where you unzip your weebly export to.
    */ 

    public static void main(String []args) {
		BufferedReader in = null;
		PrintWriter clearFile = null;
		String lineCheck = new String("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0;\">");
		String iconCode = new String("<link rel=\"icon\" href=\"uploads/7/0/7/1/70716011/dslogo.ico\" type=\"image/x-icon\">");
		String line = new String();                          // ^ this part of the html sets the icon and will need to change since 
		Stack fileNames = new Stack();                       //   the name of your icon and the path to it will change for each site.
		String srcDir = "Resources\\";
		String destDir = "Fresh_Export\\";
		String srcPath = new String();
		String destPath = new String();
		Path src;
		Path dest;
		
		//Push the files you want in the upload folder into the stack:
		fileNames.push(new String("1453081009.png"));
		fileNames.push(new String("dslogo.ico"));
		
		//Move the files in the stack to the correct directory:
		
		while(!fileNames.empty()){
			srcPath = srcDir + fileNames.peek();
			destPath = destDir + "uploads\\7\\0\\7\\1\\70716011\\" + fileNames.pop();
			                   // ^ make sure to change this to match what you upload folder looks like
		
			src = Paths.get(srcPath);
			dest = Paths.get(destPath);
		
			try{
			Files.copy(src, dest);
			}catch(IOException e){
			e.printStackTrace();
			}
		}
		
		// Push the HTML files you want to apppend the icon code to into the stack:
		fileNames.push("about.html");
		fileNames.push("index.html");
		fileNames.push("people.html");
		fileNames.push("projects.html");
		fileNames.push("events.html");
		
		//Write new HTML files with the same names as above in /Resources with an added line of code to set icon:
		
		try{
		in = new BufferedReader(new FileReader(destDir + fileNames.peek()));
		
		while(!fileNames.empty()){
			System.out.println(fileNames.peek());
			clearFile = new PrintWriter(srcDir + fileNames.peek());
			clearFile.close();
			line = in.readLine();
			Files.write(Paths.get(srcDir + fileNames.peek()), (line+"\n").getBytes(), StandardOpenOption.WRITE);
			while(line != null){
				if(line.equals(lineCheck)){
					Files.write(Paths.get(srcDir + fileNames.peek()), (iconCode+"\n").getBytes(), StandardOpenOption.APPEND);
				}
				line = in.readLine();
				if(line != null)
				Files.write(Paths.get(srcDir + fileNames.peek()), (line+"\n").getBytes(), StandardOpenOption.APPEND);
			}
			in.close();
			fileNames.pop();
			if(!fileNames.empty()){
				in = new BufferedReader(new FileReader(destDir + fileNames.peek()));
			}
		}
		in.close();
		} catch (IOException e) {}
		
		//Push the files you want to add to your website (including the ones you just appended) in to the stack:
		fileNames.push("sitemap.xml");
		fileNames.push("about.html");
		fileNames.push("index.html");
		fileNames.push("people.html");
		fileNames.push("projects.html");
		fileNames.push("events.html");
		
		//Copy the newly written files back into the website and overwrite existing files.
		//Also add the sitemap file.
		
		while(!fileNames.empty()){
			srcPath = srcDir + fileNames.peek();
			destPath = destDir + fileNames.pop();
		
			src = Paths.get(srcPath);
			dest = Paths.get(destPath);
		
			try{
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
			}catch(IOException e){
			e.printStackTrace();
			}
		}
		
		System.out.println("Site Fixed");
    }
}