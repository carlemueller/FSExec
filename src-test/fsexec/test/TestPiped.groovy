package fsexec.test

import fsexec.compile.FSExecCompiler
import fsexec.compile.ScriptInfo
import fsexec.execute.FSExecutor


FSExecCompiler compiler = new FSExecCompiler()
FSExecutor executor = new FSExecutor()

ScriptInfo script = compiler.compile("./test-flows/code/test-piped",null,null,null)
executor.exec(script)

