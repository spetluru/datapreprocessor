import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Arrays;

public class TransformServlet extends HttpServlet {
 //Connection c;
//PreparedStatement ps;
  
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String option=req.getParameter("type1");
    String x = req.getParameter("col");
    HttpSession session = req.getSession();
    String s = (String)session.getAttribute("table");
//Cookie[] cookies = req.getCookies();
    //String s="";
    
     if(option.equals("dscaling"))
{
	//out.println("mean");
    	dscale(s,x);
}
    else if(option.equals("minmax"))
	mnmx(s,x);
   else if(option.equals("zscore"))
	zscale(s,x);

   res.sendRedirect("localhost:8081/miniproject/index.html#fifth");
}
public static void dscale(String args,String colno) throws IOException
       {
	Connection c;
	try
	{
		int i,min=0,max,newmax,newmin;
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
		i =Integer.parseInt(colno);
		i--;
		StringBuffer ssb = new StringBuffer();
		ssb.append("Select count(*) from ");
		ssb.append(args);
		String ssss = new String(ssb);
		PreparedStatement psp = c.prepareStatement(ssss);
		psp.execute();
		ResultSet rpsp = psp.getResultSet();
		rpsp.next();
		int num = rpsp.getInt(1);
		if(type[i]==2)
		{
			StringBuffer asb = new StringBuffer();
			asb.append("Select ");
			asb.append(name[i]);
			asb.append(" from ");
			asb.append(args);
			String as = new String(asb);
			System.out.println(asb);
			PreparedStatement prs = c.prepareStatement(as);
			prs.execute();
			ResultSet rr = prs.getResultSet();						
			int arr[]=new int[num];		
				int k=0;
				newmin=1;
				newmax=10;
				StringBuffer sbch = new StringBuffer();
				sbch.append("Alter table ");
				sbch.append(args);
				sbch.append(" modify( ");
				sbch.append(name[i]);
				sbch.append(" float)");
				String ssbch = new String(sbch);
				PreparedStatement psbch = c.prepareStatement(ssbch);
				psbch.execute();
				while(rr.next())
				{
					arr[k++]=rr.getInt(1);
					//System.out.println(rr.getInt(1));
				}
				//System.out.println(k);
				Arrays.sort(arr);
				for(int p=k;p>=0;p--)
				{
					//System.out.println(arr[p]);
				}
				min=arr[0];
				max=arr[k-1];
				int m = max;
				int count = 0;
				int den = 1;
				while(m>=1)
				{
					m = m/10;
					den=den*10;	
				}	
			
				System.out.println("Min and max are:");
				System.out.println(min);
				System.out.println(max);
				System.out.println(den);
				PreparedStatement pqrs = c.prepareStatement(as);
				pqrs.execute();
				ResultSet rprs = pqrs.getResultSet();
				while (rprs.next()) 
				{
					int x = rprs.getInt(1);
					float norm=(float)x/den;
					//System.out.println(norm);
					StringBuffer ss = new StringBuffer();
					ss.append("Update ");
					ss.append(args);
					ss.append( " set ");
					ss.append(name[i]);
					ss.append(" = ");
					ss.append(norm);
					ss.append(" where ");
					ss.append(name[i]);
					ss.append(" = ");
					ss.append(x);
					String assss = new String(ss);
					//System.out.println(assss);
					PreparedStatement aps = c.prepareStatement(assss);
					aps.execute();
                                                		aps.close();
						//rprs.updateInt(1,avg);
						//rprs.updateRow();
				}
                                   rprs.close();
		}
			
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
  }
 public static void mnmx(String args, String colno) throws IOException
    {
	Connection c;
	try
	{
		int i,min=0,max,newmax,newmin;
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
			//System.out.println(name[i]+" "+type[i]);
		}
		i =Integer.parseInt(colno);
		i--;
		StringBuffer ssb = new StringBuffer();
		ssb.append("Select count(*) from ");
		ssb.append(args);
		String ssss = new String(ssb);
		PreparedStatement psp = c.prepareStatement(ssss);
		psp.execute();
		ResultSet rpsp = psp.getResultSet();
		rpsp.next();
		int num = rpsp.getInt(1);
		if(type[i]==2)
		{
			StringBuffer asb = new StringBuffer();
			asb.append("Select ");
			asb.append(name[i]);
			asb.append(" from ");
			asb.append(args);
			String as = new String(asb);
			System.out.println(asb);
			PreparedStatement prs = c.prepareStatement(as);
			prs.execute();
			ResultSet rr = prs.getResultSet();						
			int arr[]=new int[num];
				
				int k=0;
				newmin=1;
				newmax=10;
				while(rr.next())
				{
					arr[k++]=rr.getInt(1);
					//System.out.println(rr.getInt(1));
				}
				//System.out.println(k);
				Arrays.sort(arr);
				for(int p=k;p>=0;p--)
				{
					//System.out.println(arr[p]);
				}
				min=arr[0];
				max=arr[k-1];
				System.out.println("Min and max are:");
				System.out.println(min);
				System.out.println(max);
				PreparedStatement pqrs = c.prepareStatement(as);
				pqrs.execute();
				ResultSet rprs = pqrs.getResultSet();
				while (rprs.next()) 
				{
					int x = rprs.getInt(1);
					int norm=(int)(( ( (float)(x-min)/(max-min) )*(newmax-newmin) )+newmin);
					//System.out.println(norm);
					StringBuffer ss = new StringBuffer();
					ss.append("Update ");
					ss.append(args);
					ss.append( " set ");
					ss.append(name[i]);
					ss.append(" = ");
					ss.append(norm);
					ss.append(" where ");
					ss.append(name[i]);
					ss.append(" = ");
					ss.append(x);
					String assss = new String(ss);
					//System.out.println(assss);
					PreparedStatement aps = c.prepareStatement(assss);
					aps.execute();
                                                		aps.close();
						//rprs.updateInt(1,avg);
						//rprs.updateRow();
				}
                                   rprs.close();
		}
			
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
  }
 public static void zscale(String args, String colno) throws IOException
       {
	Connection c;
	try
	{
		int i,min=0,max,newmax,newmin;
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
			//System.out.println(name[i]+" "+type[i]);
		}
		i =Integer.parseInt(colno);
		i--;
		StringBuffer ssb = new StringBuffer();
		ssb.append("Select count(*) from ");
		ssb.append(args);
		String ssss = new String(ssb);
		PreparedStatement psp = c.prepareStatement(ssss);
		psp.execute();
		ResultSet rpsp = psp.getResultSet();
		rpsp.next();
		int num = rpsp.getInt(1);
		if(type[i]==2)
		{
			StringBuffer asb = new StringBuffer();
			asb.append("Select ");
			asb.append(name[i]);
			asb.append(" from ");
			asb.append(args);
			String as = new String(asb);
			System.out.println(asb);
			PreparedStatement prs = c.prepareStatement(as);
			prs.execute();
			ResultSet rr = prs.getResultSet();						
			int arr[]=new int[num];		
				int k=0;
				newmin=1;
				newmax=10;
				StringBuffer sbch = new StringBuffer();
				sbch.append("Alter table ");
				sbch.append(args);
				sbch.append(" modify( ");
				sbch.append(name[i]);
				sbch.append(" float)");
				String ssbch = new String(sbch);
				PreparedStatement psbch = c.prepareStatement(ssbch);
				psbch.execute();
				float avg = 0;
				while(rr.next())
				{
					arr[k++]=rr.getInt(1);
					avg+=arr[k-1];
					//System.out.println(rr.getInt(1));
				}
				avg = avg/(k-1);
				//System.out.println(k);
				Arrays.sort(arr);
				float dsum=0;
				for(int p=k-1;p>=0;p--)
				{
					dsum+=Math.pow((arr[p]-avg),2);
				}
				float deviation =(float) Math.sqrt(dsum/(k-2));
				System.out.println(dsum);
				PreparedStatement pqrs = c.prepareStatement(as);
				pqrs.execute();
				ResultSet rprs = pqrs.getResultSet();
				while (rprs.next()) 
				{
					int x = rprs.getInt(1);
					float norm=(float)Math.abs((x-avg)/deviation);
					//System.out.println(norm);
					StringBuffer ss = new StringBuffer();
					ss.append("Update ");
					ss.append(args);
					ss.append( " set ");
					ss.append(name[i]);
					ss.append(" = ");
					ss.append(norm);
					ss.append(" where ");
					ss.append(name[i]);
					ss.append(" = ");
					ss.append(x);
					String assss = new String(ss);
					//System.out.println(assss);
					PreparedStatement aps = c.prepareStatement(assss);
					aps.execute();
                                                		aps.close();
						//rprs.updateInt(1,avg);
						//rprs.updateRow();
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