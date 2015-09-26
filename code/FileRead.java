import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class FileRead
{
    public void update(String args) throws IOException
    {
	Connection c;
	try
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String nextLine;		
		c = DriverManager.getConnection("jdbc:Oracle:thin:@localhost:1521:XE","system","manager");
		Statement s = c.createStatement();
		StringBuffer stmt = new StringBuffer();
		Scanner scanner = new Scanner(System.in);
		
		int np = args.lastIndexOf('\\');
		String fname=args.substring(np+1);
		//System.out.print("Enter a file name: ");
		//String fname = scanner.nextLine();
		//System.out.println(fname);
                                StringBuffer fn=new StringBuffer();
		StringBuffer tn=new StringBuffer();
		fn.append("Create table ");
                           
		for(int i = 0;i<fname.length();i++)
                                {      if(fname.charAt(i)=='.')
                                             break; 
                                      tn.append(fname.charAt(i));
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
		String tname = new String(tn);
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
  public static void main(String args[])throws IOException
  {
	FileRead rf = new FileRead();
	rf.update(args[0]);
  }
}
    
