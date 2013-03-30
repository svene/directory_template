#Development Notes


##Develpment lifecycle (first time)

###Prerequisites
* make sure you have increased the version number in 'build.gradle' so that it does not conflict with the
latest published version
* after having done some development work in 'directory_template' invoke './gradlew install'. This will install
a snapshot jar into your local maven repository (typically located in $HOME/.m2)
* add local .m2 to grab resolvers: see: http://groovy.codehaus.org/Grape#Grape-CustomizeIvysettings

###Template Development
If you have installed a new snapshot version of 'directory_template' into your local maven respository
you first need to manually remove the old cached version from your grapes ($HOME/.groovy/grapes) since the grape mechanism
is not able to detect snapshot updates.

* after having done some development work in your template (e.g. simplejava)
  * TODO: implement feature 'install from folder'
  * execute: groovy start.groovy install -folder <somefolder>
  e.g.: groovy <path to>/directory_template/start.groovy install -folder <path to>/dt_java
  the last part of the folder will become the name of the component template

This will install the template component into $HOME/.gdt

* groovy <path to>/directory_template/start.groovy list
will show the installed template component:

 simplejava: (/home/sven/.gdt/dt_java/templates/simplejava)

* groovy <path to>/directory_template/start.groovy apply simplejava
  will expand the 'simplejava' template into the current folder

###Other notes
*find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
