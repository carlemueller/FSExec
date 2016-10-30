package fsexec.compile

class FSEFlow {
  String codeBasePath       // base directory of flow
  String configBasePath     // optional decorating directory structure that provides additional config info
  String workBasePath       // if intermediate files or flow control is used, this is necessary
  String outputBasePath     // if a directory with final output files is needed
  Map rootContext = [:]
  FSEStep rootStep
}

class FSEStep {
  String name
  String path
  StepType type
  FSEFlow flow
  FSEStep parentStep
  Map stepContext = [:]
  List<FSEStep> childSteps = []
  Object execInfo
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
}


enum StepType {
  SHELL, JAVA, JAVASCRIPT, GROOVY
}