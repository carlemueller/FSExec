package fsexec.test

import fsexec.compile.FSEFlow
import fsexec.compile.FSExecCompiler
import fsexec.execute.FSExecutor


FSExecCompiler compiler = new FSExecCompiler()
FSExecutor executor = new FSExecutor()

FSEFlow flow = compiler.compile("./test-flows/code/test-piped",null,null,null,null)
executor.exec(flow)

