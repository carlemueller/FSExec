package fsexec.compile

class FSEFlow {
  String codeBasePath
  String configBasePath
  String workBasePath
  String outputBasePath
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