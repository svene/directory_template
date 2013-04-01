#Groovy Directory Template (gdt): Build directory structures including files from a template

* Author: Sven Ehrke

This project provides the base source code to create templates for directory
structures including files. It has the same purpose as maven's archetypes and
such it is possible to create directory layouts for a java project for example.

##Prerequisites
To use gdt you need a groovy installation. I recommend gvm (http://gvmtool.net/) to easily install and manage your groovy
installation.

##Installation of gdt
On the commandline invoke

* groovy https://raw.github.com/svene/gdt_bin/master/gdt_setup.groovy

This will create a folder .gdt/bin in you home directory with the following structure:

	.gdt
	└─── bin

Please append .gdt/bin to your PATH environment variable.


##Usage


##Update gdt
On the commandline invoke

* gdt.sh selfupdate

This will update the folder .gdt/bin in you home directory.

