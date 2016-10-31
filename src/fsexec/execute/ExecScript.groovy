package fsexec.execute

import fsexec.compile.FSEFlow
import fsexec.compile.FSEFlowExec
import fsexec.compile.FSEScript
import fsexec.compile.FSEStep

class FSExecutor {

  void exec(FSEFlow script) {
    FSEFlowExec exec = new FSEFlowExec()
    execStep(script.rootStep, exec)
    // send last stream to output
    exec.previousProcess.consumeProcessOutputStream(System.out)
  }

  void execStep(FSEStep stepInfo, FSEFlowExec exec) {

    // execute substeps first
    for (FSEStep substep : stepInfo.childSteps) {
      execStep(substep,exec)
    }

    // execute main task
    // assume shell for now
    FSEScript shellInfo = stepInfo.execInfo
    if (shellInfo != null) {
      if (shellInfo.command != null) {
        ProcessBuilder pb = new ProcessBuilder(shellInfo.command)
        if (shellInfo.workDir != null) {
          pb.directory(new File(shellInfo.workDir))
        }
        if (shellInfo.env != null) {
          pb.environment(shellInfo.env)
        }
        if (exec.previousProcess != null && exec.previousProcessExecMode == FSExecModes.EXEC_SEQUENTIAL) {
          File inputFileFromPreviousStep = new File(exec.previousProcessOutputFileName)
          pb.redirectInput(inputFileFromPreviousStep)
        }

        if (stepInfo.stepExecMode == FSExecModes.EXEC_SEQUENTIAL) {
          exec.previousProcessOutputFileName = stepInfo.flow.workBasePath + "/" + stepInfo.indexPath
          File stepOutputFile = new File(exec.previousProcessOutputFileName)
          File stepErrorFile = new File(exec.previousProcessOutputFileName+".err")
          pb.redirectOutput(stepOutputFile)
          pb.redirectError(stepErrorFile)
        }
        Process process = pb.start()
        if (exec.previousProcess != null) {
          if (exec.previousProcessExecMode == FSExecModes.EXEC_PIPED) {
            exec.previousProcess.pipeTo(process)
          }
        }
        if (stepInfo.stepExecMode == FSExecModes.EXEC_SEQUENTIAL) {
          // we need to wait for the process to complete and capture its output
          process.waitForProcessOutput()
        }
        exec.previousProcess = process
        exec.previousProcessExecMode = stepInfo.stepExecMode
      }
    }
  }
}

// these modes inherit for all substeps
class FSExecModes {
  public static final String FLOW_EXEC_PIPED = "!FLOW_EXEC_PIPED!" // this is the default exec model
  public static final String FLOW_EXEC_SEQUENTIAL = "!FLOW_EXEC_SEQUENTIAL!" // complete each step before executing the next
  public static final String EXEC_PIPED = "!STEP_EXEC_PIPED!" // this is the default exec model
  public static final String EXEC_SEQUENTIAL = "!STEP_EXEC_SEQUENTIAL!" // complete each step before executing the next
}

// System.getEnv returns map.