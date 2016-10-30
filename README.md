# FSExec
A script / workflow execution framework that modularizes tasks using the filesystem and subdirectories

Here's an example (that you wouldn't use this framework for):

Let's say this piped command is too complex, and we wanted to restructure it using FSExec: 

cat <some file> | grep ccc | tr "[a-z]" "[A-Z]"

This directory structure is created:

sample-cat-grep-upcase
  01-cat
    cat.sh
  02-grep
    grep.sh
  03-upcase
    upcase.sh
    
The execution of FSExec("/Path/To/sample-cat-grep-upcase") should do the same as the piped command.

This is just an illustrative example. FSExec is for organizing much more complicated functions. 
