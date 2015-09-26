//integrate servlet

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class IntegrateServlets extends HttpServlet {
 Connection c;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    Enumeration pname = req.getParameterNames();
    List<String> list = new ArrayList<>();
    String p[]=req.getParameterValues("mytext");
    StringBuffer sb=new StringBuffer();
    int n = p[0].lastIndexOf('\\');
    for(int i=0;i<=n;i++)
    sb.append(p[0].charAt(i));
StringBuffer fff = new StringBuffer();
for(int i = n+1;i<=n+3;i++)
{	sb.append(p[0].charAt(i));
	fff.append(p[0].charAt(i));
}
    sb.append("comb");
    String tname = new String(sb); //test variable
    sb.append(".ARFF");
    String first=new String(sb); // integrated name file with path
    list.add(first);
fff.append("comb");
//fff.append(".ARFF");
String tyname=new String(fff); //table name
//out.println(tyname);
   for(int i=0;i<p.length;i++)
  { 
        	//out.println(p[i]);
	list.add(p[i]);
    }
    String[] args = list.toArray( new String[list.size()] );
//out.println("<html><head></head><body>");
for(int i =0;i<args.length;i++)
{
	//out.println(args[i]);
}
//out.println("</body></html>");
    combine(args);              // calling for integration
   // out.println("combined");
    //FileRead fr= new FileRead();
    update(args[0],tyname); // update into database
   HttpSession session = req.getSession(true);
   session.setAttribute("table",tyname);
//Cookie c1 = new Cookie("table",tyname);
//res.addCookie(c1);
   // out.println("inserted");
 res.sendRedirect("localhost:8081/miniproject/index.html#first");
}
public static void combine(String[] args)throws IOException
	{
                               
		try
		{

			String fname=args[1];
			BufferedReader myReader = new BufferedReader(new FileReader(fname));
			//System.out.println("Enter output file name:");
			//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

			String op =args[0];
			File file = new File(op);
			if (!file.exists()) 			
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String nextLine=myReader.readLine();
			while(nextLine!=null)
			{
				bw.write(nextLine);
				bw.newLine();
				nextLine=myReader.readLine();
			}
	        	for(int i=2;i<args.length;i++)
			{
				fname = args[i];	
				//System.out.println(fname);	
				BufferedReader nxt = new BufferedReader(new FileReader(fname));
				nextLine = nxt.readLine();
				nextLine = nxt.readLine();
				while(nextLine!=null)
				{
		    			bw.write(nextLine);
					bw.newLine();
		    			nextLine=nxt.readLine();
				}	
			}
			bw.close();
	
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
               public static void update(String args,String name) throws IOException
    {
	Connection c;
	try
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		c = DriverManager.getConnection("jdbc:Oracle:thin:@localhost:1521:XE","system","manager");
		String nextLine;		
		Statement s = c.createStatement();
		StringBuffer stmt = new StringBuffer();
		Scanner scanner = new Scanner(System.in);
		
		//int np = args.lastIndexOf('\\');
		String fname=args;
		//System.out.print("Enter a file name: ");
		//String fname = scanner.nextLine();
		//System.out.println(fname);
                                StringBuffer fn=new StringBuffer();
		StringBuffer tn=new StringBuffer();
		fn.append("Create table ");
                           
		for(int i = 0;i<name.length();i++)
                                {      if(name.charAt(i)=='.')
                                             break; 
                                      tn.append(name.charAt(i));
		}
		fn.append(tn);
		//System.out.println(fn);
		//variable names
		
        		BufferedReader myReader = new BufferedReader(new FileReader(fname));
		int x = 1;
		int i =0; 
	    	nextLine = myReader.readLine();
		//System.out.println(nextLine);
        		String variables[]=nextLine.split(",");
        		String datatype[]= new String[variables.length];
		String words[] = new String[variables.length];
		for(i=1;i<variables.length;i++)
		{
			stmt.append("?,");		
		}
        		stmt.append('?');
		StringBuffer sb=new StringBuffer();
		nextLine = myReader.readLine();
		//System.out.println(nextLine);
		words=nextLine.split(",");
		for(i=0;i<variables.length-1;i++)
		{
	        		//System.out.println(words[i]);
			try 
			{
       				Integer.parseInt(words[i]);
       				datatype[i]="numeric";
			} 
			catch (NumberFormatException ne) 
			{
       				datatype[i]="varchar2(50)";
			}
			sb.append(variables[i]+' '+datatype[i]+',');
			
		}
		try 
		{
       			Integer.parseInt(words[i]);
       			datatype[i]="numeric";
		} 
		catch (NumberFormatException ne) 
		{
       			datatype[i]="varchar2(50)";
		}
		sb.append(variables[i]+' '+datatype[i]);
	               fn.append("(" + sb);
 		fn.append(")");
		//for(i=0;i<variables.length;i++)
		//System.out.println(datatype[i]+"  ");
		String f = new String(fn);
	                int n=s.executeUpdate(f);
		c.commit();
		String tname = new String(tn); // table name
                                //System.out.println(stmt);
		PreparedStatement ps = c.prepareStatement("Insert into "+tname+" values("+stmt+")");
 		while(nextLine!=null)
		{
			x++;
			String word[]=nextLine.split(",");
			for(i=0;i<word.length;i++)
				//System.out.print(word[i]+"  ");
			//System.out.print("\n \n");
			for(i=0;i<word.length;i++)
			{	//System.out.println(word[i]+ "    "+datatype[i]);
				if(datatype[i]=="numeric")
				{
					try
					{
						int num = Integer.parseInt(word[i]);
						ps.setInt(i+1,num);
					}
					catch(NumberFormatException e)
					{
						ps.setInt(i+1,-1);
					}
	
				}	
			   
				else
					ps.setString(i+1,word[i]);
			}
			ps.executeUpdate();
            			nextLine = myReader.readLine();
	           
        		} c.commit();ps.close();
		      
        
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
  }
               
	
}



