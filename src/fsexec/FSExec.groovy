package fsexec
import fsexec.compile.FSEFlow
import fsexec.compile.FSExecCompiler
import fsexec.execute.FSExecutor



class FSExec {
  static void main(String[] args) {
    // fsexec args detection and filtering
    String inputBase
    String codeBase
    String configBase
    String workBase
    String outputBase
    // look for FSEexec params
    List filteredArgs = []
    if (args != null) {
      for (int i=0; i < args.length; i++) {
        if (args[i]?.startsWith("-fsexec")) {
          if (args[i] == "-fsexec") { codeBase = args[i+1] }
          if (args[i].endsWith("input")) { inputBase = args[i+1] }
          if (args[i].endsWith("config")) { configBase = args[i+1] }
          if (args[i].endsWith("work")) { workBase = args[i+1] }
          if (args[i].endsWith("output")) { outputBase = args[i+1] }
          // filter/skip the arg value too
          i++
        } else {
          filteredArgs.add(args[i])
        }
      }
    }
    args = filteredArgs as String[]

    // execute flow
    FSExecCompiler compiler = new FSExecCompiler()
    FSExecutor executor = new FSExecutor()

    FSEFlow flow = compiler.compile(codeBase, configBase, workBase, inputBase, outputBase, args)
    executor.exec(flow)
  }
}
