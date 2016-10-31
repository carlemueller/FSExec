package fsexec.test

import fsexec.compile.FSEFlow
import fsexec.compile.FSExecCompiler
import fsexec.execute.FSExecutor


FSExecCompiler compiler = new FSExecCompiler()
FSExecutor executor = new FSExecutor()

FSEFlow flow = compiler.compile("./test-flows/code/test-seq",null,"./test-flows/work/test-seq",null,null,["./proto-sample-data"] as String[])
executor.exec(flow)

