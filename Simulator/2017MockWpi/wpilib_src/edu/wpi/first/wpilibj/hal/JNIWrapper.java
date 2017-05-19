/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.hal;

import java.io.File;
import java.io.InputStream;

import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;

//
// base class for all JNI wrappers
//
public class JNIWrapper
{
    static boolean libraryLoaded = false;
    
    private static void loadLibrary(String aLibraryname)
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
        System.out.println("platform: " + resname);
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
        InputStream is = NetworkTablesJNI.class.getResourceAsStream(resname);
        if (is != null)
        {
            resname = new File("../2017MockWpi/native_wpi_libs" + resname).getAbsolutePath();
            System.out.println(resname);
            System.load(resname);
        }
        else
        {
            throw new RuntimeException("Could not load " + resname);
        }
    }

    static
    {
        if (!libraryLoaded)
        {
//            loadLibrary("libwpiutil");
            loadLibrary("snobotSimHal");
            loadLibrary("HALAthena");
            loadLibrary("wpilibJavaJNI");
            // loadLibrary("wpiutil");
            // loadLibrary("wpilibc");
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
