#Groovy Directory Template

Groovy Directory Template (gdt) helps you to setup new directory structures in a very convenient way.
It is intended to be able to quickly start new programming projects like a normal java project structure and
has the same purpose as maven's archetypes and
therefore it is possible to create directory layouts for a java project for example.

The current templates support the creation of simple java projects. You can also combine the templates. After having
applied the java template you can add support for a gradle build by simply applying the gradle template afterwards.

* Author: Sven Ehrke


##Prerequisites
To use gdt you need a groovy installation. I recommend gvm (http://gvmtool.net/) to easily install and manage your groovy
installation.

##Installation of gdt
On the commandline invoke

* groovy https://raw.github.com/svene/gdt_bin/master/gdt_setup.groovy

This will create a folder .gdt/bin in you home directory with the following structure:

	.gdt
	└─── bin

Please add .gdt/bin to your PATH environment variable.

Then add the standard templates:

	gdt.sh install -github svene dt_java


##Usage
Example:

* mkdir hello
* cd hello
* gdt.sh simplejava       // hit enter when asked for the packagename

Now you have a new directory structure which looks as follows:

	.
	├── .directory_template
	│   └── inputParameters.properties
	└── src
    	└── main
        	└── java
            	└── com
                	└── mycompany
                    	└── mypackage
                        	└── Main.java

If you would have entered a different packagename the folder names would have adapted accordingly. Note also
that the packagename in Main.java is adapted as well.

The folder '.directory_template' contains meta information. For example 'inputParameters.properties' contains
all the answers you have been asked during the application of the template (like the packagename in this case).
This makes it possible for templates which are applied next to propose as default values to you when they need the
same information

If you are working with the gradle build tool you can now apply the gradle template:

* gdt.sh gradle

Note that when the gradle template asks you for the package name it proposes the value you entered before so that
you just can hit enter.

Now that you have a build.gradle file in root of the folder you can run the hello world like this:


* gradle run

which produces the output:

	:compileJava
	:processResources UP-TO-DATE
	:classes
	:run
	hello world

To list the installed templates run:

	> gdt.sh list
	
	       simplejava: (/home/sven/.gdt/dt_java/templates/simplejava)
	directorytemplate: (/home/sven/.gdt/dt_java/templates/directorytemplate)
	           gradle: (/home/sven/.gdt/dt_java/templates/gradle)
	      simpleswing: (/home/sven/.gdt/dt_java/templates/simpleswing)

##Update gdt
On the commandline invoke

* gdt.sh selfupdate

This will update the folder .gdt/bin in you home directory.

