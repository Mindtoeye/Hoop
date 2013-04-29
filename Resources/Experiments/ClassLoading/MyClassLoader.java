import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

public class MyClassLoader extends ClassLoader
{
	private Hashtable<String, Class> classes; // map from class-name to class

	public MyClassLoader()
	{
		classes = new Hashtable<String, Class>();
	}

	public Class loadClass(String name) throws ClassNotFoundException
	{
		return loadClass(name, true);
	}

	public Class loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		/* first check local cache of classes */
		Class cached = classes.get(name);
		if(cached != null)
		{
			return cached;
		}

		/* special case if we're looking for a "ThingDoer" */
		if(name.equals("aThingDoer"))
		{
			return loadThingDoer(resolve);
		}

		/* check with the system class loader */
		Class c = super.findSystemClass(name);
		classes.put(name, c);
		return c;
	}

	private Class loadThingDoer(boolean resolve) throws ClassNotFoundException
	{
		JarFile jf;
		try
		{
			jf = new JarFile("lib.jar");
		}
		catch(IOException ioe)
		{
			throw new ClassNotFoundException();
		}

		for(Enumeration<JarEntry> entries = jf.entries(); entries.hasMoreElements(); )
		{
			JarEntry entry = entries.nextElement();
			InputStream is;
			try
			{
				is = jf.getInputStream(entry);
			}
			catch(IOException ioe)
			{
				throw new ClassNotFoundException();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int b;
			try
			{
				while((b = is.read()) != -1)
				{
					baos.write(b);
				}
			}
			catch(IOException e)
			{
				throw new ClassNotFoundException();
			}
			byte[] bytes = baos.toByteArray();

			/* determine whether this byte array is a class file, and if it is, whether it implements ThingDoer interface */
			try
			{
				/* Check if it implements the interface by defining the class */
				try
				{
					Class c = defineClass(null, bytes, 0, bytes.length);
					if(c == null)
					{
						continue;
					}
					Class[] interfaces = c.getInterfaces();
					boolean isThingDoer = false;
					for(Class thisInterface : interfaces)
					{
						if(thisInterface.getName().equals("ThingDoer") || thisInterface.getName().endsWith(".ThingDoer"))
						{
							isThingDoer = true;
							break;
						}
					}
					if(isThingDoer)
					{
						if(resolve)
						{
							resolveClass(c);
						}
						classes.put("ThingDoer", c);
						return c;
					}
				}
				catch(ClassFormatError cfe)
				{
					continue; // move on to the next class in the jar file
				}
			}
			catch(IndexOutOfBoundsException ioobe)
			{
				continue; // this file is not long enough to be a class file; move on to the next one
			}
		}

		throw new ClassNotFoundException();
	}
}