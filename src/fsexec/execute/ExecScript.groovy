package fsexec.execute

import fsexec.compile.ScriptExec
import fsexec.compile.ScriptInfo
import fsexec.compile.ShellExecInfo
import fsexec.compile.StepInfo

class FSExecutor {

  void exec(ScriptInfo script) {
    ScriptExec exec = new ScriptExec()
    execStep(script.rootStep, exec)
    // send last stream to output
    exec.previousProcess.consumeProcessOutputStream(System.out)
  }

  void execStep(StepInfo stepInfo, ScriptExec exec) {
    for (StepInfo substep : stepInfo.childSteps) {
      execStep(substep,exec)
    }
    // assume shell for now
    ShellExecInfo shellInfo = stepInfo.execInfo
    if (shellInfo != null) {
      if (shellInfo.command != null) {
        ProcessBuilder pb = new ProcessBuilder(shellInfo.command)
        if (shellInfo.workDir != null) {
          pb.directory(new File(shellInfo.workDir))
        }
        if (shellInfo.env != null) {
          pb.environment(shellInfo.env)
        }
        Process process = pb.start()
        if (exec.previousProcess != null) {
          exec.previousProcess.pipeTo(process)
        }
        exec.previousProcess = process
      }
    }
  }
}
