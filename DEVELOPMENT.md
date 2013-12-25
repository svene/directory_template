#Development Notes

Note: this description is for Unix based systems. It might work on Windows with cygwin but I never tried.

##Develpment lifecycle (first time)

###Prerequisites
* make sure you have increased the version number in 'build.gradle' so that it does not conflict with the
latest published version
* after having done some development work in 'directory_template' invoke './gradlew install'. This will install
a snapshot jar into your local maven repository (typically located in $HOME/.m2)
* add local .m2 to grab resolvers in '~/.groovy/grapeConfig.xml': see: http://groovy.codehaus.org/Grape#Grape-CustomizeIvysettings

###Development
I have a 'bin' folder in my HOME directory to which my PATH environment variable points to. In that 'bin' folder I have the following
files:

####gdtdev.sh
groovy `dirname $0`/gdt-dev.groovy $*

####gdt-dev.groovy
//@GrabResolver(name='sven-local', root='file://localhost/home/sven/se/sweng/_github/svene/svene.github.com/maven2/releases')
@Grab(group='org.svenehrke', module='directory_template', version='0.0.14-SNAPSHOT')
import org.svenehrke.directorytemplate.tool.GdtMain


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

###Relese cycle
* make sure version number got increased in build.gradle (compared to previous release)
* do a local installation: ./gradlew install
* remove snapshot grapes: in $HOME/.groovy/grapes/org.svenehrke do a rm -rf directory_template
* in an empty folder try out gdt functionality using a gdtdev.sh and a gdtdev.groovy file which uses a -SNAPSHOT grape

after successful local test:

* git tag -a -m "0.0.xx" "0.0.xx"
* git push
* git push --tags
* ./gradlew makeRelease
* in svene.github.com: commit and push new version to svene's mavenrepo on github
* in gdt_bin: set version to "0.0.xx" in gdt.groovy, commit and push

###Other notes
*find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
