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

import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;

//
// base class for all JNI wrappers
//
public class JNIWrapper
{
    static boolean libraryLoaded = false;

    private static void loadLibrary(String aLibraryName) throws IOException
    {
        String osName = System.getProperty("os.name");

        String resname;
        if (osName.startsWith("Windows"))
        {
            resname = "/Windows/" + System.getProperty("os.arch") + "/";
            resname += "snobotSimHal.dll";
        }
        else
        {
            resname = "/" + osName + "/" + System.getProperty("os.arch") + "/";
            if (osName.startsWith("Windows"))
            {
                resname += "snobotSimHal.dylib";
            }
            else
            {
                resname += "snobotSimHal.so";
            }
        }

        InputStream is = NetworkTablesJNI.class.getResourceAsStream(resname);
        if (is != null)
        {
            File jniLibrary;
            // create temporary file
            if (osName.startsWith("Windows"))
            {
                jniLibrary = File.createTempFile(aLibraryName, ".dll");
            }
            else if (osName.startsWith("Mac"))
            {
                jniLibrary = File.createTempFile(aLibraryName, ".dylib");
            }
            else
            {
                jniLibrary = File.createTempFile(aLibraryName, ".so");
            }

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
            System.load(jniLibrary.getAbsolutePath());
            System.out.println("Loaded " + jniLibrary);
        }
    }

    static
    {
        if (!libraryLoaded)
        {
            try
            {
                loadLibrary("wpilibJavaJNI");
                // loadLibrary("HALAthena");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.exit(1);
                }

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
