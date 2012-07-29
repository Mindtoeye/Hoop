import java.lang.reflect.Method;

public class MainClass
{
	public static void main(String[] args)
	{
		try
		{
			MyClassLoader mcl = new MyClassLoader();
			Class c = mcl.loadClass("aThingDoer");// load a class that implements interface ThingDoer
			Object o = c.newInstance();           // call the class's constuctor
			Method m = c.getMethod("doThings");   // get ready to call method doThings()
			m.invoke(o);                          // conceptually equivalent to o.doThings();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}