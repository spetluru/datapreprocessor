import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Arrays;

public class CleanServlet extends HttpServlet {
 //Connection c;
//PreparedStatement ps;
  
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String option=req.getParameter("type");
    HttpSession session = req.getSession();
    String s = (String)session.getAttribute("table");
//Cookie[] cookies = req.getCookies();
    //String s="";
    
     if(option.equals("mean"))
{
	//out.println("mean");
    	meanc(s);
}
    else if(option.equals("median"))
	medianc(s);
   else if(option.equals("mode"))
	modec(s);

  res.sendRedirect("localhost:8081/miniproject/index.html#third");
}
public static void modec(String args) throws IOException
    {
	Connection c;
	try
	{
		int p,i;
		//System.out.println(args);		
		Class.forName("oracle.jdbc.driver.OracleDriver");		
		c = DriverManager.getConnection("jdbc:Oracle:thin:@localhost:1521:XE","system","manager");
		StringBuffer sb = new StringBuffer("Select * from ");
		sb.append(args);
		String s = new String(sb);
		//System.out.println(s);
		PreparedStatement ps = c.prepareStatement(s);		
		ps.execute();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		int type[] = new int[numberOfColumns];
		String name[] = new String[numberOfColumns];
		for(i = 0;i<numberOfColumns;i++)
		{
			name[i] = rsmd.getColumnName(i+1);
			type[i] = rsmd.getColumnType(i+1);
			//System.out.println(name[i]+" "+type[i]);
		}
		// running till here
		
		for(i=0;i<numberOfColumns;i++)
		{					
			//System.out.println(name[i]);
			//System.out.println(args);
			StringBuffer asb = new StringBuffer();
			asb.append("Select ");
			asb.append(name[i]);
			asb.append(" from ");
			asb.append(args);
			String as = new String(asb);
			PreparedStatement prs = c.prepareStatement(as);
			prs.execute();
			ResultSet rr = prs.getResultSet();
			ResultSet rprs = prs.getResultSet();							
			if(type[i]==2)
			{
				StringBuffer aasb = new StringBuffer();
				aasb.append("Select ");
				aasb.append(name[i]);
				aasb.append(" from ");
				aasb.append(args);
				aasb.append(" where ");
				aasb.append(name[i]);
				aasb.append(" !=-1");
				String aas = new String(aasb);
				//System.out.println(aas);
				PreparedStatement srps = c.prepareStatement(aas);
				srps.execute();
				ResultSet rsrps = srps.getResultSet();
				ResultSetMetaData srsmd = srps.getMetaData();
				int n = srsmd.getColumnCount();
				//int arr [] = new int[1000];
				ArrayList<Integer> mlist=new ArrayList<>();
				//int p = 0;
				while(rsrps.next())
				{
					mlist.add(rsrps.getInt(1));
				}
				int sas =mlist.size();
   				 int[] arr = new int[sas];
    				for (int h = 0; h < sas; h++) 
        				arr[h] =mlist.get(h).intValue();
				int maxValue=0; int  maxCount=0;
				for (p = 0; p <arr.length; ++p) 		
				{
        				 int count = 0;
       					 for (int j = 0; j < arr.length; ++j)
					 {
            					if (arr[j] == arr[p])				
							++count;
        			         }
      					 if (count > maxCount)	
					 {
           					maxCount = count;
          					maxValue = arr[p];
     					 }
   				}
				while (rprs.next()) 
				{
					int x = rprs.getInt(1);
					if(x==-1)
					{
						//System.out.println(x);
						StringBuffer ss = new StringBuffer();
						ss.append("Update ");
						ss.append(args);
						ss.append( " set ");
						ss.append(name[i]);
						ss.append(" = ");
						ss.append(maxValue);
						ss.append(" where ");
						ss.append(name[i]);
						ss.append(" =-1 ");
						String assss = new String(ss);
						//System.out.println(assss);
						PreparedStatement aps = c.prepareStatement(assss);
						aps.execute();
                                                aps.close();
						//rprs.updateInt(1,avg);
						//rprs.updateRow();
					}
				}
			}
			if(type[i]==12)
			{
				rprs.next();
				String s1 = rprs.getString(1);
				while(rprs.next())
				{
					String str = rprs.getString(1);
					if(str=="*")
					{
						StringBuffer ss = new StringBuffer();
						ss.append("Update ");
						ss.append(args);
						ss.append( " set ");
						ss.append(name[i]);
						ss.append(" = ");
						ss.append(s1);
						ss.append(" where ");
						ss.append(name[i]);
						ss.append(" = '*'");
						String assss = new String(ss);
						//System.out.println(assss);
						PreparedStatement aps = c.prepareStatement(assss);
						aps.execute();	
					}
					s1 = str;
				}
			}
                                   rprs.close();
		}
			
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
  }

 public static void medianc(String args) throws IOException
    {
	Connection c;
	try
	{
		int i;
		System.out.println(args);		
		Class.forName("oracle.jdbc.driver.OracleDriver");		
		c = DriverManager.getConnection("jdbc:Oracle:thin:@localhost:1521:XE","system","manager");
		StringBuffer sb = new StringBuffer("Select * from ");
		sb.append(args);
		String s = new String(sb);
		System.out.println(s);
		PreparedStatement ps = c.prepareStatement(s);		
		ps.execute();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		int type[] = new int[numberOfColumns];
		String name[] = new String[numberOfColumns];
		for(i = 0;i<numberOfColumns;i++)
		{
			name[i] = rsmd.getColumnName(i+1);
			type[i] = rsmd.getColumnType(i+1);
			System.out.println(name[i]+" "+type[i]);
		}
		// running till here
		
		for(i=0;i<numberOfColumns;i++)
		{					
			//System.out.println(name[i]);
			//System.out.println(args);
			StringBuffer asb = new StringBuffer();
			asb.append("Select ");
			asb.append(name[i]);
			asb.append(" from ");
			asb.append(args);
			String as = new String(asb);
			PreparedStatement prs = c.prepareStatement(as);
			prs.execute();
			ResultSet rr = prs.getResultSet();
			ResultSet rprs = prs.getResultSet();							
			if(type[i]==2)
			{
				StringBuffer aasb = new StringBuffer();
				aasb.append("Select ");
				aasb.append(name[i]);
				aasb.append(" from ");
				aasb.append(args);
				aasb.append(" where ");
				aasb.append(name[i]);
				aasb.append(" !=-1");
				String aas = new String(aasb);
				System.out.println(aas);
				PreparedStatement srps = c.prepareStatement(aas);
				srps.execute();
				ResultSet rsrps = srps.getResultSet();
				ResultSetMetaData srsmd = srps.getMetaData();
				int n = srsmd.getColumnCount();
				//int arr [] = new int[1000];
				ArrayList<Integer> mlist=new ArrayList<>();
				//int d = 0;
				while(rsrps.next())
				{
					mlist.add(rsrps.getInt(1));
				}
				int sas =mlist.size();
   				 int[] arr = new int[sas];
    				for (int h = 0; h < sas; h++) 
        				arr[h] =mlist.get(h).intValue();
				Arrays.sort(arr);
				int median;
				if (arr.length % 2 == 0)
   				  median = (int)(((double)arr[arr.length/2] + (double)arr[arr.length/2 - 1])/2);
				else
    				    median = (int) arr[arr.length/2];
				 
				while (rprs.next()) 
				{
					int x = rprs.getInt(1);
					if(x==-1)
					{
						//System.out.println(x);
						StringBuffer ss = new StringBuffer();
						ss.append("Update ");
						ss.append(args);
						ss.append( " set ");
						ss.append(name[i]);
						ss.append(" = ");
						ss.append(median);
						ss.append(" where ");
						ss.append(name[i]);
						ss.append(" =-1 ");
						String assss = new String(ss);
						//System.out.println(assss);
						PreparedStatement aps = c.prepareStatement(assss);
						aps.execute();
                                                aps.close();
						//rprs.updateInt(1,avg);
						//rprs.updateRow();
					}
				}
			}
			if(type[i]==12)
			{
				rprs.next();
				String s1 = rprs.getString(1);
				while(rprs.next())
				{
					String str = rprs.getString(1);
					if(str=="*")
					{
						StringBuffer ss = new StringBuffer();
						ss.append("Update ");
						ss.append(args);
						ss.append( " set ");
						ss.append(name[i]);
						ss.append(" = ");
						ss.append(s1);
						ss.append(" where ");
						ss.append(name[i]);
						ss.append(" = '*'");
						String assss = new String(ss);
						//System.out.println(assss);
						PreparedStatement aps = c.prepareStatement(assss);
						aps.execute();	
					}
					s1 = str;
				}
			}
                                   rprs.close();
		}
			
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
  }
public static void meanc(String args) throws IOException
    {
	Connection c;
	try
	{
		int i;
		//System.out.println(args);		
		Class.forName("oracle.jdbc.driver.OracleDriver");		
		c = DriverManager.getConnection("jdbc:Oracle:thin:@localhost:1521:XE","system","manager");
		StringBuffer sb = new StringBuffer("Select * from ");
		sb.append(args);
		String s = new String(sb);
		System.out.println(s);
		PreparedStatement ps = c.prepareStatement(s);		
		ps.execute();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		int type[] = new int[numberOfColumns];
		String name[] = new String[numberOfColumns];
		for(i = 0;i<numberOfColumns;i++)
		{
			name[i] = rsmd.getColumnName(i+1);
			type[i] = rsmd.getColumnType(i+1);
			//System.out.println(name[i]+" "+type[i]);
		}
		// running till here
		
		for(i=0;i<numberOfColumns;i++)
		{					
			//System.out.println(name[i]);
			//System.out.println(args);
			StringBuffer asb = new StringBuffer();
			asb.append("Select ");
			asb.append(name[i]);
			asb.append(" from ");
			asb.append(args);
			String as = new String(asb);
			PreparedStatement prs = c.prepareStatement(as);
			prs.execute();
			ResultSet rprs = prs.getResultSet();							
			if(type[i]==2)
			{
				StringBuffer aasb = new StringBuffer();
				aasb.append("Select avg(");
				aasb.append(name[i]);
				aasb.append(") from ");
				aasb.append(args);
				aasb.append(" where ");
				aasb.append(name[i]);
				aasb.append(" !=-1");
				String aas = new String(aasb);
				//System.out.println(aas);
				PreparedStatement srps = c.prepareStatement(aas);
				srps.execute();
				ResultSet rsrps = srps.getResultSet();
				rsrps.next();
				int avg = (int) rsrps.getFloat(1);
				while (rprs.next()) 
				{
					int x = rprs.getInt(1);
					if(x==-1)
					{
						//System.out.println(x);
						StringBuffer ss = new StringBuffer();
						ss.append("Update ");
						ss.append(args);
						ss.append( " set ");
						ss.append(name[i]);
						ss.append(" = ");
						ss.append(avg);
						ss.append(" where ");
						ss.append(name[i]);
						ss.append(" =-1 ");
						String assss = new String(ss);
						//System.out.println(assss);
						PreparedStatement aps = c.prepareStatement(assss);
						aps.execute();
                                                                                                aps.close();
						//rprs.updateInt(1,avg);
						//rprs.updateRow();
					}
				}
			}
			if(type[i]==12)
			{
				rprs.next();
				String s1 = rprs.getString(1);
				while(rprs.next())
				{
					String str = rprs.getString(1);
					if(str=="*")
					{
						StringBuffer ss = new StringBuffer();
						ss.append("Update ");
						ss.append(args);
						ss.append( " set ");
						ss.append(name[i]);
						ss.append(" = ");
						ss.append(s1);
						ss.append(" where ");
						ss.append(name[i]);
						ss.append(" = '*'");
						String assss = new String(ss);
						//System.out.println(assss);
						PreparedStatement aps = c.prepareStatement(assss);
						aps.execute();	
					}
					s1 = str;
				}
			}
                                   rprs.close();
		}
}
                               catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}
}     