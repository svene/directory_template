@GrabResolver(name='sven-github', root='http://svene.github.com/maven2/releases')
@Grab(group='org.svenehrke', module='directory_template', version='0.0.6')
import org.svenehrke.directorytemplate.tool.GdtMain

new GdtMain().run(args)
