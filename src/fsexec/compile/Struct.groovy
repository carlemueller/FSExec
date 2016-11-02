package fsexec.compile

import fsexec.execute.FSExecModes

class FSEFlow {
  String[] arguments
  boolean argsUsed = false
  Map<String,String> baseEnvironment
  String codeBasePath       // base directory of flow
  String configBasePath     // optional decorating directory structure that provides additional config info
  String workBasePath       // if intermediate files or flow control is used, this is necessary
  String outputBasePath     // if a directory with final output files is needed
  Map rootContext = [:]
  FSEStep rootStep
}

class FSEStep {
  String name
  String index
  String indexPath
  String path
  StepType type
  FSEFlow flow
  FSEStep parentStep
  Map<String,File> stepConfig = [:]
  List<FSEStep> childSteps = []
  FSEScript execInfo
  String flowExecMode = FSExecModes.FLOW_EXEC_SEQUENTIAL
  String stepExecMode = FSExecModes.EXEC_SEQUENTIAL
}

//execute(List commands, String[] envp, File workdir)
class FSEScript {
  FSEStep stepInfo
  List<String> command
  String[] env
  String workDir
  // working info
  // redirectErrorStream, redirectOutputStream
}

class FSEFlowExec {
  Process previousProcess
  String previousProcessExecMode
  String previousProcessOutputFileName
}


enum StepType {
  SHELL, JAVA, JAVASCRIPT, GROOVY
}