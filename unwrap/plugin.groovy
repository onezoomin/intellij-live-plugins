import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.notification.NotificationType
import java.util.regex.Matcher
import static liveplugin.PluginUtil.*

//
// The action below replaces the current selection
// with the contents of the outermost quotes or block
//

def INFO = com.intellij.notification.NotificationType.INFORMATION

registerAction("Unwrap", "alt ctrl W") { AnActionEvent event ->
	runDocumentWriteAction(event.project) {
		currentEditorIn(event.project).with {

		    def editor = currentEditorIn(event.project)
		    def sel = editor.selectionModel.selectedText
		    def selStart = editor.selectionModel.getSelectionStart()
		    def selEnd = editor.selectionModel.getSelectionEnd()

		    Matcher m
		    if((m = sel =~ /[\["'({](.+)['"})\]]/)){
		        unwrapped = m.group(1)
		        def newSelEnd = selStart + unwrapped.size()
		        document.replaceString(selStart, selEnd, "${unwrapped}")
                caretModel.moveToOffset(newSelEnd)
                editor.selectionModel.setSelection(selStart,newSelEnd)
		    } else {
		        show("no match","Unwrap:",INFO,"1zmin - INFO")
		    }
		}
	}
}
if (!isIdeStartup) show("Use 'alt shift ctrl W' <br>\
    to unwrap the current selection <br>\
    Selection must include: {},[],(),'',or \"\"",
    "Loaded 'UnWrap' action",INFO,"1zmin - LOAD")
