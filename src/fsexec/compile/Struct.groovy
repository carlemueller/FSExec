package fsexec.compile

class ScriptInfo {
  String codeBasePath
  String configBasePath
  String workBasePath
  String outputBasePath
  Map rootContext = [:]
  StepInfo rootStep
}

class StepInfo {
  String name
  String path
  StepType type
  ScriptInfo script
  StepInfo parentStep
  Map stepContext = [:]
  List<StepInfo> childSteps = []
  Object execInfo
}
//execute(List commands, String[] envp, File workdir)
class ShellExecInfo {
  StepInfo stepInfo
  List<String> command
  String[] env
  String workDir
  // working info
  // redirectErrorStream, redirectOutputStream
}

class ScriptExec {
  Process previousProcess

}


enum StepType {
  SHELL, JAVA, JAVASCRIPT, GROOVY
}