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


  FSEFlow compile(String codeBase, String configBase, String workBase, String outputBase) {
    println("code: $codeBase config: $configBase temp: $workBase output: $outputBase")
    FSEFlow info = new FSEFlow(codeBasePath:codeBase,configBasePath:configBase,workBasePath:workBase,outputBasePath:outputBase)
    info.rootStep = compileStep(info, null, "ROOT")
    return info
  }

  FSEStep compileStep(FSEFlow flow, FSEStep parentStep, String stepDirName) {
    FSEStep curStep = new FSEStep(flow:flow, parentStep:parentStep)
    curStep.name = stepDirName
    curStep.path = parentStep == null ? flow.codeBasePath : parentStep.path + "/" + stepDirName

    File codeDir = new File(flow.codeBasePath + "/" + (parentStep == null ? "" : stepDirName))
    File configDir = new File(flow.codeBasePath + "/" + (parentStep == null ? "" : stepDirName))
    // input / output / temp TBD

    List<File> subStepDirs = calcSubStepDirs(flow, curStep, codeDir)
    curStep.childSteps = subStepDirs.collect { compileStep(flow,curStep,it.name) }

    Map<String,File> stepConfig = calcStepFiles(flow,curStep,codeDir)

    // identify type of step
    determineStepType(flow,curStep,stepConfig)

    // identify processing mode: sequential, each line, map reduce, fork join
    // identify piping / transition / flags
    // identify error / finally

    return curStep
  }

  void determineStepType(FSEFlow flow, FSEStep curStep, Map<String,File> stepConfig) {

    for (String cfgkey : stepConfig.keySet()) {
      if (StringUtils.endsWithIgnoreCase(cfgkey,".sh")) {
        println "detected SHELL step: "+cfgkey
        curStep.type = StepType.SHELL
        FSEScript shellExec = new FSEScript(stepInfo:curStep)
        shellExec.command = [curStep.path+"/"+cfgkey]
        curStep.execInfo = shellExec
      }
      if (StringUtils.endsWithIgnoreCase(cfgkey, ".js")) {
        println "detected JAVASCRIPT step: "+cfgkey
        //TODO  StepType.JAVASCRIPT // exec with java js engine
      }
      if (StringUtils.endsWithIgnoreCase(cfgkey, ".jar")) {
        println "detected JAVA step: "+cfgkey
        //TODO StepType.JAVA // exec separate JVM
      }
      if (StringUtils.endsWithIgnoreCase(cfgkey, ".groovy")) {
        println "detected GROOVY step: "+cfgkey
        //TODO StepType.GROOVY // exec with groovy cmd line
      }
    }
  }

  Map<String,File> calcStepFiles(FSEFlow flow, FSEStep curStep, File codeDir) {
    Map<String,File> files = [:]
    File configDir = StringUtils.isNotEmpty(flow.configBasePath) ? new File(flow.configBasePath +"/"+curStep.path) : null
    File[] configFiles = configDir?.isDirectory() ? configDir.listFiles() : [] as File[]
    File[] codeFiles = codeDir.listFiles()
    for (File codeFile : codeFiles) {
      if (!codeFile.name.matches("^[0-9]+(.*)")) {
        files.put(codeFile.name,codeFile)
      }
    }
    for (File configFile : configFiles) {
      files.put(configFile.name, configFile)
    }
    return files
  }

  List<File> calcSubStepDirs(FSEFlow flow, FSEStep curStep, File codeDir) {
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