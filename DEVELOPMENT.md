#Development Notes


##Develpment lifecycle (first time)

###Prerequisites
* make sure you have increased the version number in 'build.gradle' so that it does not conflict with the
latest published version
* after having done some development work in 'directory_template' invoke './gradlew install'. This will install
a snapshot jar into your local maven repository (typically located in $HOME/.m2)
* add local .m2 to grab resolvers: see: http://groovy.codehaus.org/Grape#Grape-CustomizeIvysettings

###Template Development
* after having done some development work in your template (e.g. simplejava)
  * TODO: implement feature 'install from folder'
  * execute: groovy start.groovy install -folder <somefolder>
  the last part of the folder will become the template component name

This will install the template component into $HOME/.gdt

* groovy <path to>/directory_template/start.groovy list
will show the installed template component:

 simplejava: (/home/sven/.gdt/dt_java/templatedirectory/simplejava)

* groovy <path to>/directory_template/start.groovy apply simplejava
  will expand the 'simplejava' template into the current folder

