/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.hal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;

//
// base class for all JNI wrappers
//
public class JNIWrapper
{
    private static boolean libraryLoaded = false;
    
    private static void createAndLoadTempLibrary(File aTempDir, String aResourceName) throws IOException
    {
        String fileName = aResourceName.substring(aResourceName.lastIndexOf("/") + 1);
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String basename = fileName.substring(0, fileName.lastIndexOf("."));

        InputStream is = NetworkTablesJNI.class.getResourceAsStream(aResourceName);
        if (is != null)
        {
            File jniLibrary = new File(aTempDir, fileName);

            // flag for delete on exit
            jniLibrary.deleteOnExit();
            OutputStream os = new FileOutputStream(jniLibrary);

            byte[] buffer = new byte[1024];
            int readBytes;
            try
            {
                while ((readBytes = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, readBytes);
                }
            }
            finally
            {
                os.close();
                is.close();
            }

            System.out.println("Created temporary library at " + jniLibrary.getAbsolutePath() + " from resource " + aResourceName);
            System.load(jniLibrary.getAbsolutePath());
        }
    }

    private static void loadLibrary(File aTempDir, String aLibraryname)
    {
        String osname = System.getProperty("os.name");
        String resname;
        if (osname.startsWith("Windows"))
        {
            resname = "/Windows/" + System.getProperty("os.arch") + "/";
        }
        else
        {
            resname = "/" + osname + "/" + System.getProperty("os.arch") + "/";
        }

        if (osname.startsWith("Windows"))
        {
            resname += aLibraryname + ".dll";
        }
        else if (osname.startsWith("Mac"))
        {
            resname += aLibraryname + ".dylib";
        }
        else
        {
            resname += "lib" + aLibraryname + ".so";
        }

        try
        {
            createAndLoadTempLibrary(aTempDir, resname);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not load " + resname);
        }
    }

    static
    {
        if (!libraryLoaded)
        {
            long rando = new Random().nextLong();
            File tempDir = new File("temp/" + rando + "/");
            tempDir.mkdirs();
            tempDir.deleteOnExit();

            loadLibrary(tempDir, "snobotSimHal");
            loadLibrary(tempDir, "HALAthena");
            loadLibrary(tempDir, "wpilibJavaJNI");
            loadLibrary(tempDir, "ntcore");
            loadLibrary(tempDir, "wpiutil");
            loadLibrary(tempDir, "wpilibc");
            libraryLoaded = true;
        }
    }

    public static int getPortWithModule(byte module, byte channel)
    {
        return channel;
    }

    public static int getPort(byte channel)
    {
        return channel;
    }
}
