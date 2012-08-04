import java.lang.reflect.Method;

public class MainClass
{
	public static void main(String[] args)
	{
		try
		{
			MyClassLoader mcl = new MyClassLoader();
			
			// load a class that implements interface ThingDoer
			Class c = mcl.loadClass("aThingDoer");
			
			// call the class's constuctor
			Object o = c.newInstance();           
			
			// get ready to call method doThings()
			Method m = c.getMethod("doThings");   
			
			// conceptually equivalent to o.doThings();
			m.invoke(o);                          
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}