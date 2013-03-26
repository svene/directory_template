@GrabResolver(name='sven-github', root='http://svene.github.com/maven2/releases')
@Grab(group='org.svenehrke', module='directorytemplate', version='0.0.5')
import org.svenehrke.directorytemplate.tool.GdtMain

println "hallo"

new GdtMain().run(args)
