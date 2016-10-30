package fsexec.compile

import groovy.util.logging.Slf4j

import org.apache.commons.lang3.StringUtils



@Slf4j
class FSExecCompiler {
  static FileFilter dirsOnlyFilter = new FileFilter() {
    public boolean accept(File file) {
      return file.isDirectory();
    }
  };
  static FileFilter filesOnlyFilter = new FileFilter() {
    public boolean accept(File file) {
      return !file.isDirectory();
    }
  };


  FSEScript compile(String codeBase, String configBase, String workBase, String outputBase) {
    println("code: $codeBase config: $configBase temp: $workBase output: $outputBase")
    FSEFlow info = new FSEScript(codeBasePath:codeBase,configBasePath:configBase,workBasePath:workBase,outputBasePath:outputBase)
    info.rootStep = compileStep(info, null, "ROOT")
    return info
  }

  FSEStep compileStep(FSEFlow script, FSEStep parentStep, String stepDirName) {
    FSEStep curStep = new FSEStep(script:script, parentStep:parentStep)
    curStep.name = stepDirName
    curStep.path = parentStep == null ? script.codeBasePath : parentStep.path + "/" + stepDirName

    File codeDir = new File(script.codeBasePath + "/" + (parentStep == null ? "" : stepDirName))
    File configDir = new File(script.codeBasePath + "/" + (parentStep == null ? "" : stepDirName))
    // input / output / temp TBD

    List<File> subStepDirs = calcSubStepDirs(script, curStep, codeDir)
    curStep.childSteps = subStepDirs.collect { compileStep(script,curStep,it.name) }

    // identify type of step
    determineStepType(script,curStep,codeDir)

    // identify processing mode: sequential, each line, map reduce, fork join
    // identify piping / transition / flags
    // identify error / finally

    return curStep
  }

  void determineStepType(FSEScript script, FSEStep curStep, File codeDir) {
    File[] files = codeDir.listFiles(filesOnlyFilter)
    for (File file : files) {
      if (StringUtils.endsWithIgnoreCase(file.name,".sh")) {
        println "detected SHELL step: "+file.name
        curStep.type = StepType.SHELL
        FSEScript shellExec = new FSEScript(stepInfo:curStep)
        shellExec.command = [curStep.path+"/"+file.name]
        curStep.execInfo = shellExec
      }
      if (StringUtils.endsWithIgnoreCase(file.name, ".js")) {
        println "detected JAVASCRIPT step: "+file.name
        //TODO  StepType.JAVASCRIPT // exec with java js engine
      }
      if (StringUtils.endsWithIgnoreCase(file.name, ".jar")) {
        println "detected JAVA step: "+file.name
        //TODO StepType.JAVA // exec separate JVM
      }
      if (StringUtils.endsWithIgnoreCase(file.name, ".groovy")) {
        println "detected GROOVY step: "+file.name
        //TODO StepType.GROOVY // exec with groovy cmd line
      }
    }
  }

  List<File> calcSubStepDirs(FSEScript script, FSEStep curStep, File codeDir) {
    List<File> subStepDirs = []
    File[] dirs = codeDir.listFiles(dirsOnlyFilter)
    for (File dir : dirs) {
      // detect if this is a sequential substep (TODO: 22.11.44.77 sub-sub-steps ordering)
      if (dir.name.matches("^[0-9]+(.*)")) {
        println(" SUBSTEP detected: ${codeDir.name} ${dir.name}")
        subStepDirs.add(dir)
      }
    }
    subStepDirs.sort()
    println ""+subStepDirs
    return subStepDirs
  }

  File checkForSubstepsErrorHandler(FSEScript script, FSEStep curStep, File codeDir) {
    File[] dirs = codeDir.listFiles()
    for (File dir : dirs) {
      if (dir.name.equalsIgnoreCase('@StepError')) {
        return dir
      }
    }
    return null
  }

  File checkForSubstepsFinallyHandler(FSEScript script, FSEStep curStep, File codeDir) {
    File[] dirs = codeDir.listFiles()
    for (File dir : dirs) {
      if (dir.name.equalsIgnoreCase('@StepFinally')) {
        return dir
      }
    }
    return null
  }

  File checkForErrorHandler(FSEScript script, FSEStep curStep, File codeDir) {
    File[] dirs = codeDir.listFiles()
    for (File dir : dirs) {
      if (dir.name.equalsIgnoreCase('@Error')) {
        return dir
      }
    }
    return null
  }

  File checkForFinallyHandler(FSEScript script, FSEStep curStep, File codeDir) {
    File[] dirs = codeDir.listFiles()
    for (File dir : dirs) {
      if (dir.name.equalsIgnoreCase('@Finally')) {
        return dir
      }
    }
    return null
  }

}





Compiler compiler = new Compiler()

compiler.compile("/home/cowardlydragon/AAA-CODE/FS-Exec/FS-Exec/test-flows/code/autorestart-cassandra",null,null,null)