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

##Update gdt
On the commandline invoke

* groovy https://raw.github.com/svene/gdt_bin/master/gdt_update.groovy

This will update the folder .gdt/bin in you home directory.


##Usage
see https://github.com/svene/dt_java

in your bin folder:
wget https://raw.github.com/svene/directory_template/fix0.0.6/start.groovy -O gdt2.sh

