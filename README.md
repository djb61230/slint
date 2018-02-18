# SLINT

SLINT is the **S**hared **L**ibrary **I**nstall **N**ame **T**ool project to make creating a collection of a compiled binary
program and it's dependent shared libraries an easy task.  So perhaps an easier way to distribute compiled
code with your project.

Currently just the Mac is supported.  Since the Mac does not allow for building static libraries and binary
executables, a program like this is most handy on the Mac.

The idea for this program comes from [Comskip](https://github.com/erikkaashoek/Comskip) and
[matryoshka-name-tool](https://github.com/essandess/matryoshka-name-tool).  Comskip mentions the use
of matryoshka-name-tool to perform this task for the Mac.  Unfortunately I could not get matryoshka-name-tool
to work.  It's most likely an operator error on my part and since I'm not a Python user I thought I'd spend a
day or two writing similar functionality in Java instead of hacking the matryoshka-name-tool script.

I came to building Comskip for my project [jflicks(source)](https://github.com/djb61230/jflicks) and
[jflicks.org](http://www.jflicks.org).  I have personally used Comskip before (it is terrific) but I hadn't
included it with public jflicks because until I saw the source here on github, it only supported Windows.
Oh you could get it to run on the Mac and Linux with [Wine](https://www.winehq.org) but that was even more
difficult to distribute.  At the time Comskip was only donation-ware and distributing it was not encouraged
by it's author, at least to me in an email.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

Here is a sample run to print out the usage:

```
usage:
	java -jar path/to/slint-x.x.jar -p path/to/program -o path/to/output_directory [-l libraryName] (default 'lib') [-d [DEBUG|OFF]] (default 'OFF') [-h (usage)]

	The program can be an executable or a shared library.
	The output_directory will be created if it does not exist.
	The output_directory/libraryName will be created if it does not exist.
	Also the output_directory/libraryName will be emptied if it does exist.

	Turning on DEBUG will result in messages written to the console.
```

Here is the output of a sample run.  I had previously cloned Comskip and built it on my machine following
the directions and installing needed packages using [Homebrew](https://brew.sh).  The libraries built and
installed by Homebrew will be fetched and altered by SLINT using the Mac utility install_name_tool.

```
--> java -jar slint/target/slint-1.0-SNAPSHOT.jar -p Comskip/comskip -o tmp/output
Output directory exists...
Output library directory created.
Finding dependencies for Comskip/comskip
.......................
Copying files...
First updating tmp/output/comskip to point to tmp/output/lib
Updating tmp/output/lib/libavutil.55.dylib pointing to co-located lib(1 of 7).
Updating tmp/output/lib/libavcodec.57.dylib pointing to co-located lib(2 of 7).
Updating tmp/output/lib/libswresample.2.dylib pointing to co-located lib(3 of 7).
Updating tmp/output/lib/libmp3lame.0.dylib pointing to co-located lib(4 of 7).
Updating tmp/output/lib/libx264.152.dylib pointing to co-located lib(5 of 7).
Updating tmp/output/lib/libargtable2.0.dylib pointing to co-located lib(6 of 7).
Updating tmp/output/lib/libavformat.57.dylib pointing to co-located lib(7 of 7).
Took 59 seconds.
```

Now if this worked correctly, then the comskip binary will now point to the libraries copied to
tmp/output/lib.

```
--> cd tmp/output
--> ls -lsa
total 792
  0 drwxr-xr-x   4 djb  staff     128 Feb 18 10:29 .
  0 drwxr-xr-x  37 djb  staff    1184 Feb 17 14:34 ..
792 -rwxr-xr-x   1 djb  staff  402828 Feb 18 10:29 comskip
  0 drwxr-xr-x   9 djb  staff     288 Feb 18 10:29 lib
--> otool -L comskip
comskip:
	@executable_path/lib/libargtable2.0.dylib (compatibility version 2.0.0, current version 2.8.0)
	@executable_path/lib/libavutil.55.dylib (compatibility version 55.0.0, current version 55.78.100)
	@executable_path/lib/libavformat.57.dylib (compatibility version 57.0.0, current version 57.83.100)
	@executable_path/lib/libavcodec.57.dylib (compatibility version 57.0.0, current version 57.107.100)
	/usr/lib/libSystem.B.dylib (compatibility version 1.0.0, current version 1252.0.0)
```

You can see that otool now reports this copied binary now points to the lib directory. We'll look at
one of those libraries in lib to show how they have been updated to point to the other libraries in lib.

```
--> cd lib
--> ls -lsa
total 31720
    0 drwxr-xr-x  9 djb  staff       288 Feb 18 10:29 .
    0 drwxr-xr-x  4 djb  staff       128 Feb 18 10:29 ..
   64 -rw-r--r--  1 djb  admin     28952 Feb 18 10:29 libargtable2.0.dylib
24608 -rw-r--r--  1 djb  admin  12595776 Feb 18 10:29 libavcodec.57.dylib
 3472 -rw-r--r--  1 djb  admin   1777440 Feb 18 10:29 libavformat.57.dylib
  792 -rw-r--r--  1 djb  admin    402864 Feb 18 10:29 libavutil.55.dylib
  520 -rw-r--r--  1 djb  admin    262364 Feb 18 10:29 libmp3lame.0.dylib
  256 -rw-r--r--  1 djb  admin    127796 Feb 18 10:29 libswresample.2.dylib
 2008 -rw-r--r--  1 djb  admin   1025888 Feb 18 10:29 libx264.152.dylib
--> otool -L libavutil.55.dylib
libavutil.55.dylib:
	/usr/local/opt/ffmpeg/lib/libavutil.55.dylib (compatibility version 55.0.0, current version 55.78.100)
	/System/Library/Frameworks/OpenGL.framework/Versions/A/OpenGL (compatibility version 1.0.0, current version 1.0.0)
	/System/Library/Frameworks/Foundation.framework/Versions/C/Foundation (compatibility version 300.0.0, current version 1450.16.0)
	/System/Library/Frameworks/CoreVideo.framework/Versions/A/CoreVideo (compatibility version 1.2.0, current version 1.5.0)
	/System/Library/Frameworks/CoreMedia.framework/Versions/A/CoreMedia (compatibility version 1.0.0, current version 1.0.0)
	/System/Library/Frameworks/CoreFoundation.framework/Versions/A/CoreFoundation (compatibility version 150.0.0, current version 1450.16.0)
	/System/Library/Frameworks/VideoToolbox.framework/Versions/A/VideoToolbox (compatibility version 1.0.0, current version 1.0.0)
	/System/Library/Frameworks/VideoDecodeAcceleration.framework/Versions/A/VideoDecodeAcceleration (compatibility version 1.0.0, current version 1.0.0)
	/usr/lib/libiconv.2.dylib (compatibility version 7.0.0, current version 7.0.0)
	/System/Library/Frameworks/Security.framework/Versions/A/Security (compatibility version 1.0.0, current version 58286.31.2)
	/System/Library/Frameworks/OpenCL.framework/Versions/A/OpenCL (compatibility version 1.0.0, current version 1.0.0)
	@loader_path/libx264.152.dylib (compatibility version 0.0.0, current version 0.0.0)
	@loader_path/libmp3lame.0.dylib (compatibility version 1.0.0, current version 1.0.0)
	/usr/lib/libSystem.B.dylib (compatibility version 1.0.0, current version 1252.0.0)
	/usr/lib/libbz2.1.0.dylib (compatibility version 1.0.0, current version 1.0.5)
	/usr/lib/libz.1.dylib (compatibility version 1.0.0, current version 1.2.11)
	/System/Library/Frameworks/CoreServices.framework/Versions/A/CoreServices (compatibility version 1.0.0, current version 822.19.0)
	/System/Library/Frameworks/CoreGraphics.framework/Versions/A/CoreGraphics (compatibility version 64.0.0, current version 1129.5.0)
	/System/Library/Frameworks/CoreImage.framework/Versions/A/CoreImage (compatibility version 1.0.1, current version 5.0.0)
	/System/Library/Frameworks/AVFoundation.framework/Versions/A/AVFoundation (compatibility version 1.0.0, current version 2.0.0)
	/System/Library/Frameworks/AudioToolbox.framework/Versions/A/AudioToolbox (compatibility version 1.0.0, current version 492.0.0)
	/System/Library/Frameworks/AppKit.framework/Versions/C/AppKit (compatibility version 45.0.0, current version 1561.20.106)
```
If you look at this detail, you will see that SLINT only copies non-system shared libraries.  It is assumed that
there is no need to copy /System or /usr/lib as they are likely available on the target machine.  Is this a
reasonable assumption?  I do not really know for certain, it just seems reasonable to me.  And to be honest I
haven't had the opportunity to test this on many Macs (just the three I own or have access).

I have tested with Comskip and it appears to work.  I've also tested with [OpenCV](https://opencv.org) to collect
all of the shared libraries needed to access OpenCV from Java code.  It appears to work in my testing and included 46
files.

One note: These entries displayed by otool can have a token like '@rpath/file.dylib'.  I believe it tells the
system to look for the library in the 'standard' locations.  SLINT will try to find these files in two places -
/usr/local and /opt.  Again we are assuming user built libraries will be in these locations.  At this point
where to look for these libraries cannot be controlled from the command line.  Perhaps a way to control the
searched directories from the command line would be a nice thing to add to SLINT.
### Prerequisites

You need a Java 8 JDK.  I used one from Oracle but OpenJDK should work fine.
You also need a Mac.

### Installing

Just clone and build with maven.

## Running the tests

Just one simple test that runs - simply uses the otool program on itself, from Java of course.

## Deployment

Simple 'mvn clean install' will create an executable jar in the target directory.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Doug Barnum** - [jflicks](http://www.jflicks.org)

## License

This project is licensed under the GPL License - see the [COPYING.txt](COPYING.txt) file for details.
