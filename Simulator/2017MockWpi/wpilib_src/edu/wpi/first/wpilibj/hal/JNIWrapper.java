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
    static File jniLibrary = null;

    static
    {
        if (!libraryLoaded)
        {
            try
            {
                System.loadLibrary("ntcore");
            }
            catch (UnsatisfiedLinkError e)
            {
                try
                {
                    String osname = System.getProperty("os.name");
                    String resname;
                    if (osname.startsWith("Windows"))
                        resname = "/Windows/" + System.getProperty("os.arch") + "/";
                    else
                        resname = "/" + osname + "/" + System.getProperty("os.arch") + "/";
                    System.out.println("platform: " + resname);
                    if (osname.startsWith("Windows"))
                        resname += "snobotSimHal.dll";
                    else if (osname.startsWith("Mac"))
                        resname += "libntcore.dylib";
                    else
                        resname += "libntcore.so";
                    InputStream is = NetworkTablesJNI.class.getResourceAsStream(resname);
                    if (is != null)
                    {
                        // create temporary file
                        if (System.getProperty("os.name").startsWith("Windows"))
                            jniLibrary = File.createTempFile("NetworkTablesJNI", ".dll");
                        else if (System.getProperty("os.name").startsWith("Mac"))
                            jniLibrary = File.createTempFile("libNetworkTablesJNI", ".dylib");
                        else
                            jniLibrary = File.createTempFile("libNetworkTablesJNI", ".so");
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
                    }
                    else
                    {
                        System.loadLibrary("ntcore");
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    System.exit(1);
            }
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
